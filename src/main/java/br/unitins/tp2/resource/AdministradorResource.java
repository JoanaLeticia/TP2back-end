package br.unitins.tp2.resource;

import java.util.List;

import br.unitins.tp2.application.Result;
import br.unitins.tp2.dto.AdministradorDTO;
import br.unitins.tp2.dto.AdministradorResponseDTO;
import br.unitins.tp2.dto.PaginacaoResponse;
import br.unitins.tp2.service.AdministradorService;
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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdministradorResource {
    @Inject
    AdministradorService service;

    @POST
    @Transactional
    @RolesAllowed({ "Admin" })
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
    @RolesAllowed({ "Admin" })
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
    @RolesAllowed({ "Admin" })
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
    @RolesAllowed({ "Admin" })
    public long total() {
        return service.count();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ "Admin" })
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
    @RolesAllowed({ "Admin" })
    public long totalPorNome(@PathParam("nome") String nome) {
        return service.count(nome);
    }

    @GET
    @RolesAllowed({ "Admin" })
    public PaginacaoResponse<AdministradorResponseDTO> buscarTodos(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("page_size") @DefaultValue("10") int pageSize,
        @QueryParam("sort") @DefaultValue("id") String sort) {
        
        List<AdministradorResponseDTO> administradores = service.findAll(page, pageSize, sort); // Método ajustado para DTO
        long total = service.count();
        return new PaginacaoResponse<>(administradores, page, pageSize, total);
    }

    @GET
    @Path("/nome/{nome}")
    @RolesAllowed({ "Admin" })
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
