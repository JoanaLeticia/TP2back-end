package br.com.gameverse.service;

import java.util.List;

import br.com.gameverse.dto.AdministradorDTO;
import br.com.gameverse.dto.AdministradorResponseDTO;
import br.com.gameverse.dto.UsuarioResponseDTO;

public interface AdministradorService {
    AdministradorResponseDTO create(AdministradorDTO administrador);
    AdministradorResponseDTO update(AdministradorDTO administradorDTO, Long id);
    void delete(long id);
    AdministradorResponseDTO findById(long id);
    List<AdministradorResponseDTO> findAll(int page, int pageSize);
    List<AdministradorResponseDTO> findByNome(String nome, int page, int pageSize, String sort);
    long count();
    long count(String nome);
    public UsuarioResponseDTO login(String email, String senha);
}
