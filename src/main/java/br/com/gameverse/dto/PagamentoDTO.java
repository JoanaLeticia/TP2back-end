package br.com.gameverse.dto;

public record PagamentoDTO(
    Integer idMetodo,
    String numeroCartao, // Ou dados específicos do método
    Integer parcelas // Se for cartão
) {}