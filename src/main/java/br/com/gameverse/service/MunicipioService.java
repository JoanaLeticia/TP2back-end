package br.com.gameverse.service;

import java.util.List;

import br.com.gameverse.dto.MunicipioDTO;
import br.com.gameverse.dto.MunicipioResponseDTO;
import br.com.gameverse.model.Estado;

public interface MunicipioService {
    MunicipioResponseDTO create(MunicipioDTO municipio);
    MunicipioResponseDTO update(MunicipioDTO dto, Long id);
    void delete(long id);
    MunicipioResponseDTO findById(long id);
    public List<MunicipioResponseDTO> findAll(int page, int pageSize);
    List<MunicipioResponseDTO> findByNome(String nome, int page, int pageSize, String sort);
    List<MunicipioResponseDTO> findByEstado(Long idEstado, int page, int pageSize, String sort);
    long count();
    long count(String nome);
    List<Estado> getEstados();
    List<MunicipioResponseDTO> findByEstado(Long idEstado);
    List<MunicipioResponseDTO> findByNome(String nome);
}
