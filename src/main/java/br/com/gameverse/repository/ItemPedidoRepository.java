package br.com.gameverse.repository;

import java.util.List;

import br.com.gameverse.model.Cliente;
import br.com.gameverse.model.ItemPedido;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ItemPedidoRepository implements PanacheRepository<ItemPedido> {
    public PanacheQuery<ItemPedido> findByNome(String nome) {
        return find("UPPER(nome) LIKE UPPER(?1);", "%" + nome + "%");
    }

    public long countByNome(String nome) {
        return count("UPPER(nome) LIKE UPPER(?1)", "%" + nome + "%");
    }

    public List<ItemPedido> findItensByUsuario(Cliente cliente) {
        return find("select i from Pedido p join p.itens i where p.cliente = ?1", cliente).list();
    }
}
