package com.hanjixin.core.dao.order;

import com.hanjixin.core.pojo.order.Order;
import com.hanjixin.core.pojo.order.OrderItem;
import com.hanjixin.core.pojo.order.OrderQuery;
import entity.MorrisData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDao {
    int countByExample(OrderQuery example);

    int deleteByExample(OrderQuery example);

    int deleteByPrimaryKey(Long orderId);

    int insert(Order record);

    int insertSelective(Order record);

    List<Order> selectByExample(OrderQuery example);

    OrderItem selectByPrimaryKey(Long orderId);

    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderQuery example);

    int updateByExample(@Param("record") Order record, @Param("example") OrderQuery example);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<MorrisData> selectByMonth(String start, String end);
}