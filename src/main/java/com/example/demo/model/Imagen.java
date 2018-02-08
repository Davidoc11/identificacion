package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neurotec.biometrics.NSubject;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author David
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Imagen {

    @NotEmpty(message = "error.imagenVacia")
    @NotNull(message = "error.imagenNull")
    private String image;
    
    @NotEmpty(message = "error.nombreImagenVacia")
    @NotNull(message = "error.nombreImagenNull")
    private String filename;
    
    
    @NotNull(message = "error.posicionNull")
    @Max(value=10,message = "error.posicionMax")
    @Min(value=1,message = "error.posicionMin")
    private Integer posicion;
    
    @JsonIgnore
    private NSubject subject;
    @JsonIgnore
    private String pathTemplate;

}
