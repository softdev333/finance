package com.mb.finance.service;

import com.mb.finance.entities.User;

public interface UserService {

    User saveUser(User user);

    User getUser(String userId);

    String authenticate(String username, String password);

}
