package br.com.gameverse.resource;

import br.com.gameverse.dto.AuthUsuarioDTO;
import br.com.gameverse.dto.UsuarioResponseDTO;
import br.com.gameverse.model.Perfil;
import br.com.gameverse.service.AdministradorService;
import br.com.gameverse.service.ClienteService;
import br.com.gameverse.service.HashService;
import br.com.gameverse.service.JwtService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    public AdministradorService administradorService;

    @Inject
    public ClienteService clienteService;

    @Inject
    public HashService hashService;

    @Inject
    public JwtService jwtService;

    @POST
    public Response login(AuthUsuarioDTO dto) {
        String hash = hashService.getHashSenha(dto.senha());

        UsuarioResponseDTO usuario = null;
        // administrador
        if (dto.perfil() == Perfil.ADMIN) {
            usuario = administradorService.login(dto.login(), hash);
        } else if (dto.perfil() == Perfil.CLIENTE) { // cliente
            usuario = clienteService.login(dto.login(), hash);   
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }

        if (usuario == null) {
            return Response.status(Status.UNAUTHORIZED).build();
        }

        return Response.ok(usuario)
            .header("Authorization", jwtService.generateJwt(usuario))
            .build();
    }


}