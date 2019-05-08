package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.common.utils.PhoneFormatCheckUtils;
import com.hanjixin.core.pojo.user.User;
import com.hanjixin.core.service.UserService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.PatternSyntaxException;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    //发送验证码
    @RequestMapping("/sendCode")
    public Result sendCode(String phone){
        try {
            //手机是否合法  正则表达式
            if(PhoneFormatCheckUtils.isPhoneLegal(phone)){
                userService.sendCode(phone);
            }
            else{
                //不合法
                return new Result(false, "手机号不正确");
            }
            return new Result(true, "发送成功");
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
            return new Result(false, "发送失败");
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody User user,String smscode){
        try {
            userService.add(user,smscode);
            return new Result(true,"注册成功");
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"注册失败");
        }
    }
}
