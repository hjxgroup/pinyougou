package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hanjixin.core.dao.good.BrandDao;
import com.hanjixin.core.pojo.good.Brand;
import com.hanjixin.core.pojo.good.BrandQuery;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandDao brandDao;

    @Override
    public List<Brand> findAll() {
        return brandDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        //分页小助手
        PageHelper.startPage(pageNum, pageSize);
        //查询所有
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);
    }

    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKey(brand);
    }

    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        if(ids!=null&&ids.length>0){
/*            for (Long id : ids) {
                brandDao.deleteByPrimaryKey(id);
            }*/
            BrandQuery brandQuery = new BrandQuery();
            brandQuery.createCriteria().andIdIn(Arrays.asList(ids));
            brandDao.deleteByExample(brandQuery);
        }
    }

    @Override
    public PageResult search(Integer pageNum, Integer pageSize, Brand brand) {
        //分页小助手
        PageHelper.startPage(pageNum, pageSize);
        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        String strName=brand.getName();
        String strFirstChar=brand.getFirstChar();
        //品牌名称 模糊查询
        if(null!=strName&&!"".equals(strName.trim())){
            criteria.andNameLike("%"+strName.trim()+"%");
        }
        if(null!=strFirstChar&&!"".equals(strFirstChar.trim())){
            criteria.andFirstCharEqualTo(strFirstChar.trim());
        }
        if (brand.getStatus()!=null && !brand.getStatus().trim().equals("")){
            criteria.andStatusEqualTo(brand.getStatus().trim());
        }

        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(brandQuery);
        return new PageResult(page.getTotal(), page.getResult());

    }

    //品牌审核
    @Override
    public void updateBrandStatus(Long[] selectIds, String status) {
        if (selectIds.length>0){
            for (Long id : selectIds) {
                Brand brand = brandDao.selectByPrimaryKey(id);
                brand.setStatus(status);
                brandDao.updateByPrimaryKeySelective(brand);
            }
        }
    }

   /* @Override
    public List<Map> selectOptionList() {
        return brandDao.selectOptionList();
    }*/
}
