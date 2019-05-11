package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hanjixin.core.dao.order.OrderDao;
import entity.ChartResult;
import entity.MorrisData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalesServiceImpl implements SalesService {
    @Autowired
    private OrderDao orderDao;

    @Override
    public List<MorrisData> salesPre(String start, String end, String username) {
        List<MorrisData> data=null;
        try {
            data = orderDao.selectByMonth(start,end,username);
            return data;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    public ChartResult salesChart(String start, String end) {
        ChartResult result = new ChartResult();
        List<Map<String, String>> maps = orderDao.selectGroupMonth(start, end);
        result.setSeriesData(maps);
        Map<String, String> selectedMap = new HashMap<>();
        for (int i = 0; i < maps.size(); i++) {
            if(i<6) {
                selectedMap.put(maps.get(i).get("name"), "true");
            }else {
                selectedMap.put(maps.get(i).get("name"), "false");
            }
        }
        result.setSelected(selectedMap);
        return result;
    }

}
