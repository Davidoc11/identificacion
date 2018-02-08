package com.example.demo.model;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author David
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @NotNull(message = "error.idNull")
    private String userId;
    @NotNull(message = "error.imagesNull")
    @Valid
    private List<Imagen> imagenes;

}
