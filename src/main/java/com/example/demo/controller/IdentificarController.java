package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.model.Response;
import com.example.demo.service.UsuarioService;
import java.io.IOException;
import javax.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author David
 */
@RestController
@RequestMapping("/api/user")
@Validated
public class IdentificarController {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public Response logIn(@RequestBody @Valid Usuario usuario) {

        try {
            return usuarioService.identificarUsuario(usuario);
        } catch (IOException ex) {
            return new Response(false, ex.getMessage(), null);
        }

    }

    @PostMapping("/registro")
    public Response signUp(@RequestBody @Valid Usuario usuario) {

        try {
            return usuarioService.insertarUsuario(usuario);
        } catch (IOException ex) {
            log.info(ex.getMessage());
            return null;
        }
    }

    /*  @ExceptionHandler({Exception.class, UsuarioDuplicadoException.class})
    void handleBadRequests(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }*/
}
