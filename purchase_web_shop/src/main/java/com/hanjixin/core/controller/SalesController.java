package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.core.service.SalesService;
import entity.MorrisData;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController {
    @Reference
    private SalesService salesService;
    @RequestMapping("/salespre")
    public List<MorrisData> salesPre(String startdata,String enddata){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return salesService.salesPre(startdata,enddata,name);
    }
}
