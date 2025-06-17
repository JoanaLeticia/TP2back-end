package br.com.gameverse.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.gameverse.dto.ItemPedidoDTO;
import br.com.gameverse.dto.ItemPedidoResponseDTO;
import br.com.gameverse.dto.PedidoDTO;
import br.com.gameverse.dto.PedidoResponseDTO;
import br.com.gameverse.model.Cliente;
import br.com.gameverse.model.Endereco;
import br.com.gameverse.model.ItemPedido;
import br.com.gameverse.model.MetodoPagamento;
import br.com.gameverse.model.Pagamento;
import br.com.gameverse.model.Pedido;
import br.com.gameverse.model.Produto;
import br.com.gameverse.model.StatusPagamento;
import br.com.gameverse.model.StatusPedido;
import br.com.gameverse.repository.ClienteRepository;
import br.com.gameverse.repository.EnderecoRepository;
import br.com.gameverse.repository.PedidoRepository;
import br.com.gameverse.repository.ProdutoRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PedidoServiceImpl implements PedidoService {
    @Inject
    PedidoRepository pedidoRepository;

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    ProdutoRepository produtoRepository;

    @Override
    @Transactional
    public PedidoResponseDTO create(PedidoDTO pedido, String email) {
        Pedido novoPedido = new Pedido();
        novoPedido.setDataHora(LocalDateTime.now());

        Double total = 0.0;

        // Processando itens
        List<ItemPedido> itensPedido = new ArrayList<>();
        for (ItemPedidoDTO itemDTO : pedido.itens()) {
            Produto produto = produtoRepository.findById(itemDTO.idProduto());
            if (produto == null) {
                throw new IllegalArgumentException("Não existe esse produto!");
            }

            ItemPedido item = new ItemPedido();
            item.setValor(produto.getPreco());
            item.setQuantidade(itemDTO.quantidade());
            item.setPedido(novoPedido);
            item.setProduto(produto);

            produto.setEstoque(produto.getEstoque() - item.getQuantidade());

            itensPedido.add(item);

            total += (produto.getPreco() * itemDTO.quantidade());
        }

        novoPedido.setValorTotal(total);
        novoPedido.setItens(itensPedido);

        // Setando cliente
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            novoPedido.setCliente(null);
        }
        novoPedido.setCliente(cliente);

        Endereco endereco = enderecoRepository.findById(pedido.enderecoId());
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço Válido!");
        }
        novoPedido.setEndereco(endereco);

        Pagamento pagamento = new Pagamento();
        pagamento.setMetodo(MetodoPagamento.valueOf(pedido.pagamento().idMetodo()));
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setDataPagamento(LocalDateTime.now());
        novoPedido.setPagamento(pagamento);

        novoPedido.setStatus(StatusPedido.AGUARDANDO);

        pedidoRepository.persist(novoPedido);

        return PedidoResponseDTO.valueOf(novoPedido);
    }

    @Override
    @Transactional
    public void delete(long id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    public PedidoResponseDTO findById(long id) {
        return PedidoResponseDTO.valueOf(pedidoRepository.findById(id));
    }

    @Override
    public List<PedidoResponseDTO> findAll(int page, int pageSize, String sort) {
        String query = "";
        Map<String, Object> params = new HashMap<>();

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "dataHora":
                    query += "order by dataHora";
                    break;
                case "dataHora desc":
                    query += "dataHora desc";
                    break;
                case "valorTotal":
                    query += "valorTotal";
                    break;
                case "valorTotal desc":
                    query += "valorTotal desc";
                    break;
                default:
                    query += "order by id";
            }
        } else {
            query = "order by id";
        }

        PanacheQuery<Pedido> panacheQuery = pedidoRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(pedido -> PedidoResponseDTO.valueOf(pedido))
            .collect(Collectors.toList());
    }

    

    @Override
    public long count() {
        return pedidoRepository.findAll().count();
    }

    @Override
    public List<PedidoResponseDTO> findAll(String email, int page, int pageSize, String sort) {
        return pedidoRepository.listAll().stream()
                .map(e -> PedidoResponseDTO.valueOf(e)).toList();
    }

    @Override
    public List<ItemPedidoResponseDTO> findItensByUsuario(Cliente cliente) {
        List<Pedido> pedidos = pedidoRepository.findByUsuario(cliente);
        List<ItemPedido> itens = new ArrayList<>();
    
        for (Pedido pedido : pedidos) {
            itens.addAll(pedido.getItens());
        }
    
        return itens.stream()
                .map(i -> ItemPedidoResponseDTO.valueOf(i))
                .collect(Collectors.toList());
    }

    @Override
    public List<PedidoResponseDTO> findAllPedidosByClienteId(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId);
        List<Pedido> pedidos = pedidoRepository.findByUsuario(cliente);
        
        return pedidos.stream().map(PedidoResponseDTO::valueOf).collect(Collectors.toList());
    }
    
    @Override
    public List<PedidoResponseDTO> pedidosUsuarioLogado(Cliente cliente) {
        Cliente usuario = clienteRepository.findByEmail(cliente.getEmail());
        List<Pedido> pedidos = pedidoRepository.findByUsuario(usuario);
        return pedidos.stream().map(p -> PedidoResponseDTO.valueOf(p)).collect(Collectors.toList());
    }
}
