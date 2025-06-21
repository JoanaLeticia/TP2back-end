package br.com.gameverse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TelefoneDTO(
        Long id,
        @NotBlank(message = "O campo Codigo de Area não pode ser nulo.")
        String codArea,
        @NotBlank(message = "O campo numero não pode ser nulo.")
        String numero,
        Long idCliente
) {
}
