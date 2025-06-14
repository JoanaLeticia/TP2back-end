package br.com.gameverse.resource;

import java.util.List;

import br.com.gameverse.application.Result;
import br.com.gameverse.dto.EnderecoDTO;
import br.com.gameverse.dto.EnderecoResponseDTO;
import br.com.gameverse.dto.PaginacaoResponse;
import br.com.gameverse.service.EnderecoService;
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

@Path("/enderecos")
// @RolesAllowed({ "Admin" })
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnderecoResource {
    @Inject
    EnderecoService service;

    @GET
    public PaginacaoResponse<EnderecoResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize,
            @QueryParam("sort") @DefaultValue("id") String sort) {
        List<EnderecoResponseDTO> enderecos = service.findAll(page, pageSize, sort);
        long total = service.count();
        return new PaginacaoResponse<>(enderecos, page, pageSize, total);
    }

    @GET
    @Path("search/logradouro/{logradouro}")
    public PaginacaoResponse<EnderecoResponseDTO> buscarPorLogradouro(
            @PathParam("logradouro") String logradouro,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize,
            @QueryParam("sort") @DefaultValue("id") String sort) {

        List<EnderecoResponseDTO> enderecos = service.findByLogradouro(logradouro, page, pageSize, sort);
        long total = service.count(logradouro);
        return new PaginacaoResponse<>(enderecos, page, pageSize, total);
    }

    @GET
    @Path("/municipio/{idMunicipio}")
    public PaginacaoResponse<EnderecoResponseDTO> buscarPorMunicipio(
            @PathParam("idMunicipio") Long idMunicipio,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize,
            @QueryParam("sort") @DefaultValue("id") String sort) {
        List<EnderecoResponseDTO> enderecos = service.findByMunicipio(idMunicipio, page, pageSize, sort);
        long total = enderecos.size();
        return new PaginacaoResponse<>(enderecos, page, pageSize, total);
    }

    @GET
    @Path("/bairro/{bairro}")
    public PaginacaoResponse<EnderecoResponseDTO> buscarPorBairro(
            @PathParam("bairro") String bairro,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize,
            @QueryParam("sort") @DefaultValue("id") String sort) {
        List<EnderecoResponseDTO> enderecos = service.findByBairro(bairro, page, pageSize, sort);
        long total = service.countByBairro(bairro);
        return new PaginacaoResponse<>(enderecos, page, pageSize, total);
    }

    @GET
    @Path("/logradouro/{logradouro}/count")
    public Response totalPorLogradouro(@PathParam("logradouro") String logradouro) {
        try {
            return Response.ok(service.findByLogradouro(logradouro, 0, 0, logradouro)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            EnderecoResponseDTO a = service.findById(id);
            return Response.ok(a).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response incluir(EnderecoDTO dto) {
        try {
            return Response.status(Status.CREATED).entity(service.create(dto)).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response alterar(EnderecoDTO dto, @PathParam("id") Long id) {
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
    @Path("/cliente/{clienteId}")
    public Response buscarPorClienteId(@PathParam("clienteId") Long clienteId) {
        try {
            List<EnderecoResponseDTO> enderecos = service.findByClienteId(clienteId);
            return Response.ok(enderecos).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
