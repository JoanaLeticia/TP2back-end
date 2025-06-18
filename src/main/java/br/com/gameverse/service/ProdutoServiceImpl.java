package br.com.gameverse.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.gameverse.dto.ProdutoDTO;
import br.com.gameverse.dto.ProdutoResponseDTO;
import br.com.gameverse.model.ClassificacaoIndicativa;
import br.com.gameverse.model.Genero;
import br.com.gameverse.model.Plataforma;
import br.com.gameverse.model.Produto;
import br.com.gameverse.model.TipoMidia;
import br.com.gameverse.repository.ProdutoRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@ApplicationScoped
public class ProdutoServiceImpl implements ProdutoService {
    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    Validator validator;

    @Override
    @Transactional
    public ProdutoResponseDTO create(@Valid ProdutoDTO produto) throws ConstraintViolationException {
        Produto novoProduto = new Produto();
        novoProduto.setNome(produto.nome());
        novoProduto.setDescricao(produto.descricao());
        novoProduto.setPreco(produto.preco());
        novoProduto.setEstoque(produto.estoque());
        novoProduto.setDesenvolvedora(produto.desenvolvedora());
        novoProduto.setPlataforma(Plataforma.valueOf(produto.idPlataforma()));
        novoProduto.setTipoMidia(TipoMidia.valueOf(produto.idTipoMidia()));
        novoProduto.setGenero(Genero.valueOf(produto.idGenero()));
        novoProduto.setClassificacao(ClassificacaoIndicativa.valueOf(produto.idClassificacao()));
        novoProduto.setDataLancamento(produto.dataLancamento());

        produtoRepository.persist(novoProduto);

        return ProdutoResponseDTO.valueOf(novoProduto);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO update(ProdutoDTO dto, Long id) throws ConstraintViolationException {
        validar(dto);

        Produto produtoEditado = produtoRepository.findById(id);

        produtoEditado.setNome(dto.nome());
        produtoEditado.setDescricao(dto.descricao());
        produtoEditado.setPreco(dto.preco());
        produtoEditado.setEstoque(dto.estoque());
        produtoEditado.setDesenvolvedora(dto.desenvolvedora());
        if (dto.idPlataforma() != null) {
            produtoEditado.setPlataforma(Plataforma.valueOf(dto.idPlataforma()));
        }

        if (dto.idPlataforma() != null) {
            produtoEditado.setTipoMidia(TipoMidia.valueOf(dto.idTipoMidia()));
        }

        if (dto.idGenero() != null) {
            produtoEditado.setGenero(Genero.valueOf(dto.idGenero()));
        }

        if (dto.idClassificacao() != null) {
            produtoEditado.setClassificacao(ClassificacaoIndicativa.valueOf(dto.idPlataforma()));
        }

        return ProdutoResponseDTO.valueOf(produtoEditado);
    }

    @Override
    @Transactional
    public void delete(long id) {
        produtoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO salvarImagem(Long id, String nomeImagem) {

        Produto entity = produtoRepository.findById(id);
        entity.setNomeImagem(nomeImagem);

        return ProdutoResponseDTO.valueOf(entity);
    }

    private void validar(ProdutoDTO produtoDTO) throws ConstraintViolationException {
        Set<ConstraintViolation<ProdutoDTO>> violations = validator.validate(produtoDTO);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

    @Override
    public ProdutoResponseDTO findById(long id) {
        Produto produto = produtoRepository.findById(id);
        return ProdutoResponseDTO.valueOf(produto);
    }

    @Override
    public List<ProdutoResponseDTO> findAll(int page, int pageSize) {
        List<Produto> list = produtoRepository.findAll().page(page, pageSize).list();
        return list.stream().map(e -> ProdutoResponseDTO.valueOf(e)).collect(Collectors.toList());
    }

    @Override
    public List<ProdutoResponseDTO> findByNome(String nome, int page, int pageSize, String sort) {
        List<String> allowedSortFields = List.of("id", "nome", "preco", "estoque");

        String orderByClause = "order by id"; // padrão

        if (sort != null && !sort.isBlank()) {
            String[] sortParts = sort.trim().split(" ");
            String field = sortParts[0];
            String direction = (sortParts.length > 1) ? sortParts[1].toLowerCase() : "asc";

            if (allowedSortFields.contains(field)) {
                if (direction.equals("desc") || direction.equals("asc")) {
                    orderByClause = String.format("order by %s %s", field, direction);
                } else {
                    orderByClause = String.format("order by %s", field);
                }
            }
        }

        String query = "lower(nome) like lower(:nome) " + orderByClause;

        PanacheQuery<Produto> panacheQuery = produtoRepository
                .find(query, Parameters.with("nome", "%" + nome + "%"));

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(Page.of(page, pageSize));
        }

        return panacheQuery.list().stream()
                .map(ProdutoResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProdutoResponseDTO> buscarPorPlataforma(String nomePlataforma, int page, int pageSize, String sort) {
        List<String> allowedSortFields = List.of("id", "nome", "preco", "estoque");

        String orderByClause = "order by id"; // padrão

        if (sort != null && !sort.isBlank()) {
            String[] sortParts = sort.trim().split(" ");
            String field = sortParts[0];
            String direction = (sortParts.length > 1) ? sortParts[1].toLowerCase() : "asc";

            if (allowedSortFields.contains(field)) {
                if (direction.equals("desc") || direction.equals("asc")) {
                    orderByClause = String.format("order by %s %s", field, direction);
                } else {
                    orderByClause = String.format("order by %s", field);
                }
            }
        }

        try {
            Plataforma plataforma = Plataforma.valueOf(nomePlataforma.toUpperCase());

            String query = "plataforma = :plataforma " + orderByClause;

            PanacheQuery<Produto> panacheQuery = produtoRepository.find(query,
                    Parameters.with("plataforma", plataforma));

            if (pageSize > 0) {
                panacheQuery = panacheQuery.page(Page.of(page, pageSize));
            }

            return panacheQuery.list().stream()
                    .map(ProdutoResponseDTO::valueOf)
                    .collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Plataforma inválida: " + nomePlataforma);
        }
    }

    @Override
    public long count() {
        return produtoRepository.findAll().count();
    }

    @Override
    public long count(String nome) {
        return produtoRepository.countByNome(nome);
    }

    @Override
    public long countPorPlataforma(String nomePlataforma) {
        Plataforma plataforma = Plataforma.valueOf(nomePlataforma.toUpperCase());
        return produtoRepository.countByPlataforma(plataforma);
    }

    @Override
public Map<String, Object> getFiltrosPorPlataforma(String nomePlataforma) {
    Map<String, Object> filtros = new HashMap<>();
    
    try {
        Plataforma plataforma = Plataforma.valueOf(nomePlataforma.toUpperCase());
        
        // Obter gêneros distintos
        List<String> generos = produtoRepository.findGenerosByPlataforma(plataforma)
            .stream()
            .map(Enum::name)
            .collect(Collectors.toList());
        
        // Obter desenvolvedoras distintas
        List<String> desenvolvedoras = produtoRepository.findDesenvolvedorasByPlataforma(plataforma);
        
        // Obter faixas de preço (usando a implementação dinâmica que criamos)
        List<Double> faixasPreco = produtoRepository.findProdutosPrecoByPlataforma(plataforma);
        
        // Adicionar ao mapa de filtros
        filtros.put("generos", generos);
        filtros.put("desenvolvedoras", desenvolvedoras);
        filtros.put("faixasPreco", faixasPreco);
        
        // Opcional: adicionar estatísticas úteis
        filtros.put("quantidadeProdutos", produtoRepository.countByPlataforma(plataforma));
        
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Plataforma inválida: " + nomePlataforma);
    }
    
    return filtros;
}

}
