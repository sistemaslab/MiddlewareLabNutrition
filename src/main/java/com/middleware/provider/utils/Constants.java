package com.middleware.provider.utils;


import com.middleware.lab.model.db.AuthUser;
import com.middleware.lab.repository.AuthUserRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Clase con constantes generales de la aplicación
 * @author Alejandro Cadena
 */
@Component
public class Constants {
    
    public static String MIDDLEWARE_USER;
    public static String URL_MIDDLEWARE_BASE_SOAP;
    public static String URL_MIDDLEWARE_BASE_REST;
    public static AuthUser AUTH_USER;
    public static String PROFILE;
    
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Constants.class);

    @Autowired
    public Constants (
            AuthUserRepository authUserDao,
            @Value("${spring.profiles.active}")String profile) {

        Constants.MIDDLEWARE_USER="hacebMiddleware";
        Constants.PROFILE = profile;
        LOGGER.info("profile = "+profile);
        Constants.AUTH_USER=authUserDao.findByName(Constants.MIDDLEWARE_USER);
    }

}
