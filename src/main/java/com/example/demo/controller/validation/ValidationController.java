package com.example.demo.controller.validation;

import com.example.demo.model.ErrorMessage;
import com.example.demo.model.Response;
import java.awt.TrayIcon.MessageType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author David
 */
@ControllerAdvice
public class ValidationController {

    @Autowired
    private MessageSource msgSource;
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        return processFieldError(result.getFieldErrors());
    }

    private Response processFieldError(List<FieldError> fieldErrors) {

        List<ErrorMessage> errors = new ArrayList<>();
        Locale currentLocale = LocaleContextHolder.getLocale();
        for (FieldError fieldError : fieldErrors) {
            if (fieldError != null) {

                String msg = msgSource.getMessage(fieldError.getDefaultMessage(), null, currentLocale);
                ErrorMessage error = new ErrorMessage(MessageType.ERROR, msg);
                errors.add(error);
            }
        }
        String errorMsg = msgSource.getMessage("error.erroresValidacion", null, currentLocale);
        return new Response(false, errorMsg, errors);
    }
}
