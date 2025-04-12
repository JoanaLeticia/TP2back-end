package br.unitins.tp2.service;

import java.util.List;

import br.unitins.tp2.dto.EstadoDTO;
import br.unitins.tp2.dto.EstadoResponseDTO;

public interface EstadoService {
    EstadoResponseDTO create(EstadoDTO estado);
    EstadoResponseDTO update(EstadoDTO dto, Long id);
    void delete(long id);
    EstadoResponseDTO findById(long id);
    EstadoResponseDTO findBySigla(String sigla);
    List<EstadoResponseDTO> findAll(int page, int pageSize, String sort);
    List<EstadoResponseDTO> findByNome(String nome, int page, int pageSize, String sort);
    long count();
    long count(String nome);
}