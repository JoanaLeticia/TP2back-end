package br.com.gameverse.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.com.gameverse.model.Pedido;
import br.com.gameverse.model.StatusPedido;

public record PedidoResponseDTO(
        Long idPedido,
        LocalDateTime horario,
        Long idCliente,
        String nome,
        String email,
        Double valorTotal,
        List<ItemPedidoResponseDTO> itens,
        EnderecoResponseDTO enderecoEntrega,
        StatusPedido statusPedido,
        PagamentoResponseDTO pagamento) {

    public static PedidoResponseDTO valueOf(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        Long idCliente = null;
        String nomeCliente = null;
        String emailCliente = null;

        // Verifica se o cliente do pedido não é nulo antes de acessar seus atributos
        if (pedido.getCliente() != null) {
            idCliente = pedido.getCliente().getId();
            nomeCliente = pedido.getCliente().getNome();
            emailCliente = pedido.getCliente().getEmail();
        }

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getDataHora(),
                idCliente,
                nomeCliente,
                emailCliente,
                pedido.getValorTotal(),
                ItemPedidoResponseDTO.valueOf(pedido.getItens()),
                EnderecoResponseDTO.valueOf(pedido.getEndereco()),
                pedido.getStatus(),
                pedido.getPagamento() != null ? PagamentoResponseDTO.valueOf(pedido.getPagamento()) : null
        );
    }
}
