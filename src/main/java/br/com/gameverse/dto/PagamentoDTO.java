package br.com.gameverse.dto;

public record PagamentoDTO(
    Integer idMetodo,
    String numeroCartao,
    Integer parcelas,
    Long pedidoId
) {}