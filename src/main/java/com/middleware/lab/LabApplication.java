package com.middleware.lab;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
@EnableWebMvc
@EnableJms
@EnableAsync
@SpringBootApplication
@ComponentScan({"com.middleware", "com.middleware.lab" ,  "blacksip.vtex.client", "lab.navasoft.model.soap"})
public class LabApplication {
 
 
    public static void main(String[] args) {
        SpringApplication.run(LabApplication.class, args);
      
    } 
}
