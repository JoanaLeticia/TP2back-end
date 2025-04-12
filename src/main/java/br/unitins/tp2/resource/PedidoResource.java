package br.unitins.tp2.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.tp2.application.Result;
import br.unitins.tp2.dto.PaginacaoResponse;
import br.unitins.tp2.dto.PedidoDTO;
import br.unitins.tp2.dto.PedidoResponseDTO;
import br.unitins.tp2.service.PedidoService;
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
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {
    @Inject
    PedidoService service;

    @Inject
    JsonWebToken jwt;

    @POST
    @RolesAllowed({"Cliente" })
    public Response incluir(PedidoDTO dto) {
        try {
            String email = jwt.getSubject();
            PedidoResponseDTO retorno = service.create(dto, email);
            return Response.status(201).entity(retorno).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({ "Cliente",  "Admin" })
    public Response apagar(Long id) {
        try {
            service.delete(id);
            return Response.noContent().build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @GET
    @Path("/count")
    @RolesAllowed({   "Admin" })
    public long total() {
        return service.count();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({   "Admin" })
    public Response buscarPorId(Long id) {
        try {
            PedidoResponseDTO a = service.findById(id);
            return Response.ok(a).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @RolesAllowed({   "Admin" })
    public PaginacaoResponse<PedidoResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize,
            @QueryParam("sort") @DefaultValue("id") String sort) {

        List<PedidoResponseDTO> pedidos = service.findAll(page, pageSize, sort);
        long total = service.count();
        return new PaginacaoResponse<>(pedidos, page, pageSize, total);
    }
}
