package br.com.gameverse.dto;

import java.util.List;

import br.com.gameverse.model.ItemPedido;

public record ItemPedidoResponseDTO(
        Integer quantidade,
        Double valor,
        Long idProduto,
        String nome,
        String nomeImagem
) {
    public static ItemPedidoResponseDTO valueOf(ItemPedido item) {
        return new ItemPedidoResponseDTO(
            item.getQuantidade(),
            item.getValor(),
            item.getProduto().getId(),
            item.getProduto().getNome(),
            item.getProduto().getNomeImagem()
        );
    }

    public static List<ItemPedidoResponseDTO> valueOf(List<ItemPedido> item) {
        return item.stream().map(i -> ItemPedidoResponseDTO.valueOf(i)).toList();
    }
}
