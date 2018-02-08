package com.example.demo;

import com.neurotec.biometrics.NTemplateSize;
import com.neurotec.biometrics.client.NBiometricClient;
import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication

public class IdentificarRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentificarRestApplication.class, args);
    }

    
    
    
    
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setCacheSeconds(10); //reload messages every 10 seconds
        return messageSource;
    }

    @Bean
    public NBiometricClient biometricClient() {
        NBiometricClient biometricClient = new NBiometricClient();

        /* biometricClient.setDatabaseConnectionToOdbc("Dsn=con", "usertbl");
        NClusterBiometricConnection con = new NClusterBiometricConnection();
        con.setAdminPort(24932);
        con.setHost("localhost");
        con.setPort(25452);
        biometricClient.getRemoteConnections().add(con);
         */
        
        String connection=messageSource().getMessage("mysql.dataSource", null, Locale.getDefault());
        String table=messageSource().getMessage("mysql.table", null, Locale.getDefault());
        biometricClient.setDatabaseConnectionToOdbc(connection, table);
        
     
        return biometricClient;
    }

}
