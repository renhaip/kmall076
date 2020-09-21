package com.kgc.kmall.service;

import com.kgc.kmall.bean.PmsSearchSkuInfo;
import com.kgc.kmall.bean.PmsSearchSkuParam;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-09-21 17:27
 */
public interface SearchService {

    //根据关键字  三级分类  平台属性值查询
    List<PmsSearchSkuInfo> list(PmsSearchSkuParam pmsSearchSkuParam);
}
