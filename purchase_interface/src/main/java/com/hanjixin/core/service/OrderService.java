package com.hanjixin.core.service;

import com.hanjixin.core.pojo.order.Order;
import entity.PageResult;

import java.util.List;

public interface OrderService {
    void add(Order order);

    PageResult search(Integer page, Integer rows, Order order);
    List<Order> findByYear(String year);

    List<String> findAllByOrder(String start, String end, String user_id);


    long[] showData(String year,String goodsName,String sellerid);
}
