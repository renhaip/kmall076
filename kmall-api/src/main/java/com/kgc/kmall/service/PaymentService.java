package com.kgc.kmall.service;

import com.kgc.kmall.bean.PaymentInfo;

/**
 * @author shkstart
 * @create 2020-10-23 15:37
 */
public interface PaymentService {
    void savePaymentInfo(PaymentInfo paymentInfo);

    void updatePayment(PaymentInfo paymentInfo);
}
