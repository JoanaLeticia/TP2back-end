package br.com.gameverse.dto;

import br.com.gameverse.model.Estado;
import br.com.gameverse.model.Regiao;

public record EstadoResponseDTO(
        Long id,
        String nome,
        String sigla,
        Regiao regiao) {

    public static EstadoResponseDTO valueOf(Estado estado) {
        return new EstadoResponseDTO(
                estado.getId(),
                estado.getNome(),
                estado.getSigla(),
                estado.getRegiao());
    }
}
