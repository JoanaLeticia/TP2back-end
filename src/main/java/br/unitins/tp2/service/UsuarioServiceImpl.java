package br.unitins.tp2.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.unitins.tp2.dto.UsuarioDTO;
import br.unitins.tp2.dto.UsuarioResponseDTO;
import br.unitins.tp2.model.Perfil;
import br.unitins.tp2.model.Usuario;
import br.unitins.tp2.repository.UsuarioRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UsuarioResponseDTO create(UsuarioDTO usuario) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuario.nome());
        novoUsuario.setEmail(usuario.email());
        novoUsuario.setSenha(usuario.senha());

        Perfil perfil = Perfil.valueOf((int) usuario.idPerfil());
        novoUsuario.setPerfil(perfil);

        usuarioRepository.persist(novoUsuario);

        return UsuarioResponseDTO.valueOf(novoUsuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(UsuarioDTO dto, Long id) {
        Usuario usuarioEditado = usuarioRepository.findById(id);

        usuarioEditado.setNome(dto.nome());
        usuarioEditado.setEmail(dto.email());
        usuarioEditado.setSenha(dto.senha());
        Perfil perfil = Perfil.valueOf((int) dto.idPerfil());
        usuarioEditado.setPerfil(perfil);

        usuarioRepository.persist(usuarioEditado);

        return UsuarioResponseDTO.valueOf(usuarioEditado);
    }

    @Override
    @Transactional
    public void delete(long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioResponseDTO updateNome(String email, String nome) {
        Usuario usuarioNomeEditado = usuarioRepository.findByEmail(email);
        usuarioNomeEditado.setNome(nome);
        usuarioRepository.persist(usuarioNomeEditado);

        return UsuarioResponseDTO.valueOf(usuarioNomeEditado);   
    }

    @Override
    public UsuarioResponseDTO updateSenha(String email, String senha) {
        Usuario usuarioSenhaEditada = usuarioRepository.findByEmail(email);
        usuarioSenhaEditada.setSenha(senha);
        usuarioRepository.persist(usuarioSenhaEditada);

        return UsuarioResponseDTO.valueOf(usuarioSenhaEditada);  
    }

    @Override
    public UsuarioResponseDTO findById(long id) {
        return UsuarioResponseDTO.valueOf(usuarioRepository.findById(id));
    }

    @Override
    public UsuarioResponseDTO findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        return UsuarioResponseDTO.valueOf(usuario);
    }

    @Override
    public UsuarioResponseDTO findByEmailAndSenha(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmailAndSenha(email, senha);
        return UsuarioResponseDTO.valueOf(usuario);   
    }

    @Override
    public List<UsuarioResponseDTO> findAll(int page, int pageSize, String sort) {
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

        PanacheQuery<Usuario> panacheQuery = usuarioRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(usuario -> UsuarioResponseDTO.valueOf(usuario))
            .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioResponseDTO> findByNome(String nome, int page, int pageSize, String sort) {
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

        PanacheQuery<Usuario> panacheQuery = usuarioRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(usuario -> UsuarioResponseDTO.valueOf(usuario))
            .collect(Collectors.toList());
    }

    public List<Usuario> findByNome(String nome) {
        return usuarioRepository.findByNome(nome).list();
    }

    @Override
    public long count() {
        return usuarioRepository.findAll().count();
    }

    @Override
    public long count(String nome) {
        return usuarioRepository.countByNome(nome);
    }

}
