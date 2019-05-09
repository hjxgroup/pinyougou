package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.core.pojo.user.User;
import com.hanjixin.core.service.UserService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;


    @RequestMapping("/search")
    public PageResult search(Integer page,Integer rows,@RequestBody User user){
        return userService.search(page,rows,user);

    }

    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids,Integer status ){

        try {
            userService.updateStatus(ids,status);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }

}
