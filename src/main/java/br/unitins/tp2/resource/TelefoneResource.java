package br.unitins.tp2.resource;

import java.util.List;

import br.unitins.tp2.application.Result;
import br.unitins.tp2.dto.PaginacaoResponse;
import br.unitins.tp2.dto.TelefoneDTO;
import br.unitins.tp2.dto.TelefoneResponseDTO;
import br.unitins.tp2.service.TelefoneService;
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
    @Path("/numero/{numero}")
    @RolesAllowed({ "Admin" })
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
    @RolesAllowed({ "Admin" })
    public long totalPorNumero(@PathParam("numero") String numero) {
        return service.count(numero);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ "Admin" })
    public Response buscarPorId(Long id) {
        try {
            TelefoneResponseDTO a = service.findById(id);
            return Response.ok(a).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @RolesAllowed({ "Admin" })
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
    @RolesAllowed({ "Admin" })
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
