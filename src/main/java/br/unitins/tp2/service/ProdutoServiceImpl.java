package br.unitins.tp2.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.unitins.tp2.dto.ProdutoDTO;
import br.unitins.tp2.dto.ProdutoResponseDTO;
import br.unitins.tp2.model.ClassificacaoIndicativa;
import br.unitins.tp2.model.Genero;
import br.unitins.tp2.model.Produto;
import br.unitins.tp2.model.TipoMidia;
import br.unitins.tp2.repository.ProdutoRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProdutoServiceImpl implements ProdutoService {
    @Inject
    ProdutoRepository produtoRepository;

    @Override
    @Transactional
    public ProdutoResponseDTO create(ProdutoDTO produto) {
        Produto novoProduto = new Produto();
        novoProduto.setNome(produto.nome());
        novoProduto.setDescricao(produto.descricao());
        novoProduto.setPreco(produto.preco());
        novoProduto.setEstoque(produto.estoque());
        novoProduto.setDesenvolvedora(produto.desenvolvedora());

        TipoMidia tipoMidia = TipoMidia.valueOf((int) produto.idTipoMidia());
        novoProduto.setTipoMidia(tipoMidia);

        Genero genero = Genero.valueOf((int) produto.idGenero());
        novoProduto.setGenero(genero);

        ClassificacaoIndicativa classificacao = ClassificacaoIndicativa.valueOf((int) produto.idClassificacao());
        novoProduto.setClassificacao(classificacao);

        novoProduto.setDataLancamento(produto.dataLancamento());
        novoProduto.setCapaUrl(produto.capaUrl());

        produtoRepository.persist(novoProduto);

        return ProdutoResponseDTO.valueOf(novoProduto);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO update(ProdutoDTO dto, Long id) {
        Produto produtoEditado = produtoRepository.findById(id);

        produtoEditado.setNome(dto.nome());
        produtoEditado.setDescricao(dto.descricao());
        produtoEditado.setPreco(dto.preco());
        produtoEditado.setEstoque(dto.estoque());
        produtoEditado.setDesenvolvedora(dto.desenvolvedora());
        produtoEditado.setTipoMidia(TipoMidia.valueOf(dto.idTipoMidia()));
        produtoEditado.setGenero(Genero.valueOf(dto.idGenero()));
        produtoEditado.setClassificacao(ClassificacaoIndicativa.valueOf(dto.idClassificacao()));

        return ProdutoResponseDTO.valueOf(produtoEditado);
    }

    @Override
    @Transactional
    public void delete(long id) {
        produtoRepository.deleteById(id);
    }

    @Override
    public ProdutoResponseDTO findById(long id) {
        Produto produto = produtoRepository.findById(id);
        return ProdutoResponseDTO.valueOf(produto);
    }

    @Override
    public List<ProdutoResponseDTO> findAll(int page, int pageSize, String sort) {
        String query = "";
        Map<String, Object> params = new HashMap<>();

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "nome":
                    query = "order by nome";
                    break;
                case "nome desc":
                    query = "order by nome desc";
                    break;
                default:
                    query = "order by id";
            }
        } else {
            query = "order by id";
        }

        PanacheQuery<Produto> panacheQuery = produtoRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(produto -> ProdutoResponseDTO.valueOf(produto))
            .collect(Collectors.toList());
    }

    @Override
    public List<ProdutoResponseDTO> findByNome(String nome, int page, int pageSize, String sort) {
        String query = "UPPER(nome) LIKE UPPER(:nome)";
        Map<String, Object> params = new HashMap<>();
        params.put("nome", "%" + nome + "%");

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "nome":
                    query += " order by nome";
                    break;
                case "nome desc":
                    query += " order by nome desc";
                    break;
                default:
                    query += " order by id";
            }
        } else {
            query += " order by id";
        }

        PanacheQuery<Produto> panacheQuery = produtoRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(produto -> ProdutoResponseDTO.valueOf(produto))
            .collect(Collectors.toList());
    }

    public List<ProdutoResponseDTO> findByNome(String nome) {
        return produtoRepository.findByNome(nome).stream()
                .map(e -> ProdutoResponseDTO.valueOf(e)).toList();
    }

    @Override
    public long count() {
        return produtoRepository.findAll().count();
    }

    @Override
    public long count(String nome) {
        return produtoRepository.countByNome(nome);
    }

}
