/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;

import ch.qos.logback.core.net.server.Client;
import com.example.demo.model.Usuario;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Angel
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = IdentificarRestApplication.class)
public class UnTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createClient() {
        
        ResponseEntity<Usuario> responseEntity
                = restTemplate.postForEntity("/api/user/registro", new Usuario(null,null), Usuario.class);
        Usuario cliente = responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(cliente.getImagenes());
        assertNull(cliente.getUserId());
        
    }
}
