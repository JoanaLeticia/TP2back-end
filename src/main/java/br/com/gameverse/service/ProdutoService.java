package br.com.gameverse.service;

import java.util.List;
import java.util.Map;

import br.com.gameverse.dto.ProdutoDTO;
import br.com.gameverse.dto.ProdutoResponseDTO;
import jakarta.validation.Valid;

public interface ProdutoService {
    ProdutoResponseDTO create(@Valid ProdutoDTO produto);
    ProdutoResponseDTO update(ProdutoDTO dto, Long id);
    void delete(long id);
    ProdutoResponseDTO salvarImagem(Long id, String nomeImagem);
    ProdutoResponseDTO findById(long id);
    List<ProdutoResponseDTO> findAll(int page, int pageSize);
    List<ProdutoResponseDTO> findByNome(String nome, int page, int pageSize, String sort);
    List<ProdutoResponseDTO> buscarPorPlataforma(String nomePlataforma, int page, int pageSize, String sort, String genero, String desenvolvedora, Double precoMax);
    long count();
    long count(String nome);
    long countPorPlataforma(String nomePlataforma, String genero, String desenvolvedora, Double precoMax);
    Map<String, Object> getFiltrosPorPlataforma(String nomePlataforma);
}
