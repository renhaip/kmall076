package com.kgc.kmall.service;

import com.kgc.kmall.bean.OmsOrder;

/**
 * @author shkstart
 * @create 2020-10-21 15:31
 */
public interface OrderService {


    String genTradeCode(Long memberId);

    String checkTradeCode(Long memberId, String tradeCode);

    void saveOrder(OmsOrder omsOrder);


    OmsOrder getOrderByOutTradeNo(String outTradeNo);
}
