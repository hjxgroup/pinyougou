package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hanjixin.core.dao.specification.SpecificationDao;
import com.hanjixin.core.dao.specification.SpecificationOptionDao;
import com.hanjixin.core.pojo.specification.Specification;
import com.hanjixin.core.pojo.specification.SpecificationOption;
import com.hanjixin.core.pojo.specification.SpecificationOptionQuery;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vo.SpecificationVo;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService{
    @Autowired
    private SpecificationDao specificationDao;

    @Autowired
    private SpecificationOptionDao specificationOptionDao;
    @Override
    public PageResult search(Integer page, Integer rows, Specification specification) {
        //分页插件
        PageHelper.startPage(page,rows);
        //查询分页对象
        Page<Specification> p=(Page<Specification>) specificationDao.selectByExample(null);
        return new PageResult(p.getTotal(),p.getResult());
    }

    @Override
    public void add(SpecificationVo vo) {
        //规格表
        specificationDao.insertSelective(vo.getSpecification());
        //规格选项表
        List<SpecificationOption> specificationOptionList = vo.getSpecificationOptionList();
        for (SpecificationOption specificationOption : specificationOptionList) {
            specificationOption.setSpecId(vo.getSpecification().getId());
            specificationOptionDao.insertSelective(specificationOption);
        }
    }

    @Override
    public SpecificationVo findOne(Long id) {
        SpecificationVo vo = new SpecificationVo();
        //规格对象
        vo.setSpecification(specificationDao.selectByPrimaryKey(id));
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = query.createCriteria();
        criteria.andSpecIdEqualTo(id);
        vo.setSpecificationOptionList(specificationOptionDao.selectByExample(query));
        //规格选项结果集对象
        return vo;
    }

    @Override
    public void update(SpecificationVo vo) {
        //规格
        specificationDao.updateByPrimaryKeySelective(vo.getSpecification());
        //规格选项
        //s删除
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = query.createCriteria();
        criteria.andSpecIdEqualTo(vo.getSpecification().getId());
        specificationOptionDao.deleteByExample(query);
        //添加
        List<SpecificationOption> specificationOptionList = vo.getSpecificationOptionList();
        for (SpecificationOption specificationOption : specificationOptionList) {
            specificationOption.setSpecId(vo.getSpecification().getId());
            specificationOptionDao.insertSelective(specificationOption);
        }
    }

    @Override
    public List<Map> selectOptionList() {
        return specificationDao.selectOptionList();
    }
}
