package br.com.gameverse.resource;

import java.util.List;

import br.com.gameverse.application.Result;
import br.com.gameverse.dto.EstadoDTO;
import br.com.gameverse.dto.EstadoResponseDTO;
import br.com.gameverse.dto.PaginacaoResponse;
import br.com.gameverse.service.EstadoService;
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

@Path("/estados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstadoResource {

    @Inject
    EstadoService service;

    @GET
    public PaginacaoResponse<EstadoResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize) {

        List<EstadoResponseDTO> estados = service.findAll(page, pageSize);
        long total = service.count();
        return new PaginacaoResponse<>(estados, page, pageSize, total);
    }

    @GET
    @Path("search/nome/{nome}")
    public PaginacaoResponse<EstadoResponseDTO> buscarPorNome(
        @PathParam("nome") String nome,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("page_size") @DefaultValue("10") int pageSize,
        @QueryParam("sort") @DefaultValue("id") String sort) {
        
        List<EstadoResponseDTO> estados = service.findByNome(nome, page, pageSize, sort);
        long total = service.count(nome);
        return new PaginacaoResponse<>(estados, page, pageSize, total);
    }

    @GET
    @Path("/nome/{nome}/count")
    public long totalPorNome(@PathParam("nome") String nome) {
        return service.count(nome);
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            EstadoResponseDTO a = service.findById(id);
            return Response.ok(a).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/sigla/{sigla}")
    public Response buscarPorSigla(String sigla) { 
        try {
            return Response.ok(service.findBySigla(sigla)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @RolesAllowed({ "Admin" })
    public Response incluir(EstadoDTO dto) {
        try {
            return Response.status(Status.CREATED).entity(service.create(dto)).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({ "Admin" })
    public Response alterar(EstadoDTO dto, @PathParam("id") Long id) {
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
    @RolesAllowed({ "Admin" })
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