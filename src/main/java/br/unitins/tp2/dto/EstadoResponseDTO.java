package br.unitins.tp2.dto;

import br.unitins.tp2.model.Estado;
import br.unitins.tp2.model.Regiao;

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
