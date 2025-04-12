package br.unitins.tp2.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.unitins.tp2.dto.ClienteDTO;
import br.unitins.tp2.dto.ClienteResponseDTO;
import br.unitins.tp2.dto.UsuarioResponseDTO;
import br.unitins.tp2.model.Cliente;
import br.unitins.tp2.model.Endereco;
import br.unitins.tp2.model.Municipio;
import br.unitins.tp2.model.Perfil;
import br.unitins.tp2.model.Telefone;
import br.unitins.tp2.repository.ClienteRepository;
import br.unitins.tp2.repository.MunicipioRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ClienteServiceImpl implements ClienteService {
    @Inject
    ClienteRepository clienteRepository;

    @Inject
    MunicipioRepository municipioRepository;

    @Override
    @Transactional
    public ClienteResponseDTO create(ClienteDTO cliente) {
        Cliente novoCliente = new Cliente();
        novoCliente.setNome(cliente.nome());
        novoCliente.setEmail(cliente.email());
        novoCliente.setSenha(cliente.senha());
        novoCliente.setPerfil(Perfil.CLIENTE);
        novoCliente.setCpf(cliente.cpf());
        novoCliente.setDataNascimento(cliente.dataNascimento());

        if (cliente.listaTelefone() != null && !cliente.listaTelefone().isEmpty()) {
            List<Telefone> telefones = cliente.listaTelefone().stream()
                    .map(tel -> {
                        Telefone telefone = new Telefone();
                        telefone.setCodArea(tel.codArea());
                        telefone.setNumero(tel.numero());
                        return telefone;
                    })
                    .collect(Collectors.toList());
            novoCliente.setTelefones(telefones);
        } else {
            novoCliente.setTelefones(Collections.emptyList());
        }

        //  Configure a lista de endereços
        if (cliente.listaEndereco() != null && !cliente.listaEndereco().isEmpty()) {
            List<Endereco> enderecos = cliente.listaEndereco().stream()
                    .map(end -> {
                        Endereco endereco = new Endereco();
                        endereco.setCep(end.cep());
                        endereco.setBairro(end.bairro());
                        endereco.setNumero(end.numero());
                        endereco.setLogradouro(end.logradouro());
                        endereco.setComplemento(end.complemento());

                        Municipio idMunicipio = municipioRepository.findById(end.idMunicipio());
                        endereco.setMunicipio(idMunicipio);

                        return endereco;
                    })
                    .collect(Collectors.toList());
            novoCliente.setEndereco(enderecos);
            novoCliente.setPerfil(Perfil.CLIENTE);
        } else {
            novoCliente.setEndereco(Collections.emptyList());
        } 

        clienteRepository.persist(novoCliente);

        return ClienteResponseDTO.valueOf(novoCliente);
    }

    @Override
    @Transactional
    public ClienteResponseDTO update(ClienteDTO clienteDTO, Long id) {
        Cliente clienteEditado = clienteRepository.findById(id);

        clienteEditado.setNome(clienteDTO.nome());
        clienteEditado.setEmail(clienteDTO.email());
        clienteEditado.setSenha(clienteDTO.senha());
        clienteEditado.setPerfil(Perfil.CLIENTE);
        clienteEditado.setCpf(clienteDTO.cpf());
        clienteEditado.setDataNascimento(clienteDTO.dataNascimento());
        
        if (clienteDTO.listaTelefone() != null && !clienteDTO.listaTelefone().isEmpty()) {
            clienteEditado.getTelefones().clear();
            List<Telefone> telefones = clienteDTO.listaTelefone().stream()
                    .map(tel -> {
                        Telefone telefone = new Telefone();
                        telefone.setCodArea(tel.codArea());
                        telefone.setNumero(tel.numero());
                        return telefone;
                    })
                    .collect(Collectors.toList());
                    clienteEditado.getTelefones().addAll(telefones);
        } else {
            clienteEditado.getTelefones().clear();
        }

        // Atualize a lista de endereços
        if (clienteDTO.listaEndereco() != null && !clienteDTO.listaEndereco().isEmpty()) {
            clienteEditado.getEndereco().clear();
            List<Endereco> enderecos = clienteDTO.listaEndereco().stream()
                    .map(end -> {
                        Endereco endereco = new Endereco();
                        endereco.setCep(end.cep());
                        endereco.setBairro(end.bairro());
                        endereco.setNumero(end.numero());
                        endereco.setLogradouro(end.logradouro());
                        endereco.setComplemento(end.complemento());
                        Municipio idMunicipio = municipioRepository.findById(end.idMunicipio());
                        endereco.setMunicipio(idMunicipio);

                        return endereco;
                    })
                    .collect(Collectors.toList());
                    clienteEditado.getEndereco().addAll(enderecos);
        } else {
            clienteEditado.getEndereco().clear();
        } 

        return ClienteResponseDTO.valueOf(clienteEditado);
    }

    @Override
    @Transactional
    public void delete(long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public ClienteResponseDTO findById(long id) {
        Cliente cliente = clienteRepository.findById(id);
        return ClienteResponseDTO.valueOf(cliente);
    }

    @Override
    public List<ClienteResponseDTO> findAll(int page, int pageSize, String sort) {
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

        PanacheQuery<Cliente> panacheQuery = clienteRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(cliente -> ClienteResponseDTO.valueOf(cliente))
            .collect(Collectors.toList());
    }

    @Override
    public List<ClienteResponseDTO> findByNome(String nome, int page, int pageSize, String sort) {
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

        PanacheQuery<Cliente> panacheQuery = clienteRepository.find(query, params);

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(page, pageSize);
        }

        return panacheQuery.list()
            .stream()
            .map(cliente -> ClienteResponseDTO.valueOf(cliente))
            .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return clienteRepository.findAll().count();
    }

    @Override
    public long count(String nome) {
        return clienteRepository.countByNome(nome);
    }

    public UsuarioResponseDTO login(String email, String senha) {
        
        Cliente cliente = clienteRepository.findByEmailAndSenha(email, senha);

        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado");
        }
    
        return UsuarioResponseDTO.valueOf(cliente);
    }

    @Override
    public ClienteResponseDTO findByEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email);
        return ClienteResponseDTO.valueOf(cliente);
    }

}
