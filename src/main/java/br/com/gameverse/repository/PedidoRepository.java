package br.com.gameverse.repository;

import java.util.List;

import br.com.gameverse.model.Cliente;
import br.com.gameverse.model.Pedido;
import br.com.gameverse.model.StatusPedido;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidoRepository implements PanacheRepository<Pedido> {
    public List<Pedido> findAll(String email) {
        return find("cliente.email = ?1", email).list();
    }

    public List<Pedido> findAll(Long idUsuario) {
        return find("cliente.id = ?1", idUsuario).list();
    }

    public List<Pedido> findByUsuario(Cliente cliente) {
        return find("cliente = ?1", cliente).list();
    }

    public Pedido obterUltimoPedidoDoCliente(Long idCliente) {
        return find("cliente.id = ?1 order by id desc", idCliente)
                .firstResultOptional()
                .orElse(null);
    }

    public boolean existsByEnderecoIdAndStatusIn(Long enderecoId, List<StatusPedido> statusList) {
        return count("endereco.id = ?1 AND status IN ?2", enderecoId, statusList) > 0;
    }

    public List<Pedido> findByEnderecoId(Long enderecoId) {
        return find("SELECT p FROM Pedido p JOIN p.endereco e WHERE e.id = ?1", enderecoId).list();
    }

}
