package br.unitins.tp2.repository;

import java.util.List;

import br.unitins.tp2.model.Cliente;
import br.unitins.tp2.model.Pedido;
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
}
