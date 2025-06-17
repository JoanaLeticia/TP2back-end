package br.com.gameverse.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoDTO(
                @NotBlank(message = "O campo nome não pode ser nulo.") String nome,
                @NotBlank(message = "O campo descrição não pode ser nulo.") String descricao,
                @NotNull(message = "O campo preço não pode ser nulo.") Double preco,
                @NotNull(message = "O campo estoque não pode ser nulo.") Integer estoque,
                @NotBlank(message = "O campo desenvolvedora não pode ser nulo.") String desenvolvedora,
                Integer idPlataforma,
                Integer idTipoMidia,
                Integer idGenero,
                Integer idClassificacao,
                LocalDate dataLancamento) {

}
