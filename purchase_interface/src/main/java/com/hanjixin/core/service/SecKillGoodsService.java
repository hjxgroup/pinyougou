package com.hanjixin.core.service;

import com.hanjixin.core.pojo.seckill.SeckillGoods;

import java.util.List;

public interface SecKillGoodsService {
    /**
     * 返回当前正在参与秒杀的商品
     * @return
     */
    public List<SeckillGoods> findList();

    public void updateStaus(Long[] ids,String status);

    List<SeckillGoods> findCheckList();
}

