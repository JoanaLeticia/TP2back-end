package br.com.gameverse.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import br.com.gameverse.application.Result;
import br.com.gameverse.dto.ClienteDTO;
import br.com.gameverse.dto.ClienteResponseDTO;
import br.com.gameverse.dto.PaginacaoResponse;
import br.com.gameverse.model.Cliente;
import br.com.gameverse.repository.ClienteRepository;
import br.com.gameverse.service.ClienteService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

    @Inject
    ClienteService service;

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    JsonWebToken jwt;

    private static final Logger LOG = Logger.getLogger(PedidoResource.class);

    @POST
    public Response incluir(ClienteDTO dto) {
        try {
            return Response.status(Status.CREATED).entity(service.create(dto)).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @RolesAllowed({ "Cliente", "Admin" })
    @Path("/{id}")
    public Response alterar(ClienteDTO dto, @PathParam("id") Long id) {
        try {
            if (!isOwner(id) && !jwt.getGroups().contains("Admin")) {
                return Response.status(Status.FORBIDDEN).build();
            }
            service.update(dto, id);
            return Response.noContent().build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @DELETE
    @RolesAllowed({ "Cliente", "Admin" })
    @Path("/{id}")
    @Transactional
    public Response apagar(@PathParam("id") Long id) {
        try {
            if (!isOwner(id) && !jwt.getGroups().contains("Admin")) {
                return Response.status(Status.FORBIDDEN).build();
            }
            service.delete(id);
            return Response.noContent().build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @GET
    @RolesAllowed({ "Admin" })
    @Path("/count")
    public long total() {
        return service.count();
    }

    @GET
    @RolesAllowed({ "Cliente", "Admin" })
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            if (!isOwner(id) && !jwt.getGroups().contains("Admin")) {
                return Response.status(Status.FORBIDDEN).build();
            }
            ClienteResponseDTO a = service.findById(id);
            return Response.ok(a).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @RolesAllowed({ "Admin" })
    public PaginacaoResponse<ClienteResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize) {

        List<ClienteResponseDTO> clientes = service.findAll(page, pageSize);
        long total = service.count();
        return new PaginacaoResponse<>(clientes, page, pageSize, total);
    }

    @GET
    @RolesAllowed({ "Admin" })
    @Path("search/nome/{nome}")
    public PaginacaoResponse<ClienteResponseDTO> buscarPorNome(
            @PathParam("nome") String nome,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize,
            @QueryParam("sort") @DefaultValue("id") String sort) {

        List<ClienteResponseDTO> clientes = service.findByNome(nome, page, pageSize, sort);
        long total = service.count(nome);
        return new PaginacaoResponse<>(clientes, page, pageSize, total);
    }

    private boolean isOwner(Long clienteId) {
        if (clienteId == null || jwt == null || jwt.getSubject() == null) {
            return false;
        }

        try {
            String email = jwt.getSubject();
            Cliente cliente = clienteRepository.findByEmail(email);
            return cliente != null && clienteId.equals(cliente.getId());
        } catch (Exception e) {
            LOG.error("Erro ao verificar propriedade do recurso", e);
            return false;
        }
    }

}
