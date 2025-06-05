package br.com.gameverse.resource;

import java.io.IOException;
import java.util.List;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import br.com.gameverse.application.Result;
import br.com.gameverse.dto.ProdutoDTO;
import br.com.gameverse.dto.ProdutoResponseDTO;
import br.com.gameverse.form.ProdutoImageForm;
import br.com.gameverse.model.Plataforma;
import br.com.gameverse.service.FileService;
import br.com.gameverse.service.ProdutoService;
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
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {
    @Inject
    ProdutoService service;

    @Inject
    FileService fileService;

    private static final Logger LOG = Logger.getLogger(ProdutoResource.class);

    @GET
    // @RolesAllowed({ "Admin" })
    public List<ProdutoResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize,
            @QueryParam("sort") @DefaultValue("id") String sort) {

        LOG.info("Buscando todos os produtos");
        LOG.debug("ERRO DE DEBUG");
        return service.findAll(page, pageSize, sort);
    }

    @GET
    @Path("/nome/{nome}")
    @RolesAllowed({ "Admin" })
    public List<ProdutoResponseDTO> buscarPorNome(
            @PathParam("nome") String nome,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize,
            @QueryParam("sort") @DefaultValue("id") String sort) {
        LOG.infof("Buscando produto pelo nome %s", nome);
        LOG.debug("Debug de busca pelo nome.");
        return service.findByNome(nome, page, pageSize, sort);
    }

    @GET
    @Path("/{id}")
    // @RolesAllowed({ "Admin" })
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            LOG.infof("Buscando Produto por ID (%d)", id);
            LOG.debug("Debug de busca de ID de Produto.");
            ProdutoResponseDTO a = service.findById(id);
            return Response.ok(a).build();
        } catch (EntityNotFoundException e) {
            LOG.errorf("Erro ao buscar um Produto por ID (%d).", id);
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @RolesAllowed({ "Admin" })
    public Response incluir(ProdutoDTO dto) {
        try {
            LOG.infof("Inserindo um produto: %s", dto.nome());
            ProdutoResponseDTO produto = service.create(dto);
            LOG.infof("Produto (%d) criado com sucesso.", produto.id());
            return Response.status(Status.CREATED).entity(produto).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({ "Admin" })
    public Response alterar(ProdutoDTO dto, @PathParam("id") Long id) {
        try {
            LOG.info("Atualizando produto");
            service.update(dto, id);
            LOG.info("Produto atualizado com sucesso.");
            return Response.noContent().build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.debug("Debug da atualização de Produto.");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({ "Admin" })
    public Response apagar(Long id) {
        try {
            LOG.info("Deletando o produto.");
            service.delete(id);
            LOG.info("Produto excluido com sucesso.");
            return Response.noContent().build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.debug("Debug da exclusão do Produto.");
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
    @Path("/nome/{nome}/count")
    @RolesAllowed({ "Admin" })
    public long totalPorNome(@PathParam("nome") String nome) {
        return service.count(nome);
    }

    @GET
    @Path("/image/download/{nomeImagem}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("nomeImagem") String nomeImagem) {
        ResponseBuilder response = Response.ok(fileService.download(nomeImagem));
        response.header("Content-Disposition", "attachment;filename=" + nomeImagem);
        return response.build();
    }

    @PATCH
    @Path("/image/upload")
    @RolesAllowed({ "Admin" })
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response salvarImagem(@MultipartForm ProdutoImageForm form) {
        try {
            fileService.salvar(form.getId(), form.getNomeImagem(), form.getImagem());
            return Response.noContent().build();
        } catch (IOException e) {
            return Response.status(Status.CONFLICT).build();
        }
    }

    @GET
    @Path("/plataformas")
    @RolesAllowed({ "Admin" })
    public Response getPlataformas() {
        return Response.ok(Plataforma.values()).build();
    }
}
