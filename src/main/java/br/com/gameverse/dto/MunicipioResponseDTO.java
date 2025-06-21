package br.com.gameverse.dto;

import br.com.gameverse.model.Municipio;

public record MunicipioResponseDTO(
        Long id,
        String nome,
        EstadoResponseDTO estado ) {
    public static MunicipioResponseDTO valueOf(Municipio municipio) {
        return new MunicipioResponseDTO(
            municipio.getId(), municipio.getNome(),
            EstadoResponseDTO.valueOf(municipio.getEstado()));
    }
}
