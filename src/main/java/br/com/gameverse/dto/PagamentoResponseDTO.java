package br.com.gameverse.dto;

import java.time.LocalDateTime;

import br.com.gameverse.model.MetodoPagamento;
import br.com.gameverse.model.Pagamento;
import br.com.gameverse.model.StatusPagamento;

public record PagamentoResponseDTO(
        Long id,
        MetodoPagamento metodo,
        LocalDateTime dataPagamento,
        String codigoTransacao,
        StatusPagamento status,
        Long pedidoId,
        String numeroCartaoMascarado) {
    public static PagamentoResponseDTO valueOf(Pagamento pagamento) {
        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getMetodo(),
                pagamento.getDataPagamento(),
                pagamento.getCodigoTransacao(),
                pagamento.getStatus(),
                pagamento.getPedido() != null ? pagamento.getPedido().getId() : null,
                mascararNumeroCartao(pagamento.getNumeroCartao(), pagamento.getMetodo()));
    }

    private static String mascararNumeroCartao(String numeroCartao, MetodoPagamento metodo) {
        if (metodo != MetodoPagamento.CARTAO_CREDITO && metodo != MetodoPagamento.CARTAO_DEBITO) {
            return "N/A";
        }
        if (numeroCartao == null) {
            return "****";
        }

        String apenasNumeros = numeroCartao.replaceAll("[^0-9]", "");

        if (apenasNumeros.length() < 4) {
            return "****";
        }

        return "****-****-****-" + apenasNumeros.substring(apenasNumeros.length() - 4);
    }
}
