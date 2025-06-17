package br.com.gameverse.dto;

import java.time.LocalDate;

import br.com.gameverse.model.ClassificacaoIndicativa;
import br.com.gameverse.model.Genero;
import br.com.gameverse.model.Plataforma;
import br.com.gameverse.model.Produto;
import br.com.gameverse.model.TipoMidia;

public record ProdutoResponseDTO(
    Long id,
    String nome,
    String descricao,
    Double preco,
    Integer estoque,
    String desenvolvedora,
    Plataforma plataforma,
    TipoMidia tipoMidia,
    Genero genero,
    ClassificacaoIndicativa classificacao,
    LocalDate dataLancamento,
    String nomeImagem
) {
    public static ProdutoResponseDTO valueOf(Produto produto) {
        return new ProdutoResponseDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getEstoque(),
            produto.getDesenvolvedora(),
            produto.getPlataforma(),
            produto.getTipoMidia(),
            produto.getGenero(),
            produto.getClassificacao(),
            produto.getDataLancamento(),
            produto.getNomeImagem()
        );
    }
}