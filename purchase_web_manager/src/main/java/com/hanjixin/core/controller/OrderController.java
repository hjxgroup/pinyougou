package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.core.pojo.order.Order;
import com.hanjixin.core.pojo.item.ItemCat;
import com.hanjixin.core.pojo.order.Order;
import com.hanjixin.core.pojo.order.OrderItem;
import com.hanjixin.core.service.OrderService;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;


    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Order order){
        return orderService.search(page,rows,order);
    }
	/*//查询全部数量
	@RequestMapping("/findNum")
    public  double[] findNum(Integer year){
        return orderService.findNum(year);
    }
	//查询全部金额
	@RequestMapping("/findTotalMoney")
    public BigDecimal[] findTotalMoney(Integer year){
        return orderService.findTotalMoney(year);
    }*/
	@RequestMapping("/findByYear")
    public List<Order> findByYear(String year){
	    return orderService.findByYear(year);
    }
    @RequestMapping("/findNum")
    public List<Long> findNum(String year){
	    //创建一个集合
        List<Long> list = new ArrayList<>();
        //获取一年的数据  (集合)
        List<Order> byYear = orderService.findByYear(year);
        //循环十二个数字 索引0 为一月 依次累加
        for (int i = 0; i < 12; i++) {
            //count 累加器
            Long count=0L;
            //查询一年数据
            for (Order order : byYear) {
                //根据创建时间获取月份 符合条件 count+1
                if (order.getCreateTime().getMonth()==i){
                    count++;
                }
            }
            //给list索引为0的赋值数据
            list.add(i,count);
        }
        //返回集合
        return list;
    }

}
