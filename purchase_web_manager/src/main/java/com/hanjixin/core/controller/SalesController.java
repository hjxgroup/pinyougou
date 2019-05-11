package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.core.service.SalesService;
import entity.ChartResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sales")
public class SalesController {
    @Reference
    private SalesService salesService;
    @RequestMapping("/salesChart")
    public ChartResult salesChart(String start,String end){
        return salesService.salesChart(start,end);
    }
}
