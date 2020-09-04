package com.kgc.kmall.service;

import com.kgc.kmall.bean.PmsBaseSaleAttr;
import com.kgc.kmall.bean.PmsProductImage;
import com.kgc.kmall.bean.PmsProductInfo;
import com.kgc.kmall.bean.PmsProductSaleAttr;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-08-28 14:08
 */
public interface SpuService {

    public List<PmsProductInfo> spuList(Integer catalog3Id);

    public List<PmsBaseSaleAttr> baseSaleAttrList();


    //添加
    public Integer  saveSpuInfo(PmsProductInfo pmsProductInfo);


    List<PmsProductSaleAttr> spuSaleAttrList(Integer spuId);

    //查询图片
    List<PmsProductImage> spuImageList(Long spuId);
}
