package br.com.gameverse.service;

import java.util.List;

import br.com.gameverse.dto.PagamentoDTO;
import br.com.gameverse.dto.PagamentoResponseDTO;

public interface PagamentoService {
    PagamentoResponseDTO create(PagamentoDTO pagamento);
    PagamentoResponseDTO update(PagamentoDTO dto, Long id);
    void delete(long id);
    PagamentoResponseDTO findById(long id);
    public List<PagamentoResponseDTO> findAll(int page, int size);
    List<PagamentoResponseDTO> findByNumeroCartao(String numeroCartao, int page, int size, String sort);
    long count();
    long count(String numeroCartao);
}
