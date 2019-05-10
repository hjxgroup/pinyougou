package com.hanjixin.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hanjixin.common.utils.FileUtils;
import com.hanjixin.core.pojo.good.Brand;
import com.hanjixin.core.service.BrandService;
import entity.Result;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/excel")
public class ExcelImportController implements ServletContextAware {
    @Reference
    private BrandService brandService;

    @RequestMapping("/importBrandExcel")
    public Result importBrand(MultipartFile file) {


        try {
            //创建工作簿
            XSSFWorkbook wk = new XSSFWorkbook(file.getInputStream());
            //获取第一张Sheet表
            XSSFSheet sheet = wk.getSheetAt(0);
            //创建品牌对象集合
            ArrayList<Brand> brands = new ArrayList<>();
            //遍历sheet
            for (Row row : sheet) {
                int rowNum = row.getRowNum();
                if (rowNum == 0) {
                    continue;
                }
                Brand brand = new Brand();
                if (row.getCell(0) != null) {
                    brand.setName(row.getCell(0).getStringCellValue().trim());
                }
                if (row.getCell(1) != null) {
                    brand.setFirstChar(row.getCell(1).getStringCellValue().trim().toUpperCase());
                }
                if (row.getCell(2) != null) {
                    row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                    //stuUser.setPhone(row.getCell(0).getStringCellValue());
                    //brand.setStatus(String.valueOf((int) row.getCell(2).getNumericCellValue()));
                    brand.setStatus(row.getCell(2).getStringCellValue());
                }
                brands.add(brand);
            }
            brandService.saveBeans(brands);
            return new Result(true, "数据导入成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "数据导入失败");
        }
    }

    @Value("${brandTemaplateName}")
    private String brandTemaplateName;

    @Value("${brandTemplatePath}")
    private String brandTemplatePath;

    @RequestMapping("/brandTemplateDownload")
    public void brandTemplateDownload(HttpServletRequest request, HttpServletResponse response) {
        FileInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            String path = servletContext.getRealPath(brandTemplatePath);
            String mimeType = servletContext.getMimeType(brandTemaplateName);
            response.setContentType(mimeType);
            String agent = request.getHeader("User-Agent");
            response.setHeader("Content-Disposition", "attachment;filename=" + FileUtils.encodeDownloadFilename(brandTemaplateName,agent ));
            byte[] bytes = new byte[2048];
            int len;
            inputStream = new FileInputStream(path);
            outputStream = new BufferedOutputStream(response.getOutputStream());
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
