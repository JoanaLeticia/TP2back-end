package br.com.gameverse.service;

import java.util.List;

import br.com.gameverse.dto.ItemPedidoResponseDTO;
import br.com.gameverse.dto.PedidoDTO;
import br.com.gameverse.dto.PedidoResponseDTO;
import br.com.gameverse.model.Cliente;

public interface PedidoService {
    PedidoResponseDTO create(PedidoDTO pedido, String email);

    void delete(long id);

    PedidoResponseDTO findById(long id);

    List<PedidoResponseDTO> findAll(int page, int pageSize, String sort);

    List<PedidoResponseDTO> findAll(String email, int page, int pageSize, String sort);

    List<ItemPedidoResponseDTO> findItensByUsuario(Cliente cliente);
    
    public List<PedidoResponseDTO> pedidosUsuarioLogado(Cliente cliente);

    List<PedidoResponseDTO> findAllPedidosByClienteId(Long clienteId);

    long count();
}
