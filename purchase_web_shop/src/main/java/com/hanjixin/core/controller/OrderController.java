package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.core.pojo.order.Order;
import com.hanjixin.core.service.OrderService;
import entity.PageResult;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setSellerId(name);
        return orderService.search(page,rows,order);
    }
    @RequestMapping("/showData")
    public long[] showData(String year,String goodsName){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderService.showData(year,goodsName,name);
    }
}
