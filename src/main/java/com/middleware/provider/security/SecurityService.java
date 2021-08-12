package com.middleware.provider.security;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}