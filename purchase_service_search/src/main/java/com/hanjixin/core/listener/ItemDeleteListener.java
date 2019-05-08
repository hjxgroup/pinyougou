package com.hanjixin.core.listener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ItemDeleteListener implements MessageListener {
    @Autowired
    private SolrTemplate solrTemplate;
    @Override
    public void onMessage(Message message) {
        //接口  实现类
        //有接口 获取此接口的实现类
        //向下强转
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;
        try {
            String goodsId= atm.getText();
            System.out.println("搜索项目(删除时)中获取到的商品ID:" + goodsId);
            //TODO 2:删除索引库
            SolrDataQuery solrDataQuery = new SimpleQuery("item_goodsid:" + goodsId);
            solrTemplate.delete(solrDataQuery);
            solrTemplate.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
