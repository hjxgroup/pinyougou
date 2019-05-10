package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.core.pojo.item.ItemCat;
import com.hanjixin.core.pojo.order.Order;
import com.hanjixin.core.pojo.order.OrderItem;
import com.hanjixin.core.service.OrderService;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Order order){
        return orderService.search(page,rows,order);
    }

}
