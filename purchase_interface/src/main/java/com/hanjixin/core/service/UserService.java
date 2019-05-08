package com.hanjixin.core.service;

import com.hanjixin.core.pojo.user.User;

public interface UserService {
    void sendCode(String phone);

    void add(User user, String smscode);
}
