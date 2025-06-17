package br.com.gameverse.dto;

import br.com.gameverse.model.Perfil;
import br.com.gameverse.model.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        Perfil perfil) {

    public static UsuarioResponseDTO valueOf(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil());
    }

}