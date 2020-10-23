package com.kgc.kmall.kmallpaymentservice.service;

import com.kgc.kmall.kmallpaymentservice.mapper.PaymentInfoMapper;
import com.kgc.kmall.bean.PaymentInfo;
import com.kgc.kmall.bean.PaymentInfoExample;
import com.kgc.kmall.service.PaymentService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

/**
 * @author shkstart
 * @create 2020-10-23 15:40
 */
@Service
public class PaymentServiceImpl  implements PaymentService {

    @Resource
    PaymentInfoMapper paymentInfoMapper;
    @Override
    public void savePaymentInfo(PaymentInfo paymentInfo) {
        paymentInfoMapper.insertSelective(paymentInfo);
    }

    @Override
    public void updatePayment(PaymentInfo paymentInfo) {
        PaymentInfoExample example=new PaymentInfoExample();
        example.createCriteria().andOrderSnEqualTo(paymentInfo.getOrderSn());
        paymentInfoMapper.updateByExampleSelective(paymentInfo,example);
    }
}
