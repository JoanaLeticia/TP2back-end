package br.com.gameverse.service;

import java.util.List;

import br.com.gameverse.dto.EnderecoDTO;
import br.com.gameverse.dto.EnderecoResponseDTO;

public interface EnderecoService {
    EnderecoResponseDTO create(EnderecoDTO endereco);
    EnderecoResponseDTO update(EnderecoDTO dto, Long id);
    void delete(long id);
    EnderecoResponseDTO findById(long id);
    List<EnderecoResponseDTO> findByMunicipio(Long idMunicipio, int page, int pageSize, String sort);
    List<EnderecoResponseDTO> findAll(int page, int pageSize, String sort);
    List<EnderecoResponseDTO> findByLogradouro(String logradouro, int page, int pageSize, String sort);
    List<EnderecoResponseDTO> findByBairro(String bairro, int page, int pageSize, String sort);
    long countByBairro(String bairro);
    long count();
    long count(String logradouro);
    List<EnderecoResponseDTO> findByClienteId(Long clienteId);
}
