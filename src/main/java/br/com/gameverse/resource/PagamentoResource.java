package br.com.gameverse.resource;

import java.util.List;

import br.com.gameverse.application.Result;
import br.com.gameverse.dto.PagamentoDTO;
import br.com.gameverse.dto.PagamentoResponseDTO;
import br.com.gameverse.dto.PaginacaoResponse;
import br.com.gameverse.service.PagamentoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/pagamentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PagamentoResource {
    @Inject
    PagamentoService service;

    @GET
    @RolesAllowed({ "Admin" })
    public PaginacaoResponse<PagamentoResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        List<PagamentoResponseDTO> pagamentos = service.findAll(page, size);
        long total = service.count();
        return new PaginacaoResponse<>(pagamentos, page, size, total);
    }

    @GET
    @RolesAllowed({ "Admin" })
    @Path("search/numerocartao/{numeroCartao}")
    public PaginacaoResponse<PagamentoResponseDTO> buscarPorNome(
            @PathParam("numeroCartao") String numeroCartao,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sort") @DefaultValue("id") String sort) {

        List<PagamentoResponseDTO> pagamentos = service.findByNumeroCartao(numeroCartao, page, size, sort);
        long total = service.count(numeroCartao);
        return new PaginacaoResponse<>(pagamentos, page, size, total);
    }

    @GET
    @Path("/numerocartao/{numeroCartao}/count")
    public long totalPorNumeroCartao(@PathParam("numeroCartao") String numeroCartao) {
        return service.count(numeroCartao);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ "Cliente", "Admin" })
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            PagamentoResponseDTO a = service.findById(id);
            return Response.ok(a).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @RolesAllowed({ "Cliente", "Admin" })
    public Response incluir(PagamentoDTO dto) {
        try {
            return Response.status(Status.CREATED).entity(service.create(dto)).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @RolesAllowed({ "Admin" })
    @Path("/{id}")
    public Response alterar(PagamentoDTO dto, @PathParam("id") Long id) {
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

    @PATCH
    @Path("/{id}/aprovar")
    @RolesAllowed({ "Admin" })
    @Transactional
    public Response aprovarPagamento(@PathParam("id") Long id) {
        try {
            service.aprovarPagamento(id);
            return Response.ok("Pagamento aprovado e pedido atualizado").build();
        } catch (EntityNotFoundException e) {
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalStateException e) {
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/pedido/{pedidoId}")
    @RolesAllowed({ "Cliente", "Admin" })
    public Response buscarPorPedido(@PathParam("pedidoId") Long pedidoId) {
        try {
            PagamentoResponseDTO pagamento = service.findByPedidoId(pedidoId);
            return Response.ok(pagamento).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

}
