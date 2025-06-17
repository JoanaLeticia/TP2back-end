package br.com.gameverse.repository;

import java.util.List;

import br.com.gameverse.model.Plataforma;
import br.com.gameverse.model.Produto;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {
    public PanacheQuery<Produto> findByNome(String nome) {
        return find("UPPER(nome) LIKE UPPER(?1)", "%" + nome + "%");
    }

    public long countByNome(String nome) {
        return count("UPPER(nome) LIKE UPPER(?1)", "%" + nome + "%");
    }

    public long countByPlataforma(Plataforma plataforma) {
        return count("plataforma = ?1", plataforma);
    }

    public List<Produto> findByPlataforma(Plataforma plataforma) {
        return find("plataforma = ?1", plataforma).list();
    }

}
