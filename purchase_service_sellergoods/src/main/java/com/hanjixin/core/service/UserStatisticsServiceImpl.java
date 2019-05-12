package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hanjixin.core.dao.user.UserDao;
import com.hanjixin.core.pojo.user.User;
import com.hanjixin.core.pojo.user.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserStatisticsServiceImpl implements UserStatisticsService {
    @Autowired
    private UserDao userDao;




    @Override
    public List<String> findAll(String start, String end, String statusValue) {

        String data = "";
        List<Map<String, Object>> mapList = userDao.findAllByDate(start, end, statusValue);
        List<String> strings=new ArrayList<>();
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> map = mapList.get(i);
            String[] item=new String[2];
            item[0]=String.valueOf(map.get("createtime"));
            item[1]=String.valueOf((Long) map.get("COUNT"));
            strings.add(JSON.toJSONString(item));
        }
        return strings;
    }
}
