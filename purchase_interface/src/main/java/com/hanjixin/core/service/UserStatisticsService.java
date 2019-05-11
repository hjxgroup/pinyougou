package com.hanjixin.core.service;

import com.hanjixin.core.pojo.user.User;

import java.util.List;

public interface UserStatisticsService {
    List<String> findAll(String start, String end, String statusValue);

}
