package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hanjixin.core.dao.seller.SellerDao;
import com.hanjixin.core.pojo.seller.Seller;
import com.hanjixin.core.pojo.seller.SellerQuery;
import entity.PageResult;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


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

    //查询所有   带分页 待条件
    @Override
    public PageResult search(Integer page, Integer rows, Seller seller) {
        PageHelper.startPage(page,rows);
        SellerQuery query = new SellerQuery();
        SellerQuery.Criteria criteria = query.createCriteria();
        if (seller.getName()!=null &&!seller.getName().trim().equals("")){
            criteria.andNameLike("%"+seller.getName().trim()+"%");
        }
        if (seller.getNickName()!=null && !seller.getNickName().trim().equals("")){
            criteria.andNickNameLike("%"+seller.getNickName()+"%");
        }
        if (seller.getStatus()!=null && !seller.getStatus().trim().equals("")){
            criteria.andStatusEqualTo(seller.getStatus());
        }

        Page<Seller> sellers = (Page<Seller>) sellerDao.selectByExample(query);
        return new PageResult(sellers.getTotal(),sellers.getResult());
    }
    //店铺数据的批量导出
    @Override
    public List<Seller> dataExport() {
        return sellerDao.selectByExample(null);
    }





}
