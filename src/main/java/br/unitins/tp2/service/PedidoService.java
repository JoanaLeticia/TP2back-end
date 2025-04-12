package br.unitins.tp2.service;

import java.util.List;

import br.unitins.tp2.dto.ItemPedidoResponseDTO;
import br.unitins.tp2.dto.PedidoDTO;
import br.unitins.tp2.dto.PedidoResponseDTO;
import br.unitins.tp2.model.Cliente;

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
