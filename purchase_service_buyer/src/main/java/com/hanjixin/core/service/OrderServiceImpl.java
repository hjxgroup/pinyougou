package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hanjixin.common.utils.IdWorker;
import com.hanjixin.core.dao.item.ItemDao;
import com.hanjixin.core.dao.log.PayLogDao;
import com.hanjixin.core.dao.order.OrderDao;
import com.hanjixin.core.dao.order.OrderItemDao;
import com.hanjixin.core.pojo.item.Item;
import com.hanjixin.core.pojo.log.PayLog;
import com.hanjixin.core.pojo.order.Order;
import com.hanjixin.core.pojo.order.OrderItem;
import com.hanjixin.core.pojo.order.OrderQuery;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import vo.Cart;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private PayLogDao payLogDao;
    @Override
    public void add(Order order) {
        //1:保存购物车 每一个购物车 商家为单位  一个购物车对应一个商家 对应一个订单
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("CART").get(order.getUserId());
        //实付金额 日志表的金额 (总金额)
        long tp=0;
        //订单集合
        ArrayList<String> orderIdList = new ArrayList<>();
        for (Cart cart : cartList) {
            //订单ID 唯一
            long orderID = idWorker.nextId();
            order.setOrderId(orderID);
            orderIdList.add(String.valueOf(orderID));
            //总金额
            double totalPrice=0;
            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                //订单详情对象 OrderItem

                //订单详情ID
                orderItem.setId(idWorker.nextId());
                //根据库存ID 查询库存对象
                Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                //商品ID
                orderItem.setGoodsId(item.getGoodsId());
                //订单ID 外键
                orderItem.setOrderId(orderID);
                //标题
                orderItem.setTitle(item.getTitle());
                //单价
                orderItem.setPrice(item.getPrice());
                //小计
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()));
                //计算此订单的总金额
                totalPrice+=orderItem.getTotalFee().doubleValue();
                //图片
                orderItem.setPicPath(item.getImage());
                //商家ID
                orderItem.setSellerId(item.getSellerId());
                //保存订单详情表
                orderItemDao.insertSelective(orderItem);
            }
            //实付金额
            order.setPayment(new BigDecimal(totalPrice));
            //未付款
            order.setStatus("1");
            //创建时间
            order.setCreateTime(new Date());
            //更新时间
            order.setUpdateTime(new Date());
            //来源
            order.setSourceType("2");
            //商家ID
            order.setSellerId(cart.getSellerId());
            tp+=order.getPayment().doubleValue()*100;
            //保存订单
            orderDao.insertSelective(order);
        }
        //保存日志表  (支付表)
        PayLog payLog = new PayLog();
        //ID
        payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));
        //生成时间
        payLog.setCreateTime(new Date());
        //总金额 上面所有订单的金额
        payLog.setTotalFee(tp);
        //用户Id
        payLog.setUserId(order.getUserId());
        //交易状态
        payLog.setTradeState("0");
        //支付类型
        payLog.setPayType("1");
        //订单集合   "1,2,3,4,5"
        payLog.setOrderList(orderIdList.toString().replace("[","").replace("]",""));
        //保存
        payLogDao.insertSelective(payLog);
        //保存缓存一份
        redisTemplate.boundHashOps("payLog").put(order.getUserId(),payLog);
        //清除购物车
        //redisTemplate.boundHashOps("CART").delete(order.getUserId());
        //删除购物车中已经购买的商品
    }



    @Override
    public PageResult search(Integer page, Integer rows, Order order) {
        PageHelper.startPage(page,rows);
        OrderQuery orderQuery = new OrderQuery();

        OrderQuery.Criteria criteria =orderQuery.createCriteria();
        //模糊查询
        if (null !=order.getUserId() && !"".equals(order.getUserId().trim())){
            criteria.andUserIdLike("%" +order.getUserId().trim()+"%");
        }

//        if (order!=null&&order.getSellerId()!=null){
//            orderQuery.createCriteria().andSellerIdEqualTo(order.getSellerId());
//        }
        Page<Order> orderList= (Page<Order>) orderDao.selectByExample(orderQuery);
        return new PageResult(orderList.getTotal(),orderList.getResult());
    }

    @Override
    public List<Order> findByYear(String year) {
        try {
            //根据年份查询全部数据
            String dateStr = year + "-01-01 00:00";
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr);

            OrderQuery orderQuery = new OrderQuery();
            orderQuery.createCriteria().andCreateTimeGreaterThan(date);
            List<Order> orders = orderDao.selectByExample(orderQuery);
            return orders;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  double[] findNum(Integer year) {
        //根据年份查询全部数据
        String date = year + "-01-01 00:00";
        return null;
    }

}
