package br.unitins.tp2.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.unitins.tp2.dto.AdministradorDTO;
import br.unitins.tp2.dto.AdministradorResponseDTO;
import br.unitins.tp2.dto.UsuarioResponseDTO;
import br.unitins.tp2.model.Administrador;
import br.unitins.tp2.model.Perfil;
import br.unitins.tp2.repository.AdministradorRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AdministradorServiceImpl implements AdministradorService {
    @Inject
    AdministradorRepository administradorRepository;

    @Override
    @Transactional
    public AdministradorResponseDTO create(AdministradorDTO administrador) {
        Administrador novoAdmin = new Administrador();
        novoAdmin.setNome(administrador.nome());
        novoAdmin.setEmail(administrador.email());
        novoAdmin.setSenha(administrador.senha());
        novoAdmin.setPerfil(Perfil.ADMIN);
        novoAdmin.setCpf(administrador.cpf());
        novoAdmin.setDataNascimento(administrador.dataNascimento());

        administradorRepository.persist(novoAdmin);

        return AdministradorResponseDTO.valueOf(novoAdmin);
    }

    @Override
    @Transactional
    public AdministradorResponseDTO update(AdministradorDTO administradorDTO, Long id) {
        Administrador adminEditado = administradorRepository.findById(id);

        adminEditado.setNome(administradorDTO.nome());
        adminEditado.setEmail(administradorDTO.email());
        adminEditado.setSenha(administradorDTO.senha());
        adminEditado.setCpf(administradorDTO.cpf());
        adminEditado.setPerfil(Perfil.ADMIN);
        adminEditado.setDataNascimento(administradorDTO.dataNascimento());

        return AdministradorResponseDTO.valueOf(adminEditado);
    }

    @Override
    @Transactional
    public void delete(long id) {
        administradorRepository.deleteById(id);
    }

    @Override
    public AdministradorResponseDTO findById(long id) {
        Administrador administrador = administradorRepository.findById(id);

        return AdministradorResponseDTO.valueOf(administrador);
    }

    @Override
    public List<AdministradorResponseDTO> findAll(int page, int pageSize, String sort) {
        String query = "";
        Map<String, Object> params = new HashMap<>();

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "nome":
                    query = "order by nome";
                    break;
                case "nome desc":
                    query = "order by nome desc";
                    break;
                default:
                    query = "order by id";
            }
        } else {
            query = "order by id";
        }

        PanacheQuery<Administrador> panacheQuery = administradorRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(administrador -> AdministradorResponseDTO.valueOf(administrador))
            .collect(Collectors.toList());
    }

    @Override
    public List<AdministradorResponseDTO> findByNome(String nome, int page, int pageSize, String sort) {
        String query = "UPPER(nome) LIKE UPPER(:nome)";
        Map<String, Object> params = new HashMap<>();
        params.put("nome", "%" + nome + "%");

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "nome":
                    query += " order by nome";
                    break;
                case "nome desc":
                    query += " order by nome desc";
                    break;
                default:
                    query += " order by id";
            }
        } else {
            query += " order by id";
        }

        PanacheQuery<Administrador> panacheQuery = administradorRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(administrador -> AdministradorResponseDTO.valueOf(administrador))
            .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return administradorRepository.findAll().count();
    }

    @Override
    public long count(String nome) {
        return administradorRepository.countByNome(nome);
    }

    public UsuarioResponseDTO login(String email, String senha) {
        
        Administrador adm = administradorRepository.findByEmailAndSenha(email, senha);
    
        if (adm == null) {
            throw new RuntimeException("Administrador não encontrado");
        }
    
        return UsuarioResponseDTO.valueOf(adm);
    }

}
