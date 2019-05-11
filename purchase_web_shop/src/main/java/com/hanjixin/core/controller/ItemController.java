package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.core.pojo.item.Item;
import com.hanjixin.core.pojo.seckill.SeckillGoods;
import com.hanjixin.core.service.ItemService;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Reference
    ItemService itemService;
    @RequestMapping("/findItemByGoodsId")
    public List<Item> findItemByGoodsId(Long id){
        return itemService.findItemByGoodsId(id);
    }
    @RequestMapping("/addSecKill")
    public Result addSecKill(Long[] ids,@RequestBody SeckillGoods seckillGoods){
        try {
            itemService.addSecKill(ids,seckillGoods);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }

    }
}

