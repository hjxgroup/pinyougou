package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hanjixin.core.dao.good.GoodsDao;
import com.hanjixin.core.dao.good.GoodsDescDao;
import com.hanjixin.core.dao.item.ItemCatDao;
import com.hanjixin.core.dao.item.ItemDao;
import com.hanjixin.core.pojo.good.Goods;
import com.hanjixin.core.pojo.good.GoodsDesc;
import com.hanjixin.core.pojo.item.Item;
import com.hanjixin.core.pojo.item.ItemQuery;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 静态化处理实现类
 */
@Service
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware {
    @Autowired
    private GoodsDescDao goodsDescDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    //静态化程序方法  人为调用  定时器
    // 入参: 商品ID
    public void index(Long id){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        String path=getPath("/"+id+".html");
        Writer out=null;
        try {
            Template template = configuration.getTemplate("item.ftl");
            //数据
            Map<String, Object> root = new HashMap<>();
            //商品详情对象
            GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
            root.put("goodsDesc",goodsDesc);
            //商品对象  一级 二级 三级的商品分类的ID
            Goods goods = goodsDao.selectByPrimaryKey(id);
            root.put("goods",goods);
            //一级商品分类 名称
            root.put("itemCat1",itemCatDao.selectByPrimaryKey(goods.getCategory1Id()).getName());
            //二级商品分类 名称
            root.put("itemCat2",itemCatDao.selectByPrimaryKey(goods.getCategory2Id()).getName());
            //三级商品分类 名称
            root.put("itemCat3",itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());
            ItemQuery itemQuery = new ItemQuery();
            itemQuery.createCriteria().andGoodsIdEqualTo(id).andIsDefaultEqualTo("1");
            List<Item> itemList = itemDao.selectByExample(itemQuery);
            root.put("itemList",itemList);
            //输出流
            out= new OutputStreamWriter(new FileOutputStream(path),"UTF-8");
            template.process(root,out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPath(String path){
        return servletContext.getRealPath(path);
    }

    private ServletContext servletContext;
    //当前Spring容器  上下文
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext=servletContext;
    }
}
