package br.com.gameverse.dto;

import br.com.gameverse.model.Telefone;

public record TelefoneResponseDTO(
        Long id,
        String codArea,
        String numero) {
    public static TelefoneResponseDTO valueOf(Telefone telefone) {
        return new TelefoneResponseDTO(
                telefone.getId(),
                telefone.getCodArea(),
                telefone.getNumero());
    }
}
