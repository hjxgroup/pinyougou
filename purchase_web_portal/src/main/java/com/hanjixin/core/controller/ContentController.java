package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.core.pojo.ad.Content;
import com.hanjixin.core.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference
    private ContentService contentService;
    //根据广告类型Id 查询所有广告
    @RequestMapping("/findByCategoryId")
    public List<Content> findByCategoryId(Long categoryId){
       return contentService.findByCategoryId(categoryId);
    }
}
