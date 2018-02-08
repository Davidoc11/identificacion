/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.repository;

import com.example.demo.model.Imagen;
import com.example.demo.model.Response;
import com.example.demo.model.Usuario;
import com.neurotec.biometrics.NSubject;
import java.io.IOException;

/**
 *
 * @author Angel
 */
public interface UsuarioRepository {

    public NSubject generarSubject(Usuario usuario, Imagen imagen) throws IOException;

    public Response identificarUsuario(Usuario usuario) throws IOException;

    public Response insertarUsuario(Usuario usuario) throws IOException;

    public NSubject.MatchingResultCollection obtenerCoincidencias(NSubject subject);

    public void crearTemplateEnDisco(NSubject subject, String path) throws IOException;
}
