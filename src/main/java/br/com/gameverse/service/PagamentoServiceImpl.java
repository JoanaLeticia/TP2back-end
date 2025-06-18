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
import br.com.gameverse.model.Pedido;
import br.com.gameverse.model.StatusPagamento;
import br.com.gameverse.model.StatusPedido;
import br.com.gameverse.repository.PagamentoRepository;
import br.com.gameverse.repository.PedidoRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PagamentoServiceImpl implements PagamentoService {
    @Inject
    PagamentoRepository pagamentoRepository;

    @Inject
    PedidoRepository pedidoRepository;

    @Inject
    PedidoService pedidoService;

    @Override
    @Transactional
    public PagamentoResponseDTO create(PagamentoDTO pagamentoDTO) {
        Pedido pedido = pedidoRepository.findById(pagamentoDTO.pedidoId());
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado.");
        }

        Pagamento novoPagamento = new Pagamento();
        MetodoPagamento metodo = MetodoPagamento.valueOf(pagamentoDTO.idMetodo());
        novoPagamento.setMetodo(metodo);

        // Limpa e valida o número do cartão se for método de cartão
        if (metodo == MetodoPagamento.CARTAO_CREDITO || metodo == MetodoPagamento.CARTAO_DEBITO) {
            if (pagamentoDTO.numeroCartao() == null) {
                throw new IllegalArgumentException("Número do cartão é obrigatório para pagamento com cartão");
            }

            String numeroLimpo = pagamentoDTO.numeroCartao().replaceAll("[^0-9]", "");

            if (numeroLimpo.length() < 13 || numeroLimpo.length() > 19) {
                throw new IllegalArgumentException("Número do cartão deve ter entre 13 e 19 dígitos");
            }

            novoPagamento.setNumeroCartao(numeroLimpo);
        } else {
            novoPagamento.setNumeroCartao(null);
        }

        novoPagamento.setDataPagamento(LocalDateTime.now());
        novoPagamento.setCodigoTransacao(null);
        novoPagamento.setStatus(StatusPagamento.PENDENTE);

        novoPagamento.setPedido(pedido);
        pedido.setPagamento(novoPagamento);

        pagamentoRepository.persist(novoPagamento);
        pedidoRepository.persist(pedido);

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

    @Transactional
    @Override
    public void aprovarPagamento(Long pagamentoId) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId);
        if (pagamento == null) {
            throw new EntityNotFoundException("Pagamento não encontrado");
        }

        // Atualiza status do pagamento
        pagamento.setStatus(StatusPagamento.APROVADO);
        pagamento.setCodigoTransacao(gerarCodigoTransacao());
        pagamentoRepository.persist(pagamento);

        // Atualiza status do pedido vinculado
        if (pagamento.getPedido() != null) {
            pedidoService.atualizarStatusPedido(
                    pagamento.getPedido().getId(),
                    StatusPedido.PAGO);
        }
    }

    private String gerarCodigoTransacao() {
        return "TXN-" + System.currentTimeMillis();
    }

    @Override
    public PagamentoResponseDTO findByPedidoId(Long pedidoId) {
        if (pedidoId == null) {
            throw new IllegalArgumentException("ID do pedido não pode ser nulo");
        }

        Pagamento pagamento = pagamentoRepository.findByPedidoId(pedidoId);

        if (pagamento == null) {
            throw new EntityNotFoundException("Pagamento não encontrado para o pedido: " + pedidoId);
        }
        return PagamentoResponseDTO.valueOf(pagamento);
    }
}
