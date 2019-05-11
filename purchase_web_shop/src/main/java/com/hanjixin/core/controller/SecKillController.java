package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.core.pojo.seckill.SeckillGoods;
import com.hanjixin.core.service.SecKillGoodsService;
import entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckillGoods")
public class SecKillController {
    /**
     * 当前秒杀的商品
     * @return
     */
    @Reference
    SecKillGoodsService secKillGoodsService;
    @RequestMapping("/findList")
    public List<SeckillGoods> findList(){
        return secKillGoodsService.findList();
    }

    @RequestMapping("/updateStaus")
    public Result updateStaus(Long[] ids, String status){
        try {
            secKillGoodsService.updateStaus(ids,status);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

}
