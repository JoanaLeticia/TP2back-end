package br.com.gameverse.service;

import java.util.List;

import br.com.gameverse.dto.ClienteDTO;
import br.com.gameverse.dto.ClienteResponseDTO;
import br.com.gameverse.dto.ClienteUpdateDTO;
import br.com.gameverse.dto.UsuarioResponseDTO;

public interface ClienteService {
    ClienteResponseDTO create(ClienteDTO cliente);
    ClienteResponseDTO update(ClienteDTO clienteDTO, Long id);
    void updatePartial(ClienteUpdateDTO dto, Long id);
    void delete(long id);
    public UsuarioResponseDTO registrar(ClienteDTO clienteDTO);
    ClienteResponseDTO findById(long id);
    List<ClienteResponseDTO> findAll(int page, int pageSize);
    List<ClienteResponseDTO> findByNome(String nome, int page, int pageSize, String sort);
    long count();
    long count(String nome);
    public UsuarioResponseDTO login(String email, String senha);
    ClienteResponseDTO findByEmail(String email);
}
