package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hanjixin.core.dao.specification.SpecificationOptionDao;
import com.hanjixin.core.dao.template.TypeTemplateDao;
import com.hanjixin.core.pojo.specification.SpecificationOption;
import com.hanjixin.core.pojo.specification.SpecificationOptionQuery;
import com.hanjixin.core.pojo.template.TypeTemplate;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {
    @Autowired
    private TypeTemplateDao typeTemplateDao;
    @Autowired
    private SpecificationOptionDao specificationOptionDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate) {
        //查询所有模板结果集
        List<TypeTemplate> typeTemplateList = typeTemplateDao.selectByExample(null);
        for (TypeTemplate template : typeTemplateList) {
            //品牌结果集  [{"id":35,"text":"牛栏山"},{"id":36,"text":"剑南春"},{"id":39,"text":"口子窑"}]
            List<Map> brandList = JSON.parseArray(template.getBrandIds(), Map.class);
            redisTemplate.boundHashOps("brandList").put(template.getId(),brandList);
            List<Map> specList = findBySpecList(template.getId());
            redisTemplate.boundHashOps("specList").put(template.getId(),specList);
        }
        //分页小助手
        PageHelper.startPage(page,rows);
        PageHelper.orderBy("id desc");
        Page<TypeTemplate> p= (Page<TypeTemplate>) typeTemplateDao.selectByExample(null);
        return new PageResult(p.getTotal(),p.getResult());
    }

    @Override
    public void add(TypeTemplate typeTemplate) {
        typeTemplateDao.insertSelective(typeTemplate);
    }

    @Override
    public TypeTemplate findOne(Long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(TypeTemplate typeTemplate) {
        typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
    }

    //根据模板ID 查询规格结果集 List<Map> 每个Map里还有结果集
    @Override
    public List<Map> findBySpecList(Long id) {
        //1：根据模板ID 查询模板对象
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        //2:模板对象中规格字符串  [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = typeTemplate.getSpecIds();
        //3:将上面的Json格式字符串转成List<Map>
        List<Map> specIdList = JSON.parseArray(specIds, Map.class);
        for (Map map : specIdList) {
//            id    :   27
//            text : 网络
//            options : 根据27查询规格选项结果集
            SpecificationOptionQuery query = new SpecificationOptionQuery();
            //map.get("id")  Object --> 简单类型 Integer String  -->     长整  Long   类型转换异常
            // query.createCriteria().andSpecIdEqualTo((long)(Integer)(map.get("id")));
            query.createCriteria().andSpecIdEqualTo(Long.parseLong(String.valueOf(map.get("id"))));
            List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(query);
            map.put("options",specificationOptions);
        }
        return specIdList;
    }
}
