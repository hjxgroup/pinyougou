package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hanjixin.core.dao.seckill.SeckillGoodsDao;
import com.hanjixin.core.pojo.seckill.SeckillGoods;
import com.hanjixin.core.pojo.seckill.SeckillGoodsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class SecKillGoodsServiceImpl implements SecKillGoodsService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    @Override
    public List<SeckillGoods> findList() {
        //获取秒杀商品列表
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods").values();
        if(seckillGoodsList==null || seckillGoodsList.size()==0){

            SeckillGoodsQuery example=new SeckillGoodsQuery();
            SeckillGoodsQuery.Criteria criteria = example.createCriteria();
            // Criteria criteria = example.createCriteria();
            criteria.andStatusEqualTo("1");//审核通过
//            criteria.andStockCountGreaterThan(0);//剩余库存大于0
//            criteria.andStartTimeLessThanOrEqualTo(new Date());//开始时间小于等于当前时间
//            criteria.andEndTimeGreaterThan(new Date());//结束时间大于当前时间
            seckillGoodsList= seckillGoodsDao.selectByExample(example);
            //将商品列表装入缓存
            System.out.println("将秒杀商品列表装入缓存");
            for(SeckillGoods seckillGoods:seckillGoodsList){
                redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getId(), seckillGoods);
            }
        }
        return seckillGoodsList;
    }
    public void updateStaus(Long[] ids,String status){
        //创建秒杀对象
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setStatus(status);
        for (Long id : ids) {
            seckillGoods.setId(id);
            seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
            if (status.equals("1")) {
                SeckillGoods seckillGoods1 = seckillGoodsDao.selectByPrimaryKey(id);
                redisTemplate.boundHashOps("seckillGoods").put(seckillGoods1.getId(), seckillGoods1);
            }
        }

    }
    @Override
    public List<SeckillGoods> findCheckList() {

            SeckillGoodsQuery example=new SeckillGoodsQuery();
            SeckillGoodsQuery.Criteria criteria = example.createCriteria();

            criteria.andStatusEqualTo("0");//未审核
//            criteria.andStockCountGreaterThan(0);//剩余库存大于0
//            criteria.andStartTimeLessThanOrEqualTo(new Date());//开始时间小于等于当前时间
//            criteria.andEndTimeGreaterThan(new Date());//结束时间大于当前时间
        List<SeckillGoods> seckillGoods1 = seckillGoodsDao.selectByExample(example);
        return seckillGoods1;
    }


}
