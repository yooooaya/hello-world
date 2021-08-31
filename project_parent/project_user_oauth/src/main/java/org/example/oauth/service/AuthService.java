package org.example.oauth.service;


import org.example.oauth.util.AuthToken;
import org.example.pojo.Result;
import org.example.user.pojo.User;

import java.util.List;

public interface AuthService {
    AuthToken login(String username, String password, String clientId, String clientSecret);
    Result findAll();
}
