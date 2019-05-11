package com.hanjixin.core.service;

import com.hanjixin.core.pojo.item.Item;
import com.hanjixin.core.pojo.seckill.SeckillGoods;

import java.util.List;

public interface ItemService {
     List<Item> findItemByGoodsId(Long id);

     void addSecKill(Long[] ids, SeckillGoods seckillGoods);
}
