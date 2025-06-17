package br.com.gameverse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MunicipioDTO(
    @NotBlank(message = "O campo nome não pode ser nulo.")
    String nome,
    @NotNull(message = "O campo estado não pode ser nulo.")
    Long idEstado
) {
}
