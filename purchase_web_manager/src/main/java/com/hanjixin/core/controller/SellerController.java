package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.common.utils.ExportPOIUtils;
import com.hanjixin.core.pojo.seller.Seller;
import com.hanjixin.core.service.SellerService;
import entity.PageResult;
import entity.Result;
import org.apache.poi.hssf.eventmodel.ERFListener;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;


@RestController
@RequestMapping("/seller")
public class SellerController {
        @Reference
        private SellerService sellerService;

    //查询 带分页 待条件
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Seller seller){
        return sellerService.search(page,rows,seller);
    }
    //批量导出数据
    @RequestMapping("/dataExport")
    public Result dataExport(HttpServletResponse response) {

        try {
            List<Seller> sellerList = sellerService.dataExport();
            String fileName = "商家详细信息" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") + ".xls";
            String[] columnNames = {"用户ID", "公司名称", "店铺名称", "email", "公司手机", "公司电话", "状态", "详细地址", "联系人姓名"};
            String[] keys = {"sellerId","name","nickName","email","mobile","telephone","status","addressDetail","linkmanName"};
            ExportPOIUtils.start_download(response, fileName, sellerList, columnNames, keys);
            return new Result(true,"数据导出成功!!");
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,"数据导出失败!!");
        }

    }


}




