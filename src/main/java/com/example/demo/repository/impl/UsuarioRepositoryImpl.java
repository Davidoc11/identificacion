package com.example.demo.repository.impl;

import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import com.example.demo.model.Response;
import com.example.demo.utils.Utils;
import com.neurotec.biometrics.NBiometricOperation;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NBiometricTask;
import com.neurotec.biometrics.NFPosition;
import com.neurotec.biometrics.NFinger;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.NSubject.MatchingResultCollection;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.io.NFile;
import com.neurotec.licensing.NLicense;
import com.neurotec.tutorials.util.LibraryManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Repository;

/**
 * @author David
 */
@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository{

    @Autowired
    private MessageSource msgSource;

    @Autowired
    private NBiometricClient biometricClient;
   
    @Autowired
    private GenerarImpl generador;
    
    @Value("${pathTemplates}")
    public String carpetaTemplates;
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    public NSubject generarSubject(Usuario usuario, Imagen imagen) throws IOException {

        NSubject subject = null;
        try {
            NFinger finger = new NFinger();
            finger.setSampleBuffer(NFile.readAllBytes(imagen.getFilename()));
            subject = new NSubject();
            NFPosition posicion=Utils.obtenerPosicionDedo(imagen.getPosicion());
            finger.setPosition(posicion);

            subject.getFingers().add(finger);
            String path = carpetaTemplates + "" + usuario.getUserId() + "/" + finger.getPosition().name();

            String subjectId = usuario.getUserId() + "_" + finger.getPosition().name();
            subject.setId(subjectId);
            crearTemplateEnDisco(subject, path);
            imagen.setPathTemplate(path);
            imagen.setSubject(subject);
            return imagen.getSubject();

        } finally {
            if (subject != null) {

            }
        }
    }

    public Response identificarUsuario(Usuario usuario) throws IOException {
        generador.crearImagenesUsuario(usuario);
        Response response = new Response();

        NSubject.MatchingResultCollection coincidencias = null;
        for (Imagen imagen : usuario.getImagenes()) {
            try {
                String msg = null;
                long start = System.nanoTime();
                if (imagen.getSubject() == null) {
                    generarSubject(usuario, imagen);
                }
                coincidencias = obtenerCoincidencias(imagen.getSubject());
                if (coincidencias != null) {
                    response.setFlag(true);
                    msg = msgSource.getMessage("success.login", null, Locale.getDefault());
                    response.setMessage(msg);
                } else {
                    response.setFlag(false);
                    msg = msgSource.getMessage("success.loginError", null, Locale.getDefault());
                    response.setMessage(msg);
                    return response;
                }

                /*                long totalTime = System.nanoTime() - start;//;(endTime - startTime) / 1000;
                System.out.println("Total: "
                        + String.format("%d min, %d millisec",
                                TimeUnit.NANOSECONDS.toHours(totalTime),
                                TimeUnit.NANOSECONDS.toMillis(totalTime)
                                - TimeUnit.MINUTES.toMillis(TimeUnit.NANOSECONDS.toMinutes(totalTime))));
                 */
            } finally {

                if (imagen.getSubject() != null) {
                    imagen.getSubject().dispose();
                }
            }
        }
        return response;
    }

    public Response insertarUsuario(Usuario usuario) throws IOException {
        String msg=null;
        generador.crearImagenesUsuario(usuario);
        Response response = new Response();
        for (Imagen imagen : usuario.getImagenes()) {
            if (imagen.getSubject() == null) {
                generarSubject(usuario, imagen);
            }

            String id = imagen.getSubject().getId();
            imagen.getSubject().setTemplateBuffer(NFile.readAllBytes(imagen.getPathTemplate()));
            imagen.getSubject().setId(id);

            MatchingResultCollection coincidencias = obtenerCoincidencias(imagen.getSubject());
            if (coincidencias != null) {
                String idMatch = coincidencias.get(0).getId();
                String dedo = idMatch.substring(idMatch.indexOf("_") + 1).replaceAll("_", " ");
                idMatch = idMatch.substring(0, idMatch.indexOf("_"));
                response.setFlag(false);
                
                
                msg=msgSource.getMessage("error.usuarioDuplicado", new Object[]{idMatch,dedo}, Locale.getDefault());
                response.setMessage(msg);
                return response;
            }

            NBiometricTask task = biometricClient.createTask(EnumSet.of(NBiometricOperation.ENROLL_WITH_DUPLICATE_CHECK), imagen.getSubject());
            biometricClient.performTask(task);
            if (task.getStatus() == NBiometricStatus.OK) {
                // JOptionPane.showMessageDialog(null, "Enrrolamiento exitoso.");
                response.setFlag(true);
                msg=msgSource.getMessage("success.registroCorrecto", null, Locale.getDefault());
                response.setMessage(msg);
            } else {
                String msgError = "";
                switch (task.getStatus()) {
                    case DUPLICATE_ID:
                        msgError = msgSource.getMessage("error.idDuplicado", null, Locale.getDefault());
                        break;
                    case DUPLICATE_FOUND:

                        msgError = msgSource.getMessage("error.huellaDuplicada", null, Locale.getDefault());
                        break;
                    default:
                        msgError = task.getStatus().name();
                        break;
                }
                response.setFlag(false);
                response.setMessage(msgError);
            }
        }
        return response;

    }

    public NSubject.MatchingResultCollection obtenerCoincidencias(NSubject subject) {
        NBiometricTask task = biometricClient.createTask(EnumSet.of(NBiometricOperation.IDENTIFY), subject);

        biometricClient.performTask(task);

        if (task.getStatus() != NBiometricStatus.OK) {
            return null;
        }
        return subject.getMatchingResults();
    }

    public void crearTemplateEnDisco(NSubject subject, String path) throws IOException {

        long start = System.nanoTime();
        NBiometricStatus status = biometricClient.createTemplate(subject);

        long totalTime = System.nanoTime() - start;//;(endTime - startTime) / 1000;
        System.out.println("Crear template: "
                + String.format("%d min, %d millisec",
                        TimeUnit.NANOSECONDS.toHours(totalTime),
                        TimeUnit.NANOSECONDS.toMillis(totalTime)
                        - TimeUnit.MINUTES.toMillis(TimeUnit.NANOSECONDS.toMinutes(totalTime))));

        //Se pregunta el status 
        if (status == NBiometricStatus.OK) {

            NFile.writeAllBytes(path, subject.getTemplateBuffer());

            // JOptionPane.showMessageDialog(null, "El template se genero con exito");
        }
    }

    @PostConstruct
    public void licencias() throws IOException {
        List<String> components = new ArrayList<String>();
        components.add("Biometrics.FingerMatching");
        components.add("Biometrics.FingerExtractionFast");
        LibraryManager.initLibraryPath();
        boolean anyMatchingComponent = false;
        for (String component : components) {
            if (NLicense.obtainComponents("/local", 5000, component)) {
                anyMatchingComponent = true;
            }
        }

        if (!anyMatchingComponent) {
            System.err.format("Could not obtain any of components: %s%n", components);
            System.exit(-1);
        }
    }

    
}
