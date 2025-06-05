package br.com.gameverse.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.gameverse.dto.TelefoneDTO;
import br.com.gameverse.dto.TelefoneResponseDTO;
import br.com.gameverse.model.Cliente;
import br.com.gameverse.model.Telefone;
import br.com.gameverse.repository.ClienteRepository;
import br.com.gameverse.repository.TelefoneRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class TelefoneServiceImpl implements TelefoneService {
    @Inject
    TelefoneRepository telefoneRepository;

    @Inject
    ClienteRepository clienteRepository;

    @Override
    @Transactional
    public TelefoneResponseDTO create(TelefoneDTO telefone) {
        Telefone novoTelefone = new Telefone();
        novoTelefone.setCodArea(telefone.codArea());
        novoTelefone.setNumero(telefone.numero());

        telefoneRepository.persist(novoTelefone);

        return TelefoneResponseDTO.valueOf(novoTelefone);
    }

    @Override
    @Transactional
    public TelefoneResponseDTO update(TelefoneDTO dto, Long id) {
        Telefone telefoneEditado = telefoneRepository.findById(id);

        telefoneEditado.setCodArea(dto.codArea());
        telefoneEditado.setNumero(dto.numero());

        return TelefoneResponseDTO.valueOf(telefoneEditado);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Telefone telefone = telefoneRepository.findById(id);

        if (telefone == null) {
            throw new NotFoundException();
        }

        List<Cliente> clientes = clienteRepository.findByTelefoneId(id);
        for (Cliente cliente : clientes) {
            cliente.getTelefones().remove(telefone);
        }

        telefoneRepository.delete(telefone);
    }

    @Override
    public TelefoneResponseDTO findById(long id) {
        Telefone telefone = telefoneRepository.findById(id);
        return TelefoneResponseDTO.valueOf(telefone);
    }

    @Override
    public List<TelefoneResponseDTO> findAll(int page, int pageSize, String sort) {
        String query = "";
        Map<String, Object> params = new HashMap<>();

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "numero":
                    query = "order by numero";
                    break;
                case "numero desc":
                    query = "order by numero desc";
                    break;
                default:
                    query = "order by id";
            }
        } else {
            query = "order by id";
        }

        PanacheQuery<Telefone> panacheQuery = telefoneRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(telefone -> TelefoneResponseDTO.valueOf(telefone))
            .collect(Collectors.toList());
    }

    @Override
    public List<TelefoneResponseDTO> findByNumero(String numero, int page, int pageSize, String sort) {
        String query = "UPPER(numero) LIKE UPPER(:numero)";
        Map<String, Object> params = new HashMap<>();
        params.put("numero", "%" + numero + "%");

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "numero":
                    query += " order by numero";
                    break;
                case "numero desc":
                    query += " order by numero desc";
                    break;
                default:
                    query += " order by id";
            }
        } else {
            query += " order by id";
        }

        PanacheQuery<Telefone> panacheQuery = telefoneRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(telefone -> TelefoneResponseDTO.valueOf(telefone))
            .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return telefoneRepository.findAll().count();
    }

    @Override
    public long count(String numero) {
        return telefoneRepository.countByNumero(numero);
    }

}
