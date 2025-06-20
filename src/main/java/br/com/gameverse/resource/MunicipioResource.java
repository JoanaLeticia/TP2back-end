package br.com.gameverse.resource;

import java.util.List;

import br.com.gameverse.application.Result;
import br.com.gameverse.dto.EstadoResponseDTO;
import br.com.gameverse.dto.MunicipioDTO;
import br.com.gameverse.dto.MunicipioResponseDTO;
import br.com.gameverse.dto.PaginacaoResponse;
import br.com.gameverse.model.Estado;
import br.com.gameverse.service.MunicipioService;
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

@Path("/municipios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MunicipioResource {
    @Inject
    MunicipioService service;

    @GET
    public PaginacaoResponse<MunicipioResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize) {

        List<MunicipioResponseDTO> estados = service.findAll(page, pageSize);
        long total = service.count();
        return new PaginacaoResponse<>(estados, page, pageSize, total);
    }

    @GET
    @Path("search/nome/{nome}")
    public PaginacaoResponse<MunicipioResponseDTO> buscarPorNome(
            @PathParam("nome") String nome,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize,
            @QueryParam("sort") @DefaultValue("id") String sort) {

        List<MunicipioResponseDTO> municipios = service.findByNome(nome, page, pageSize, sort);
        long total = service.count(nome);
        return new PaginacaoResponse<>(municipios, page, pageSize, total);
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
            MunicipioResponseDTO a = service.findById(id);
            return Response.ok(a).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/estados/{idEstado}")
    public PaginacaoResponse<MunicipioResponseDTO> buscarPorEstado(
            @PathParam("idEstado") Long idEstado,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize,
            @QueryParam("sort") @DefaultValue("id") String sort) {

        List<MunicipioResponseDTO> municipios = service.findByEstado(idEstado, page, pageSize, sort);
        long total = municipios.size();
        return new PaginacaoResponse<>(municipios, page, pageSize, total);
    }

    @GET
    @Path("/estados/estado/{idEstado}")
    public Response buscarPorEstado(@PathParam("idEstado") Long idEstado) {
        try {
            List<MunicipioResponseDTO> municipios = service.findByEstado(idEstado);
            return Response.ok(municipios).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/estados")
    public Response buscarEstados() {
        try {
            List<Estado> estados = service.getEstados();
            return Response.ok(estados).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response incluir(MunicipioDTO dto) {
        try {
            return Response.status(Status.CREATED).entity(service.create(dto)).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response alterar(MunicipioDTO dto, @PathParam("id") Long id) {
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
    @Path("/search/nomesimple/{nome}")
    public Response buscarPorNomeSemPaginacao(@PathParam("nome") String nome) {
        try {
            List<MunicipioResponseDTO> municipios = service.findByNome(nome);
            return Response.ok(municipios).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

}
