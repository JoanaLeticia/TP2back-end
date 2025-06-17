package br.com.gameverse.service;

import java.util.List;

import br.com.gameverse.dto.EstadoDTO;
import br.com.gameverse.dto.EstadoResponseDTO;

public interface EstadoService {
    EstadoResponseDTO create(EstadoDTO estado);
    EstadoResponseDTO update(EstadoDTO dto, Long id);
    void delete(long id);
    EstadoResponseDTO findById(long id);
    EstadoResponseDTO findBySigla(String sigla);
    List<EstadoResponseDTO> findAll(int page, int pageSize);
    List<EstadoResponseDTO> findByNome(String nome, int page, int pageSize, String sort);
    long count();
    long count(String nome);
}