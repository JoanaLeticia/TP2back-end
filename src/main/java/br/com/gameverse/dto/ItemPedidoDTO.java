package br.com.gameverse.dto;

import jakarta.validation.constraints.NotNull;

public record ItemPedidoDTO(
    @NotNull(message = "O campo de quantidade não pode ser nulo.")
    Integer quantidade,
    @NotNull(message = "O campo de idProduto não pode ser nulo.")
    Long idProduto
) {
}
