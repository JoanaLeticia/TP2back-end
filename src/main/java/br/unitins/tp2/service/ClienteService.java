package br.unitins.tp2.service;

import java.util.List;

import br.unitins.tp2.dto.ClienteDTO;
import br.unitins.tp2.dto.ClienteResponseDTO;
import br.unitins.tp2.dto.UsuarioResponseDTO;

public interface ClienteService {
    ClienteResponseDTO create(ClienteDTO cliente);
    ClienteResponseDTO update(ClienteDTO clienteDTO, Long id);
    void delete(long id);
    ClienteResponseDTO findById(long id);
    List<ClienteResponseDTO> findAll(int page, int pageSize, String sort);
    List<ClienteResponseDTO> findByNome(String nome, int page, int pageSize, String sort);
    long count();
    long count(String nome);
    public UsuarioResponseDTO login(String email, String senha);
    public ClienteResponseDTO findByEmail(String email);
}
