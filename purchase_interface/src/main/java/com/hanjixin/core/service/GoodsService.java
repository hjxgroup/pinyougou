package com.hanjixin.core.service;

import com.hanjixin.core.pojo.good.Goods;
import entity.PageResult;
import vo.GoodsVo;

import java.io.OutputStream;

public interface GoodsService {
    void add(GoodsVo vo);

    PageResult search(Integer page, Integer rows, Goods goods);

    GoodsVo findOne(Long id);

    void update(GoodsVo vo);

    void updateStatus(Long[] ids, String status);

    void delete(Long[] ids);

}
