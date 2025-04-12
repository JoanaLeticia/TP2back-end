package br.unitins.tp2.service;

import java.util.List;

import br.unitins.tp2.dto.ProdutoDTO;
import br.unitins.tp2.dto.ProdutoResponseDTO;

public interface ProdutoService {
    ProdutoResponseDTO create(ProdutoDTO produto);
    ProdutoResponseDTO update(ProdutoDTO dto, Long id);
    void delete(long id);
    ProdutoResponseDTO findById(long id);
    List<ProdutoResponseDTO> findAll(int page, int pageSize, String sort);
    List<ProdutoResponseDTO> findByNome(String nome, int page, int pageSize, String sort);
    long count();
    long count(String nome);
}
