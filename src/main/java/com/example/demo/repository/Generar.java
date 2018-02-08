package com.example.demo.repository;

import com.example.demo.model.Usuario;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface Generar {

    public void crearImagenesUsuario(Usuario usuario) throws FileNotFoundException, IOException;
}
