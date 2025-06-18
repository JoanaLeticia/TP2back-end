package br.com.gameverse.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import br.com.gameverse.application.Result;
import br.com.gameverse.dto.PaginacaoResponse;
import br.com.gameverse.dto.ProdutoDTO;
import br.com.gameverse.dto.ProdutoResponseDTO;
import br.com.gameverse.form.ProdutoImageForm;
import br.com.gameverse.model.ClassificacaoIndicativa;
import br.com.gameverse.model.Genero;
import br.com.gameverse.model.Plataforma;
import br.com.gameverse.model.TipoMidia;
import br.com.gameverse.repository.ProdutoRepository;
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

    @Inject
    ProdutoRepository produtoRepository;

    private static final Logger LOG = Logger.getLogger(ProdutoResource.class);

    @GET
    public PaginacaoResponse<ProdutoResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("page_size") @DefaultValue("10") int pageSize) {

        List<ProdutoResponseDTO> produtos = service.findAll(page, pageSize);
        long total = service.count();
        return new PaginacaoResponse<>(produtos, page, pageSize, total);
    }

    @GET
    @Path("search/nome/{nome}")
    public List<ProdutoResponseDTO> buscarPorNome(
            @PathParam("nome") String nome,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sort") @DefaultValue("id") String sort) {
        LOG.infof("Buscando produto pelo nome %s", nome);
        LOG.debug("Debug de busca pelo nome.");
        return service.findByNome(nome, page, size, sort);
    }

    @GET
    @Path("/{id}")
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
    @RolesAllowed({ "Admin" })
    @Path("/{id}")
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
    @RolesAllowed({ "Admin" })
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
    public long count() {
        return service.count();
    }

    @GET
    @Path("/nome/{nome}/count")
    public long totalPorNome(@PathParam("nome") String nome) {
        return service.count(nome);
    }

    @GET
    @Path("/plataforma/{nomePlataforma}/count")
    public long totalPorPlataforma(@PathParam("nomePlataforma") String nomePlataforma) {
        return service.countPorPlataforma(nomePlataforma);
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
    @RolesAllowed({ "Admin" })
    @Path("/image/upload")
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
    public Response getPlataformas() {
        return Response.ok(Plataforma.values()).build();
    }

    @GET
    @Path("/tiposMidia")
    public Response getTiposMidia() {
        return Response.ok(TipoMidia.values()).build();
    }

    @GET
    @Path("/generos")
    public Response getGeneros() {
        return Response.ok(Genero.values()).build();
    }

    @GET
    @Path("/classificacoes")
    public Response getClassificacoes() {
        return Response.ok(ClassificacaoIndicativa.values()).build();
    }

    @GET
    @Path("/plataforma/{nome}")
    public List<ProdutoResponseDTO> buscarPorPlataforma(@PathParam("nome") String nomePlataforma,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sort") @DefaultValue("id asc") String sort) {
        return service.buscarPorPlataforma(nomePlataforma, page, size, sort);
    }

    @GET
    @Path("/filtros/{plataforma}")
    public Response getFiltrosPorPlataforma(@PathParam("plataforma") String nomePlataforma) {
        try {
            LOG.infof("Buscando filtros para plataforma: %s", nomePlataforma);

            Map<String, Object> filtros = service.getFiltrosPorPlataforma(nomePlataforma);

            if (filtros.isEmpty()) {
                LOG.warnf("Nenhum filtro encontrado para plataforma: %s", nomePlataforma);
                return Response.status(Status.NOT_FOUND)
                        .entity("Nenhum produto encontrado para a plataforma especificada")
                        .build();
            }

            return Response.ok(filtros).build();

        } catch (RuntimeException e) {
            LOG.errorf("Erro ao buscar filtros para plataforma %s: %s", nomePlataforma, e.getMessage());
            return Response.status(Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

}
