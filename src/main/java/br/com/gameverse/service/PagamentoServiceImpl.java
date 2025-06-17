package br.com.gameverse.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.gameverse.dto.PagamentoDTO;
import br.com.gameverse.dto.PagamentoResponseDTO;
import br.com.gameverse.model.MetodoPagamento;
import br.com.gameverse.model.Pagamento;
import br.com.gameverse.model.StatusPagamento;
import br.com.gameverse.repository.PagamentoRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PagamentoServiceImpl implements PagamentoService {
    @Inject
    PagamentoRepository pagamentoRepository;

    @Override
    @Transactional
    public PagamentoResponseDTO create(PagamentoDTO pagamento) {
        Pagamento novoPagamento = new Pagamento();
        novoPagamento.setMetodo(MetodoPagamento.valueOf(pagamento.idMetodo()));
        novoPagamento.setNumeroCartao(pagamento.numeroCartao());
        novoPagamento.setDataPagamento(LocalDateTime.now());
        novoPagamento.setCodigoTransacao(null);
        novoPagamento.setStatus(StatusPagamento.PENDENTE);

        pagamentoRepository.persist(novoPagamento);

        return PagamentoResponseDTO.valueOf(novoPagamento);
    }

    @Override
    @Transactional
    public PagamentoResponseDTO update(PagamentoDTO dto, Long id) {
        Pagamento pagamentoEditado = pagamentoRepository.findById(id);
        pagamentoEditado.setMetodo(MetodoPagamento.valueOf(dto.idMetodo()));
        pagamentoEditado.setNumeroCartao(dto.numeroCartao());
        pagamentoEditado.setDataPagamento(LocalDateTime.now());
        pagamentoEditado.setCodigoTransacao(null);
        pagamentoEditado.setStatus(StatusPagamento.PENDENTE);

        pagamentoRepository.persist(pagamentoEditado);

        return PagamentoResponseDTO.valueOf(pagamentoEditado);
    }

    @Override
    @Transactional
    public void delete(long id) {
        pagamentoRepository.deleteById(id);
    }

    @Override
    public PagamentoResponseDTO findById(long id) {
        Pagamento pagamento = pagamentoRepository.findById(id);
        return PagamentoResponseDTO.valueOf(pagamento);
    }

    @Override
    public List<PagamentoResponseDTO> findAll(int page, int size) {
        List<Pagamento> list = pagamentoRepository.findAll().page(page, size).list();
        return list.stream().map(e -> PagamentoResponseDTO.valueOf(e)).collect(Collectors.toList());
    }

    @Override
    public List<PagamentoResponseDTO> findByNumeroCartao(String numeroCartao, int page, int size, String sort) {
        String query = "UPPER(numeroCartao) LIKE UPPER(:numeroCartao)";
        Map<String, Object> params = new HashMap<>();
        params.put("numeroCartao", "%" + numeroCartao + "%");

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "numeroCartao":
                    query += " order by numeroCartao";
                    break;
                case "numeroCartao desc":
                    query += " order by numeroCartao desc";
                    break;
                default:
                    query += " order by id";
            }
        } else {
            query += " order by id";
        }

        PanacheQuery<Pagamento> panacheQuery = pagamentoRepository.find(query, params);

        if (size > 0) {
            panacheQuery = panacheQuery.page(page, size);
        }

        return panacheQuery.list()
                .stream()
                .map(pagamento -> PagamentoResponseDTO.valueOf(pagamento))
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return pagamentoRepository.findAll().count();
    }

    @Override
    public long count(String numeroCartao) {
        return pagamentoRepository.count(numeroCartao);
    }

}
