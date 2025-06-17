package br.com.gameverse.dto;

import java.time.LocalDateTime;

import br.com.gameverse.model.MetodoPagamento;
import br.com.gameverse.model.Pagamento;
import br.com.gameverse.model.StatusPagamento;

public record PagamentoResponseDTO (
    Long id,
    MetodoPagamento metodo,
    LocalDateTime dataPagamento,
    String codigoTransacao,
    StatusPagamento status
) {
    public static PagamentoResponseDTO valueOf(Pagamento pagamento) {
        return new PagamentoResponseDTO(pagamento.getId(), pagamento.getMetodo(), pagamento.getDataPagamento(), pagamento.getCodigoTransacao(), pagamento.getStatus());
    }
}
