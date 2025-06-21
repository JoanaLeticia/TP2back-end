package br.com.gameverse.dto;

import java.sql.Date;
import java.util.List;

public record ClienteUpdateDTO(
    String nome,
    Date dataNascimento,
    String cpf,
    List<TelefoneDTO> listaTelefone
) {}