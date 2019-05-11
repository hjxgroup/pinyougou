package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.core.pojo.user.User;
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

}
