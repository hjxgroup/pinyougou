package com.hanjixin.core.service;

import com.hanjixin.core.pojo.order.Order;

public interface OrderService {
    void add(Order order);

    PageResult search(Integer page, Integer rows, Order order);
}
