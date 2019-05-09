package com.hanjixin.core.service;

import com.hanjixin.core.pojo.good.Brand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

public interface BrandService {
    List<Brand> findAll();

    //商品分类分页查询
    public PageResult findPage(Integer pageNum, Integer pageSize);

    void add(Brand brand);

    void update(Brand brand);

    Brand findOne(Long id);

    void delete(Long[] ids);

    PageResult search(Integer pageNum, Integer pageSize, Brand brand);

    void updateBrandStatus(Long[] selectIds, String status);

    /* List<Map> selectOptionList();*/
}
