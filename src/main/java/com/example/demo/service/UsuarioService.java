package com.example.demo.service;

import com.example.demo.model.Response;
import com.example.demo.model.Usuario;
import java.io.IOException;

public interface UsuarioService {
    
    public Response insertarUsuario(Usuario usuario) throws IOException;
    
    public Response identificarUsuario(Usuario usuario) throws IOException;
}
