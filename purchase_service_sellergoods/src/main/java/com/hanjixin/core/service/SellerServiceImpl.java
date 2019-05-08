package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hanjixin.core.dao.seller.SellerDao;
import com.hanjixin.core.pojo.seller.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 商家管理
 */
@Service
@Transactional
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerDao sellerDao;
    @Override
    public void add(Seller seller) {
        //加密密码
        seller.setPassword(new BCryptPasswordEncoder().encode(seller.getPassword()));
        //未审核
        seller.setStatus("0");
        //时间
        seller.setCreateTime(new Date());
        //保存
        sellerDao.insertSelective(seller);
    }

    @Override
    public Seller findOne(String username) {
        return sellerDao.selectByPrimaryKey(username);
    }
}
