package br.com.gameverse.repository;

import java.util.List;

import br.com.gameverse.model.Telefone;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TelefoneRepository implements PanacheRepository<Telefone> {
    public List<Telefone> findByNumero(String numero) {
        return find("UPPER(numero) LIKE UPPER(?1) ", "%" + numero + "%").list();
    }

    public long countByNumero(String numero) {
        return count("UPPER(numero) LIKE UPPER(?1)", "%" + numero + "%");
    }
}
