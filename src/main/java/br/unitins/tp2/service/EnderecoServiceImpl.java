package br.unitins.tp2.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.unitins.tp2.dto.EnderecoDTO;
import br.unitins.tp2.dto.EnderecoResponseDTO;
import br.unitins.tp2.model.Endereco;
import br.unitins.tp2.repository.EnderecoRepository;
import br.unitins.tp2.repository.MunicipioRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class EnderecoServiceImpl implements EnderecoService {
    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    MunicipioRepository municipioRepository;

    @Override
    @Transactional
    public EnderecoResponseDTO create(EnderecoDTO endereco) {
        Endereco novoEndereco = new Endereco();
        novoEndereco.setLogradouro(endereco.logradouro());
        novoEndereco.setNumero(endereco.numero());
        novoEndereco.setComplemento(endereco.complemento());
        novoEndereco.setBairro(endereco.bairro());
        novoEndereco.setCep(endereco.cep());

        novoEndereco.setMunicipio(municipioRepository.findById(endereco.idMunicipio()));

        enderecoRepository.persist(novoEndereco);

        return EnderecoResponseDTO.valueOf(novoEndereco);
    }

    @Override
    @Transactional
    public EnderecoResponseDTO update(EnderecoDTO dto, Long id) {
        Endereco enderecoEditado = enderecoRepository.findById(id);
        if (enderecoEditado == null) {
            throw new IllegalArgumentException("Endereço com ID " + id + " não encontrado.");
        }

        enderecoEditado.setLogradouro(dto.logradouro());
        enderecoEditado.setNumero(dto.numero());
        enderecoEditado.setComplemento(dto.complemento());
        enderecoEditado.setBairro(dto.bairro());
        enderecoEditado.setCep(dto.cep());
        enderecoEditado.setMunicipio(municipioRepository.findById(dto.idMunicipio()));

        enderecoRepository.persist(enderecoEditado);
        return EnderecoResponseDTO.valueOf(enderecoEditado);

    }

    @Override
    @Transactional
    public void delete(long id) {
        enderecoRepository.deleteById(id);
    }

    @Override
    public EnderecoResponseDTO findById(long id) {
        Endereco end = enderecoRepository.findById(id);
        return EnderecoResponseDTO.valueOf(end);
    }

    @Override
    public List<EnderecoResponseDTO> findByMunicipio(Long idMunicipio, int page, int pageSize, String sort) {
        String query = "municipio.id = :idMunicipio";
        Map<String, Object> params = new HashMap<>();
        params.put("idMunicipio", idMunicipio);

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "logradouro":
                    query += " order by logradouro";
                    break;
                case "logradouro desc":
                    query += " order by logradouro desc";
                    break;
                default:
                    query += " order by id";
            }
        } else {
            query += " order by id";
        }

        PanacheQuery<Endereco> panacheQuery = enderecoRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(endereco -> EnderecoResponseDTO.valueOf(endereco))
            .collect(Collectors.toList());
    }

    @Override
    public List<EnderecoResponseDTO> findAll(int page, int pageSize, String sort) {
        String query = "";
        Map<String, Object> params = new HashMap<>();

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "logradouro":
                    query = "order by logradouro";
                    break;
                case "logradouro desc":
                    query = "order by logradouro desc";
                    break;
                default:
                    query = "order by id";
            }
        } else {
            query = "order by id";
        }

        PanacheQuery<Endereco> panacheQuery = enderecoRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(endereco -> EnderecoResponseDTO.valueOf(endereco))
            .collect(Collectors.toList());
    }

    @Override
    public List<EnderecoResponseDTO> findByLogradouro(String logradouro, int page, int pageSize, String sort) {
        String query = "UPPER(logradouro) LIKE UPPER(:logradouro)";
        Map<String, Object> params = new HashMap<>();
        params.put("logradouro", "%" + logradouro + "%");

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "logradouro":
                    query += " order by logradouro";
                    break;
                case "logradouro desc":
                    query += " order by logradouro desc";
                    break;
                default:
                    query += " order by id";
            }
        } else {
            query += " order by id";
        }

        PanacheQuery<Endereco> panacheQuery = enderecoRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(endereco -> EnderecoResponseDTO.valueOf(endereco))
            .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return enderecoRepository.findAll().count();
    }

    @Override
    public long count(String logradouro) {
        return enderecoRepository.countByLogradouro(logradouro);
    }

    @Override
    public List<EnderecoResponseDTO> findByBairro(String bairro, int page, int pageSize, String sort) {
        String query = "UPPER(bairro) LIKE UPPER(:bairro)";
        Map<String, Object> params = new HashMap<>();
        params.put("bairro", "%" + bairro + "%");

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "bairro":
                    query += " order by bairro";
                    break;
                case "bairro desc":
                    query += " order by bairro desc";
                    break;
                default:
                    query += " order by id";
            }
        } else {
            query += " order by id";
        }

        PanacheQuery<Endereco> panacheQuery = enderecoRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(endereco -> EnderecoResponseDTO.valueOf(endereco))
            .collect(Collectors.toList());
    }

    @Override
    public long countByBairro(String bairro) {
        return enderecoRepository.countByBairro(bairro);
    }
}
