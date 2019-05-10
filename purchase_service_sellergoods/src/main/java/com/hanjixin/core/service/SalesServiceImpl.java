package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hanjixin.core.dao.order.OrderDao;
import com.hanjixin.core.pojo.order.OrderQuery;
import entity.MorrisData;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {
    @Autowired
    private OrderDao orderDao;

    @Override
    public List<MorrisData> salesPre(String start, String end, String username) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            OrderQuery orderQuery = new OrderQuery();

            if (username != null) {
                orderQuery.createCriteria().andSellerIdEqualTo(username);
            }
            List<MorrisData> data = orderDao.selectByMonth(start,end);
            return orderDao.selectByMonth(start,end);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
