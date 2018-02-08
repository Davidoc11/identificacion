package com.example.demo.repository.impl;

import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import com.example.demo.repository.Generar;
import com.example.demo.utils.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * @author David
 */
@Repository
public class GenerarImpl implements Generar {

    @Value("${pathTemplates}")
    private String RUTA;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void crearDirectorio() {
        File file = new File(this.RUTA);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.mkdir();
        }
    }

    public void crearImagenesUsuario(Usuario usuario) throws FileNotFoundException, IOException {
        File carpetaUsuario = new File(this.RUTA + usuario.getUserId());
        if (!carpetaUsuario.exists()) {
            carpetaUsuario.mkdir();
        }

        for (Imagen imagen : usuario.getImagenes()) {
            byte[] imageByteArray = Utils.decodeImage(imagen.getImage());
            String rutaCompleta = carpetaUsuario.getAbsolutePath() + "/" + imagen.getFilename();
            Path path = Paths.get(rutaCompleta);
            Files.write(path, imageByteArray);

            boolean exito = path.toFile().exists();
            if (exito) {
                imagen.setFilename(path.toFile().getAbsolutePath());
            } else {
                throw new FileNotFoundException();
            }
        }
    }
}
