package com.kgc.kmall.managerweb.controller;

import com.kgc.kmall.bean.PmsSkuInfo;
import com.kgc.kmall.service.SkuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shkstart
 * @create 2020-09-04 15:42
 */
@CrossOrigin
@RestController
public class SkuController {

    @Reference
    SkuService skuService;
    @RequestMapping("/saveSkuInfo")
    public Integer saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){
        Integer integer = skuService.saveSkuInfo(pmsSkuInfo);
        return integer;
    }
}
