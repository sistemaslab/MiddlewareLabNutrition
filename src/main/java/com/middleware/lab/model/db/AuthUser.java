package com.middleware.lab.model.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.middleware.data.annotation.Ignore;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Alejandro Cadena
 */
@Entity
@Getter @Setter
public class AuthUser {
    
    @Ignore
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    
    @Id
    private String username;
    //@JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    private String email;
    private String fullname;
    private Boolean enabled;
    

    public AuthUser() {
    }

    public AuthUser(String username, String password, String email, String fullname, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.enabled = enabled;
    }
        
}
