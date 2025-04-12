package br.unitins.tp2.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.tp2.model.Pedido;

public record PedidoResponseDTO(
        Long idPedido,
        LocalDateTime horario,
        Long idCliente,
        String nome,
        String login,
        Double valorTotal,
        List<ItemPedidoResponseDTO> itens,
        List<EnderecoResponseDTO> enderecosCliente) {

    public static PedidoResponseDTO valueOf(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        Long idCliente = null;
        String nomeCliente = null;
        String loginCliente = null;
        List<EnderecoResponseDTO> enderecosCliente = null;

        // Verifica se o cliente do pedido não é nulo antes de acessar seus atributos
        if (pedido.getCliente() != null) {
            idCliente = pedido.getCliente().getId();
            nomeCliente = pedido.getCliente().getNome();
            loginCliente = pedido.getCliente().getEmail();
            // Mapeia os endereços do cliente
            enderecosCliente = pedido.getCliente().getEndereco()
                                    .stream()
                                    .map(EnderecoResponseDTO::valueOf)
                                    .toList();
        }

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getDataHora(),
                idCliente,
                nomeCliente,
                loginCliente,
                pedido.getValorTotal(),
                ItemPedidoResponseDTO.valueOf(pedido.getItens()),
                enderecosCliente
        );
    }
}
