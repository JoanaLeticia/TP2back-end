package br.unitins.tp2.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.tp2.dto.ItemPedidoResponseDTO;
import br.unitins.tp2.dto.PedidoResponseDTO;
import br.unitins.tp2.model.Cliente;
import br.unitins.tp2.repository.ClienteRepository;
import br.unitins.tp2.repository.UsuarioRepository;
import br.unitins.tp2.service.ClienteService;
import br.unitins.tp2.service.PedidoService;
import br.unitins.tp2.service.UsuarioService;
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
    UsuarioService usuarioService;

    @Inject
    PedidoService pedidoService;

    @Inject
    ClienteService clienteService;

    @Inject
    ClienteRepository clienteRepository;


    @GET
    @RolesAllowed({ "Cliente", "Admin" })
    public Response getUsuario() {
        // obtendo o login pelo token jwt
        String login = jwt.getSubject();
        try {
            return Response.ok(usuarioService.findByEmail(login)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao retornar informações do usuário logado: " + e.getMessage())
                    .build();
            
        }
    }

    @PATCH
    @Transactional
    @Path("/updateNome/{nome}")
    @RolesAllowed({ "Cliente", "Admin" })
    public Response updateNome(@PathParam("nome") String nome) {
        String login = jwt.getSubject();
        try {
            usuarioService.updateNome(login, nome);
            return Response.ok("Informações do usuário atualizadas com sucesso").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar informações do usuário: " + e.getMessage())
                    .build();
        }
    }

    @PATCH
    @Transactional
    @Path("/updateSenha/{senha}")
    @RolesAllowed({ "Cliente", "Admin" })
    public Response updateSenha(@PathParam("senha") String senha) {
        String login = jwt.getSubject();
        try {
            usuarioService.updateSenha(login, senha);
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
        Cliente usuarioLogado = clienteRepository.findByEmail(login);

        if (usuarioLogado != null) {
            List<PedidoResponseDTO> pedidos = pedidoService.pedidosUsuarioLogado(usuarioLogado);
            return Response.ok(pedidos).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @RolesAllowed({ "Cliente", "Admin" })
    @Path("/ItensDasComprasUsuario")
    public Response getItensPedidosUsuario() {
        String login = jwt.getSubject();
        Cliente usuarioLogado = clienteRepository.findByEmail(login);

        if (usuarioLogado != null) {
            List<ItemPedidoResponseDTO> itens = pedidoService.findItensByUsuario(usuarioLogado);
            return Response.ok(itens).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
