package com.hanjixin.core.listener;

import com.hanjixin.core.service.StaticPageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class PageListener implements MessageListener {
    @Autowired
    private StaticPageService staticPageService;
    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;
        try {
            String goodsId = atm.getText();
            System.out.println("静态化项目接收到的商品ID:" + goodsId);
            //3: 将商品进行静态化处理   暂定    静态化页面 磁盘上
            staticPageService.index(Long.parseLong(goodsId));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
