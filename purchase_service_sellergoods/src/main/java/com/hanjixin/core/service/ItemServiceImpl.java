package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hanjixin.core.dao.item.ItemDao;
import com.hanjixin.core.dao.seckill.SeckillGoodsDao;
import com.hanjixin.core.pojo.item.Item;
import com.hanjixin.core.pojo.item.ItemQuery;
import com.hanjixin.core.pojo.seckill.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemDao itemDao;
    @Autowired
    SeckillGoodsDao seckillGoodsDao;

    @Override
    public List<Item> findItemByGoodsId(Long id) {
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        List<Item> items = itemDao.selectByExample(itemQuery);
        return items;
    }
    //添加秒杀

    @Override
    public void addSecKill(Long[] ids, SeckillGoods seckillGoods) {
        for (Long id : ids) {
            Item item = itemDao.selectByPrimaryKey(id);
            //价格 标题 图片 商品id 商家id
            seckillGoods.setItemId(item.getId());
            seckillGoods.setTitle(item.getTitle());
            seckillGoods.setSmallPic(item.getImage());
            seckillGoods.setGoodsId(item.getGoodsId());
            seckillGoods.setPrice(item.getPrice());
            seckillGoods.setSellerId(item.getSellerId());
            //状态未审批
            seckillGoods.setStatus("0");
            //剩余库存
            seckillGoods.setStockCount(item.getNum());
            seckillGoods.setCreateTime(new Date());
            int i = seckillGoodsDao.insertSelective(seckillGoods);
            System.out.println(i);
        }
    }
}
