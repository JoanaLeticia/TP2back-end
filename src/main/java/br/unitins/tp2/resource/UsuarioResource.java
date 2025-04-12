package br.unitins.tp2.resource;

import java.util.List;

import br.unitins.tp2.application.Result;
import br.unitins.tp2.dto.PaginacaoResponse;
import br.unitins.tp2.dto.UsuarioDTO;
import br.unitins.tp2.dto.UsuarioResponseDTO;
import br.unitins.tp2.service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
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

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {
    @Inject
    UsuarioService service;

    @GET
    @RolesAllowed({ "Admin" })
    public PaginacaoResponse<UsuarioResponseDTO> buscarTodos(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("page_size") @DefaultValue("10") int pageSize,
        @QueryParam("sort") @DefaultValue("id") String sort) {
        
        List<UsuarioResponseDTO> usuarios = service.findAll(page, pageSize, sort);
        long total = service.count();
        return new PaginacaoResponse<>(usuarios, page, pageSize, total);
    }

    @GET
    @Path("/nome/{nome}")
    @RolesAllowed({ "Admin" })
    public PaginacaoResponse<UsuarioResponseDTO> buscarPorNome(
        @PathParam("nome") String nome,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("page_size") @DefaultValue("10") int pageSize,
        @QueryParam("sort") @DefaultValue("id") String sort) {
        
        List<UsuarioResponseDTO> usuarios = service.findByNome(nome, page, pageSize, sort);
        long total = service.count(nome);
        return new PaginacaoResponse<>(usuarios, page, pageSize, total);
    }

    @GET
    @Path("/nome/{nome}/count")
    @RolesAllowed({ "Admin" })
    public long totalPorNome(@PathParam("nome") String nome) {
        return service.count(nome);
    }

    @POST
    @RolesAllowed({ "Admin" })
    public Response incluir(UsuarioDTO dto) {
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
    public Response alterar(UsuarioDTO dto, @PathParam("id") Long id) {
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
    @RolesAllowed({ "Admin" })
    public long total() {
        return service.count();
    }
}
