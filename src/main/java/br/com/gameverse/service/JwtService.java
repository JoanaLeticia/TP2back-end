package br.com.gameverse.service;

import br.com.gameverse.dto.UsuarioResponseDTO;

public interface JwtService {

    public String generateJwt(UsuarioResponseDTO dto);
    
}
