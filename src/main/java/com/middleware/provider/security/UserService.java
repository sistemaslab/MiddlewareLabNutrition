
package com.middleware.provider.security;

import com.middleware.lab.model.db.User;


public interface UserService {
    void save(User user);

    User findByUsername(String username);
}