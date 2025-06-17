package br.com.gameverse.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.com.gameverse.application.Result;
import br.com.gameverse.dto.PaginacaoResponse;
import br.com.gameverse.dto.TelefoneDTO;
import br.com.gameverse.dto.TelefoneResponseDTO;
import br.com.gameverse.service.TelefoneService;
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

@Path("/telefones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TelefoneResource {
    @Inject
    TelefoneService service;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed({ "Admin" })
    public PaginacaoResponse<TelefoneResponseDTO> buscarTodos(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("page_size") @DefaultValue("10") int pageSize,
        @QueryParam("sort") @DefaultValue("id") String sort) {
        
        List<TelefoneResponseDTO> telefones = service.findAll(page, pageSize, sort);
        long total = service.count();
        return new PaginacaoResponse<>(telefones, page, pageSize, total);
    }

    @GET
    @RolesAllowed({ "Admin" })
    @Path("search/numero/{numero}")
    public PaginacaoResponse<TelefoneResponseDTO> buscarPorNumero(
        @PathParam("numero") String numero,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("page_size") @DefaultValue("10") int pageSize,
        @QueryParam("sort") @DefaultValue("id") String sort) {
        
        List<TelefoneResponseDTO> telefones = service.findByNumero(numero, page, pageSize, sort);
        long total = service.count(numero);
        return new PaginacaoResponse<>(telefones, page, pageSize, total);
    }

    @GET
    @Path("/numero/{numero}/count")
    public long totalPorNumero(@PathParam("numero") String numero) {
        return service.count(numero);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Cliente", "Admin"})
    public Response buscarPorId(Long id) {
        try {
            TelefoneResponseDTO a = service.findById(id);
            return Response.ok(a).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @RolesAllowed({"Cliente", "Admin"})
    public Response incluir(TelefoneDTO dto) {
        try {
            return Response.status(Status.CREATED).entity(service.create(dto)).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Cliente", "Admin"})
    public Response alterar(TelefoneDTO dto, @PathParam("id") Long id) {
        try {
            service.update(dto, id);
            return Response.noContent().build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Cliente", "Admin"})
    @Transactional
    public Response apagar(@PathParam("id") Long id) {
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
    public long total() {
        return service.count();
    }
}
