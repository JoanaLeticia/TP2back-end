package br.unitins.tp2.dto;

import br.unitins.tp2.model.Telefone;

import jakarta.validation.constraints.NotBlank;

public record TelefoneDTO(
        @NotBlank(message = "O campo Codigo de Area não pode ser nulo.")
        String codArea,
        @NotBlank(message = "O campo numero não pode ser nulo.")
        String numero) {
    public static TelefoneDTO valueOf(Telefone telefone) {
        return new TelefoneDTO(
                telefone.getCodArea(),
                telefone.getNumero());
    }
}
