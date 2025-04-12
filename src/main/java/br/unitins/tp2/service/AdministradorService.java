package br.unitins.tp2.service;

import java.util.List;

import br.unitins.tp2.dto.AdministradorDTO;
import br.unitins.tp2.dto.AdministradorResponseDTO;
import br.unitins.tp2.dto.UsuarioResponseDTO;

public interface AdministradorService {
    AdministradorResponseDTO create(AdministradorDTO administrador);
    AdministradorResponseDTO update(AdministradorDTO administradorDTO, Long id);
    void delete(long id);
    AdministradorResponseDTO findById(long id);
    List<AdministradorResponseDTO> findAll(int page, int pageSize, String sort);
    List<AdministradorResponseDTO> findByNome(String nome, int page, int pageSize, String sort);
    long count();
    long count(String nome);
    public UsuarioResponseDTO login(String email, String senha);
}
