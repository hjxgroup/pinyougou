package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hanjixin.core.dao.item.ItemCatDao;
import com.hanjixin.core.pojo.item.ItemCat;
import com.hanjixin.core.pojo.item.ItemCatQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService{
    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private RedisTemplate redisTemplate;
    //根据父ID查询
    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        try {
            //查询所有商品分类 保存缓存中  商品分类管理页面上 添加新的按钮  导入Mysql商品分类数据到缓存中
            List<ItemCat> itemCatList=findAll();
            for (ItemCat itemCat : itemCatList) {
                redisTemplate.boundHashOps("itemCat").put(itemCat.getName(),itemCat.getTypeId());
            }
            ItemCatQuery itemCatQuery = new ItemCatQuery();
            itemCatQuery.createCriteria().andParentIdEqualTo(parentId);
            return itemCatDao.selectByExample(itemCatQuery);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    //查询一个
    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    //查询所有商品分类结果集
    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);
    }
}
