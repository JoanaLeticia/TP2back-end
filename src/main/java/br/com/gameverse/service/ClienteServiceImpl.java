package br.com.gameverse.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.com.gameverse.dto.ClienteDTO;
import br.com.gameverse.dto.ClienteResponseDTO;
import br.com.gameverse.dto.ClienteUpdateDTO;
import br.com.gameverse.dto.TelefoneDTO;
import br.com.gameverse.dto.UsuarioResponseDTO;
import br.com.gameverse.model.Cliente;
import br.com.gameverse.model.Endereco;
import br.com.gameverse.model.Municipio;
import br.com.gameverse.model.Perfil;
import br.com.gameverse.model.Telefone;
import br.com.gameverse.repository.ClienteRepository;
import br.com.gameverse.repository.MunicipioRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ClienteServiceImpl implements ClienteService {
    @Inject
    ClienteRepository clienteRepository;

    @Inject
    MunicipioRepository municipioRepository;

    @Inject
    HashService hashService;

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

        // Configure a lista de endereços
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
            novoCliente.setEnderecos(enderecos);
            novoCliente.setPerfil(Perfil.CLIENTE);
        } else {
            novoCliente.setEnderecos(Collections.emptyList());
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
            clienteEditado.getEnderecos().clear();
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
            clienteEditado.getEnderecos().addAll(enderecos);
        } else {
            clienteEditado.getEnderecos().clear();
        }

        return ClienteResponseDTO.valueOf(clienteEditado);
    }

    @Override
    @Transactional
    public void updatePartial(ClienteUpdateDTO dto, Long id) {
        Cliente cliente = clienteRepository.findById(id);
        if (cliente == null) {
            throw new EntityNotFoundException("Cliente não encontrado");
        }

        System.out.println("Telefones recebidos no DTO: " + dto.listaTelefone());
        System.out.println("Telefones atuais no cliente: " + cliente.getTelefones());

        if (dto.nome() != null) {
            cliente.setNome(dto.nome());
        }
        if (dto.dataNascimento() != null) {
            cliente.setDataNascimento(dto.dataNascimento());
        }
        if (dto.cpf() != null) {
            cliente.setCpf(dto.cpf());
        }

        if (dto.listaTelefone() != null) {
            Map<Long, Telefone> telefonesExistentes = cliente.getTelefones().stream()
                    .collect(Collectors.toMap(Telefone::getId, Function.identity()));

            cliente.getTelefones().clear();

            for (TelefoneDTO telDTO : dto.listaTelefone()) {
                Telefone telefone;

                if (telDTO.id() != null && telefonesExistentes.containsKey(telDTO.id())) {
                    // Atualiza existente
                    telefone = telefonesExistentes.get(telDTO.id());
                    telefone.setCodArea(telDTO.codArea());
                    telefone.setNumero(telDTO.numero());
                } else {
                    // Cria novo
                    telefone = new Telefone();
                    telefone.setCodArea(telDTO.codArea());
                    telefone.setNumero(telDTO.numero());
                    telefone.setCliente(cliente);
                }
                cliente.getTelefones().add(telefone);
            }
        }
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
    public List<ClienteResponseDTO> findAll(int page, int pageSize) {
        List<Cliente> list = clienteRepository.findAll().page(page, pageSize).list();
        return list.stream().map(e -> ClienteResponseDTO.valueOf(e)).collect(Collectors.toList());
    }

    @Override
    public List<ClienteResponseDTO> findByNome(String nome, int page, int pageSize, String sort) {
        List<String> allowedSortFields = List.of("id", "nome");

        String orderByClause = "order by id"; // padrão

        if (sort != null && !sort.isBlank()) {
            String[] sortParts = sort.trim().split(" ");
            String field = sortParts[0];
            String direction = (sortParts.length > 1) ? sortParts[1].toLowerCase() : "asc";

            if (allowedSortFields.contains(field)) {
                if (direction.equals("desc") || direction.equals("asc")) {
                    orderByClause = String.format("order by %s %s", field, direction);
                } else {
                    orderByClause = String.format("order by %s", field);
                }
            }
        }

        String query = "lower(nome) like lower(:nome) " + orderByClause;

        PanacheQuery<Cliente> panacheQuery = clienteRepository
                .find(query, Parameters.with("nome", "%" + nome + "%"));

        if (pageSize > 0) {
            panacheQuery = panacheQuery.page(Page.of(page, pageSize));
        }

        return panacheQuery.list().stream()
                .map(ClienteResponseDTO::valueOf)
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
    @Transactional
    public UsuarioResponseDTO registrar(ClienteDTO clienteDTO) {
        if (clienteRepository.existePorEmail(clienteDTO.email())) {
            throw new RuntimeException("Email já cadastrado!");
        }

        Cliente novoCliente = new Cliente();
        novoCliente.setNome(clienteDTO.nome());
        novoCliente.setEmail(clienteDTO.email());
        novoCliente.setSenha(hashService.getHashSenha(clienteDTO.senha()));
        novoCliente.setPerfil(Perfil.CLIENTE);
        novoCliente.setCpf(clienteDTO.cpf());
        novoCliente.setDataNascimento(clienteDTO.dataNascimento());

        novoCliente.setTelefones(null);
        novoCliente.setEnderecos(null);

        clienteRepository.persist(novoCliente);

        return new UsuarioResponseDTO(
                novoCliente.getId(),
                novoCliente.getNome(),
                novoCliente.getEmail(),
                novoCliente.getPerfil());
    }

    @Override
    public ClienteResponseDTO findByEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            throw new EntityNotFoundException("Cliente não encontrado");
        }
        return ClienteResponseDTO.valueOf(cliente);
    }

}
