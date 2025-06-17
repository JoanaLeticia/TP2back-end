package br.com.gameverse.repository;

import br.com.gameverse.model.Pagamento;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PagamentoRepository implements PanacheRepository<Pagamento>{
    public PanacheQuery<Pagamento> findByNumeroCartao(String numeroCartao) {
        return find("UPPER(numeroCartao) LIKE UPPER(?1)", "%" + numeroCartao + "%");
    }
}
