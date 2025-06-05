package br.com.gameverse.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;

public record PedidoDTO (
    @NotBlank(message = "O campo itens de venda não pode ser nulo")
    List<ItemPedidoDTO> itens
) {
    
}
