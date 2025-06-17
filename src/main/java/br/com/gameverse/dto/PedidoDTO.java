package br.com.gameverse.dto;

import java.util.List;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;

public record PedidoDTO (
    @NotBlank(message = "O campo itens de venda não pode ser nulo")
    List<ItemPedidoDTO> itens,
    @NotNull
    Long enderecoId,
    @NotNull
    PagamentoDTO pagamento
) {
    
}
