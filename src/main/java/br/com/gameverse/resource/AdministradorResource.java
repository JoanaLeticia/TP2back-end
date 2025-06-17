package br.com.gameverse.resource;

import java.util.List;

import br.com.gameverse.application.Result;
import br.com.gameverse.dto.AdministradorDTO;
import br.com.gameverse.dto.AdministradorResponseDTO;
import br.com.gameverse.dto.PaginacaoResponse;
import br.com.gameverse.service.AdministradorService;
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

@Path("/administradores")
@RolesAllowed({ "Admin" })
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdministradorResource {
    @Inject
    AdministradorService service;

    @POST
    @Transactional
    public Response incluir(AdministradorDTO dto) {
        try {
            AdministradorResponseDTO retorno = service.create(dto);
            return Response.status(201).entity(retorno).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response alterar(AdministradorDTO dto, @PathParam("id") Long id) {
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

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            AdministradorResponseDTO administrador = service.findById(id);
            return Response.ok(administrador).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/nome/{nome}/count")
    public long totalPorNome(@PathParam("nome") String nome) {
        return service.count(nome);
    }

    @GET
    public PaginacaoResponse<AdministradorResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize) {

        List<AdministradorResponseDTO> admins = service.findAll(page, pageSize);
        long total = service.count();
        return new PaginacaoResponse<>(admins, page, pageSize, total);
    }

    @GET
    @Path("search/nome/{nome}")
    public PaginacaoResponse<AdministradorResponseDTO> buscarPorNome(
        @PathParam("nome") String nome,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("page_size") @DefaultValue("10") int pageSize,
        @QueryParam("sort") @DefaultValue("id") String sort) {
        
        List<AdministradorResponseDTO> administradores = service.findByNome(nome, page, pageSize, sort);
        long total = service.count(nome);
        return new PaginacaoResponse<>(administradores, page, pageSize, total);
    }
}
