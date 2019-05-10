package com.hanjixin.core.service;

import com.hanjixin.core.pojo.user.User;
import entity.PageResult;

public interface UserService {
    void sendCode(String phone);

    void add(User user, String smscode);

    PageResult search(Integer page, Integer rows, User user);

    void updateStatus(Long[] ids, Integer status);

}
