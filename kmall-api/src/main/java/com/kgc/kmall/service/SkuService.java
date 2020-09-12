package com.kgc.kmall.service;

import com.kgc.kmall.bean.PmsSkuInfo;

/**
 * @author shkstart
 * @create 2020-09-04 15:38
 */
public interface SkuService {

    public Integer saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo selectBySkuId(Long skuId);
}
