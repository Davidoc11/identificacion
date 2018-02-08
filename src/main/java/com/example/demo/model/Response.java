package com.example.demo.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author David
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private Boolean flag;
    private String message;
    private List<ErrorMessage> errors;
}
