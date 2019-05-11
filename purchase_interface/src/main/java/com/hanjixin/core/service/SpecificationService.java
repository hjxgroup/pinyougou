package com.hanjixin.core.service;

import com.hanjixin.core.pojo.specification.Specification;
import entity.PageResult;
import vo.SpecificationVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface SpecificationService {
    PageResult search(Integer page, Integer rows, Specification specification);

    void add(SpecificationVo vo);

    SpecificationVo findOne(Long id);

    void update(SpecificationVo vo);

    List<Map> selectOptionList();

    void saveBeans(ArrayList<Specification> specifications);
}
