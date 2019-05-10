package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.common.utils.ExportPOIUtils;
import com.hanjixin.core.pojo.seller.Seller;
import com.hanjixin.core.service.SellerService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/seller")
public class SellerController {
        @Reference
        private SellerService sellerService;
    @Value("${files}")
    private String files;
    @Value("${columnName}")
    private String columnName;
    @Value("${key}")
    private String key;
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
            String fileName = files + UUID.randomUUID().toString().replaceAll("-","") + ".xls";
            String[] columnNames = columnName.split(",");
            String[] keys = key.split(",");
            ExportPOIUtils.start_download(response, fileName, sellerList, columnNames, keys);
            return new Result(true,"数据导出成功!!");
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,"数据导出失败!!");
        }

    }


}




