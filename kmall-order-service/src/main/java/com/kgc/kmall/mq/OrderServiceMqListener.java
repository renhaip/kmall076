package com.kgc.kmall.mq;

import com.kgc.kmall.bean.OmsOrder;
import com.kgc.kmall.service.OrderService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;

/**
 * @author shkstart
 * @create 2020-10-26 18:40
 */
@Component
public class OrderServiceMqListener  {
    @Resource
    OrderService orderService;

    @JmsListener(destination = "PAYHMENT_SUCCESS_QUEUE",containerFactory = "jmsQueueListener")
    public void consumePaymentResult(MapMessage mapMessage) throws JMSException {

        String out_trade_no = mapMessage.getString("out_trade_no");

        // 更新订单状态业务
        System.out.println(out_trade_no);

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(out_trade_no);
        orderService.updateOrder(omsOrder);
    }
}
