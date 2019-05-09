package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hanjixin.core.dao.good.BrandDao;
import com.hanjixin.core.dao.good.GoodsDao;
import com.hanjixin.core.dao.good.GoodsDescDao;
import com.hanjixin.core.dao.item.ItemCatDao;
import com.hanjixin.core.dao.item.ItemDao;
import com.hanjixin.core.dao.seller.SellerDao;
import com.hanjixin.core.pojo.good.Brand;
import com.hanjixin.core.pojo.good.Goods;
import com.hanjixin.core.pojo.good.GoodsQuery;
import com.hanjixin.core.pojo.item.Item;
import com.hanjixin.core.pojo.item.ItemCat;
import com.hanjixin.core.pojo.item.ItemQuery;
import com.hanjixin.core.pojo.seller.Seller;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;
import vo.GoodsVo;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 商品管理
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsDescDao goodsDescDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private SellerDao sellerDao;
    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination topicPageAndSolrDestination;
    @Autowired
    private Destination queueSolrDeleteDestination;
    //商品添加  商品录入
    @Override
    public void add(GoodsVo vo) {
        //商品表
        //商品状态  未审核
        vo.getGoods().setAuditStatus("0");
        //逻辑删除   添加的商品不允许真删除 delete     假删除 update 是否删除的字段
        //添加  同时 返回商品ID
        goodsDao.insertSelective(vo.getGoods());
        //商品详情表
        //上面的商品ID 设置商品详情
        vo.getGoodsDesc().setGoodsId(vo.getGoods().getId());
        goodsDescDao.insertSelective(vo.getGoodsDesc());
        //判断是否启用规格
        if("1".equals(vo.getGoods().getIsEnableSpec())) {
            //启用
            //一堆库存表
            List<Item> itemList = vo.getItemList();
            for (Item item : itemList) {
                //标题  商品名称 + 空格 + 规格1 + 空格 + 规格 2 + 空格 规格 3
                String title = vo.getGoods().getGoodsName();
                // {"机身内存":"16G","网络":"联通3G"}
                String spec = item.getSpec();
                Map<String, String> specMap = JSON.parseObject(spec, Map.class);
                Set<Map.Entry<String, String>> entries = specMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    title += " " + entry.getValue();
                }
                //设置标题
                item.setTitle(title);
                //给库存对象设置属性
                setAttribute(item,vo);
                //保存库存表
                itemDao.insertSelective(item);
            }
        }
        else{
            //不启用 赠品 商品详情页面  没有规格选择  一个规格 颜色随机 默认库存数据
            Item item = new Item();
            //标题==商品名称
            item.setTitle(vo.getGoods().getGoodsName());
            //给库存对象设置属性
            setAttribute(item,vo);
            //价格  默认值
            item.setPrice(new BigDecimal(0));
            //库存 默认
            item.setNum(0);
            //默认启用
            item.setStatus("1");
            //默认是否
            //item.setIsDefault("0");
            //保存库存表
            itemDao.insertSelective(item);
        }
    }

    //查询分页结果对象  带条件  需求： 商家管理 查询商品要使用  运营商查询所有商家的商品也要使用
    @Override
    public PageResult search(Integer page, Integer rows, Goods goods) {
        //分页小助手
        PageHelper.startPage(page,rows);
        //排序
        //PageHelper.orderBy("id desc");
        //查询分页对象
        GoodsQuery goodsQuery = new GoodsQuery();
        //排序
        goodsQuery.setOrderByClause("id desc");
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        //判断状态
        if(goods.getAuditStatus()!=null&&!"".equals(goods.getAuditStatus().trim())){
            criteria.andAuditStatusEqualTo(goods.getAuditStatus().trim());
        }
        //判断商品名称 模糊查询
        if(null!=goods.getGoodsName()&&!"".equals(goods.getGoodsName().trim())){
            criteria.andGoodsNameLike("%"+goods.getGoodsName().trim()+"%");
        }
        //当前是运营商 查所有商家的商品
        //查询当前商家的商品结果集  当前登陆人的  必须得这么查
        if(null!=goods.getSellerId()){
            //商家后台管理查询分页结果集的条件
            criteria.andSellerIdEqualTo(goods.getSellerId());
        }
        criteria.andIsDeleteIsNull();
        Page<Goods> goodsList = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        return new PageResult(goodsList.getTotal(),goodsList.getResult());
    }

    @Override
    public GoodsVo findOne(Long id) {
        GoodsVo goodsVo = new GoodsVo();
        goodsVo.setGoods(goodsDao.selectByPrimaryKey(id));
        goodsVo.setGoodsDesc(goodsDescDao.selectByPrimaryKey(id));
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        goodsVo.setItemList(itemDao.selectByExample(itemQuery));
        return goodsVo;
    }

    @Override
    public void update(GoodsVo vo) {
        goodsDao.updateByPrimaryKeySelective(vo.getGoods());
        goodsDescDao.updateByPrimaryKeySelective(vo.getGoodsDesc());
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(vo.getGoods().getId());
        itemDao.deleteByExample(itemQuery);
        //2:再添加
        //判断是否启用规格
        if ("1".equals(vo.getGoods().getIsEnableSpec())) {
            //启用
            //一堆库存表
            List<Item> itemList = vo.getItemList();
            for (Item item : itemList) {
                //标题  商品名称 + 空格 + 规格1 + 空格 + 规格 2 + 空格 规格 3

                String title = vo.getGoods().getGoodsName();
                // {"机身内存":"16G","网络":"联通3G"}
                String spec = item.getSpec();
                Map<String, String> specMap = JSON.parseObject(spec, Map.class);

                Set<Map.Entry<String, String>> entries = specMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    title += " " + entry.getValue();
                }

                //设置标题
                item.setTitle(title);

                //给库存对象设置属性
                setAttribute(item, vo);

                //保存库存表
                itemDao.insertSelective(item);
            }

        } else {
            //不启用 赠品 商品详情页面  没有规格选择  一个规格 颜色随机 默认库存数据
            Item item = new Item();

            //标题==商品名称
            item.setTitle(vo.getGoods().getGoodsName());
            //给库存对象设置属性
            setAttribute(item, vo);
            //价格  默认值
            item.setPrice(new BigDecimal(0));
            //库存 默认
            item.setNum(0);
            //默认启用
            item.setStatus("1");
            //默认是否
            //item.setIsDefault("0");
            //保存库存表
            itemDao.insertSelective(item);
        }
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        //创建商品对象
        Goods goods = new Goods();
        //设置审核状态
        goods.setAuditStatus(status);
        for (Long id : ids) {
            //1:改商品的状态
            goods.setId(id);
            //更新状态
            goodsDao.updateByPrimaryKeySelective(goods);
            //判断是否为审核通过
            if("1".equals(status)){
                //发消息  商品ID 类型:TextMessage  硬编码问题 配置文件中去 properties中
                jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(String.valueOf(id));
                    }
                });
            }
        }
    }

    @Override
    public void delete(Long[] ids) {
        Goods goods = new Goods();
        //是否删除字段  1:为删除  null:不删除
        goods.setIsDelete("1");
        //遍历
        for (Long id : ids) {
            goods.setId(id);
            //1:逻辑删除  更新是否删除字段
            goodsDao.updateByPrimaryKeySelective(goods);
            jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(String.valueOf(id));
                }
            });
        }
    }



    //给库存对象设置属性
    public void setAttribute(Item item, GoodsVo vo) {
        //库存表只要一张图片  选取商品众多图片中的一张  首张
        //[{"color":"黄色","url":"http://192.168.200.128/group1/M00/00/01/wKjIgFq7QDWACj-5AAHTwjU3Qf4939.png"},{"url":"http://192.168.200.128/group1/M00/00/01/wKjIgFq7QESAdfWBAABJC2MBOwc105.jpg"}]
        String itemImages = vo.getGoodsDesc().getItemImages();
        List<Map> imagesList = JSON.parseArray(itemImages, Map.class);
        if (imagesList != null && imagesList.size() > 0) {
            item.setImage(String.valueOf(imagesList.get(0).get("url")));
        }
        //时间
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        //商品ID
        item.setGoodsId(vo.getGoods().getId());
        //第三级商品分类Id
        item.setCategoryid(vo.getGoods().getCategory3Id());
        //商家ID
        item.setSellerId(vo.getGoods().getSellerId());
        //商家名称
        Seller seller = sellerDao.selectByPrimaryKey(vo.getGoods().getSellerId());
        item.setSeller(seller.getNickName());
        //第三级分类的名称
        ItemCat itemCat = itemCatDao.selectByPrimaryKey(vo.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());
        //品牌名称
        Brand brand = brandDao.selectByPrimaryKey(vo.getGoods().getBrandId());
        item.setBrand(brand.getName());
    }
}
