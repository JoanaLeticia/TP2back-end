package br.com.gameverse.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import br.com.gameverse.dto.ItemPedidoResponseDTO;
import br.com.gameverse.dto.PedidoResponseDTO;
import br.com.gameverse.model.Cliente;
import br.com.gameverse.repository.ClienteRepository;
import br.com.gameverse.repository.UsuarioRepository;
import br.com.gameverse.service.ClienteService;
import br.com.gameverse.service.PedidoService;
import br.com.gameverse.service.UsuarioService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuariologado")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioLogadoResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    UsuarioRepository repository;

    @Inject
    PedidoService pedidoService;

    @Inject
    ClienteService clienteService;

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    UsuarioService usuarioService;

    private static final Logger LOG = Logger.getLogger(UsuarioLogadoResource.class);

    @GET
    @RolesAllowed({ "Cliente", "Admin" })
    public Response getUsuario() {
        // obtendo o login pelo token jwt
        String login = jwt.getSubject();
        LOG.infof("login: %s", login);
        try {
            LOG.info("obtendo o login pelo token jwt");
            LOG.info("Retornando login");
            return Response.ok(usuarioService.findByEmail(login)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao retornar informações do usuário logado: " + e.getMessage())
                    .build();
            
        }
    }

    @PATCH
    @Transactional
    @RolesAllowed({ "Cliente", "Admin" })
    @Path("/updateNome/{nome}")
    public Response updateNome(@PathParam("nome") String nome) {
        String login = jwt.getSubject();
        try {
            usuarioService.updateNome(login, nome);
            LOG.info("Nome atualizado!");
            return Response.ok("Informações do usuário atualizadas com sucesso").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar informações do usuário: " + e.getMessage())
                    .build();
        }
    }

    @PATCH
    @Transactional
    @RolesAllowed({ "Cliente", "Admin" })
    @Path("/updateSenha/{senha}")
    public Response updateSenha(@PathParam("senha") String senha) {
        String login = jwt.getSubject();
        try {
            usuarioService.updateSenha(login, senha);
            LOG.info("Senha atualizada!");
            return Response.ok("Informações do usuário atualizadas com sucesso").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar informações do usuário: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @RolesAllowed({ "Cliente", "Admin" })
    @Path("/PedidosDoUsuario")
    public Response getPedidosUsuario() {
        String login = jwt.getSubject();
        LOG.infof("Jwt: %s", login);
        Cliente usuarioLogado = clienteRepository.findByEmail(login);

        if (usuarioLogado != null) {
            List<PedidoResponseDTO> pedidos = pedidoService.pedidosUsuarioLogado(usuarioLogado);
            LOG.info("Retornando pedidos do usuário: " + login);
            return Response.ok(pedidos).build();
        } else {
            LOG.error("Usuário não encontrado: " + login);
            return Response.status(Response.Status.NOT_FOUND)
            .entity("Usuário não é cliente ou não foi encontrado.")
            .build();
        }
    }

    @GET
    @RolesAllowed({ "Cliente", "Admin" })
    @Path("/ItensDasComprasUsuario")
    public Response getItensPedidosUsuario() {
        String login = jwt.getSubject();
        Cliente usuarioLogado = clienteRepository.findByEmail(login);
        LOG.infof("Jwt: %s", login);
        if (usuarioLogado != null) {
            List<ItemPedidoResponseDTO> itens = pedidoService.findItensByUsuario(usuarioLogado);
            LOG.info("Retornando itens dos pedidos do usuário: " + login);
            return Response.ok(itens).build();
        } else {
            LOG.error("Usuário não encontrado: " + login);
            return Response.status(Response.Status.NOT_FOUND)
            .entity("Usuário não é cliente ou não foi encontrado.")
            .build();
        }
    }
}
