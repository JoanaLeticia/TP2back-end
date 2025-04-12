package br.unitins.tp2.dto;

import br.unitins.tp2.model.Perfil;
import br.unitins.tp2.model.Usuario;

public record UsuarioResponseDTO(
    Long id,
    String nome,
    String login,
    Perfil perfil
) { 
    public static UsuarioResponseDTO valueOf(Usuario usuario){
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getPerfil()
        );
    }
    
    
}