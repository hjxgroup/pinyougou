package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.core.pojo.user.User;
import com.hanjixin.core.service.OrderService;
import com.hanjixin.core.service.UserStatisticsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user_statistics")
public class UserStatisticsController {

    @Reference
    private UserStatisticsService userStatisticsService;
    @Reference
    private OrderService orderService;

    @RequestMapping("/findAll")
    public List<String> findAll(String start, String end, String statusValue){
        List<String> data=null;
        if(statusValue.equals("3")){
            data= userStatisticsService.findAll(start,end,null);
        }else {
            data= userStatisticsService.findAll(start,end,statusValue);
        }

        return data;
    }

    @RequestMapping("/findAllByOrder")
    public List<String> findAllByOrder(String start, String end, String user_id){
        List<String> data = new ArrayList<>();
        return orderService.findAllByOrder(start,end,user_id);

    }

}
