package br.com.gameverse.repository;

import java.util.List;
import java.util.stream.Collectors;

import br.com.gameverse.model.Genero;
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

    public List<Genero> findGenerosByPlataforma(Plataforma plataforma) {
        return list("plataforma", plataforma)
                .stream()
                .map(Produto::getGenero)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> findDesenvolvedorasByPlataforma(Plataforma plataforma) {
        return list("plataforma", plataforma)
                .stream()
                .map(Produto::getDesenvolvedora)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Double> findProdutosPrecoByPlataforma(Plataforma plataforma) {
        return List.of(50.0, 100.0, 150.0, 200.0, 300.0);
    }
}
