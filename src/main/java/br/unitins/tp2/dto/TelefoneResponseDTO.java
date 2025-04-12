package br.unitins.tp2.dto;

import br.unitins.tp2.model.Telefone;

public record TelefoneResponseDTO(
        String codArea,
        String numero) {
    public static TelefoneResponseDTO valueOf(Telefone telefone) {
        return new TelefoneResponseDTO(
                telefone.getCodArea(),
                telefone.getNumero());
    }
}
