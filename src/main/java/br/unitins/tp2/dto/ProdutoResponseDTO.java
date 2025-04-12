package br.unitins.tp2.dto;

import java.time.LocalDate;

import br.unitins.tp2.model.ClassificacaoIndicativa;
import br.unitins.tp2.model.Genero;
import br.unitins.tp2.model.Produto;
import br.unitins.tp2.model.TipoMidia;

public record ProdutoResponseDTO(
    Long id,
    String nome,
    String descricao,
    Double preco,
    Integer estoque,
    String desenvolvedora,
    TipoMidia tipoMidia,
    Genero genero,
    ClassificacaoIndicativa classificacao,
    LocalDate dataLancamento,
    String capaUrl
) {
    public static ProdutoResponseDTO valueOf(Produto produto) {
        return new ProdutoResponseDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getEstoque(),
            produto.getDesenvolvedora(),
            produto.getTipoMidia(),
            produto.getGenero(),
            produto.getClassificacao(),
            produto.getDataLancamento(),
            produto.getCapaUrl());
    }
}