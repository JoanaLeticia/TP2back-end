package br.unitins.tp2.service;

import java.util.List;

import br.unitins.tp2.dto.MunicipioDTO;
import br.unitins.tp2.dto.MunicipioResponseDTO;

public interface MunicipioService {
    MunicipioResponseDTO create(MunicipioDTO municipio);
    MunicipioResponseDTO update(MunicipioDTO dto, Long id);
    void delete(long id);
    MunicipioResponseDTO findById(long id);
    List<MunicipioResponseDTO> findAll(int page, int pageSize, String sort);
    List<MunicipioResponseDTO> findByNome(String nome, int page, int pageSize, String sort);
    List<MunicipioResponseDTO> findByEstado(Long idEstado, int page, int pageSize, String sort);
    long count();
    long count(String nome);
}
