package com.middleware.lab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Clase con constantes generales de la aplicación
 * @author Natalia  Manrique
 */
@Component
public class EnviromentConfig {

    private final String profile;
    private boolean runJobs = false;
    
    private static final Logger LOGGER = LogManager.getLogger(EnviromentConfig.class);

    @Autowired
    public EnviromentConfig (
        @Value("${spring.profiles.active}") String profile,
        @Value("${spring.profiles.runJobs}") boolean runJobs) {
        this.profile = profile;
        this.runJobs = runJobs;
        LOGGER.info("CONFIGURATION:");
        LOGGER.info("spring.profiles.active = "+this.profile);
        LOGGER.info("spring.profiles.runJobs = "+this.runJobs);
    }

    public String getProfile() {
        if(profile == null){
            return "test";
        }
        return profile;
    }

    public boolean isRunJobs() {
        return runJobs;
    }
    
}
