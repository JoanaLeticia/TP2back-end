package br.com.gameverse.dto;

import br.com.gameverse.model.Perfil;

public record AuthUsuarioDTO(
    String login,
    String senha,
    Perfil perfil
) {
}
