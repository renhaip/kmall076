package com.kgc.kmall.service;

import com.kgc.kmall.bean.PaymentInfo;

import java.util.Map;

/**
 * @author shkstart
 * @create 2020-10-23 15:37
 */
public interface PaymentService {
    void savePaymentInfo(PaymentInfo paymentInfo);

    void updatePayment(PaymentInfo paymentInfo);

    void sendDelayPaymentResultCheckQueue(String outTradeNo, int i);

   public Map<String,Object> checkAlipayPayment(String out_trade_no);

}
