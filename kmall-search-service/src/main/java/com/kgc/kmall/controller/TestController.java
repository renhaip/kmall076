/*
package com.kgc.kmall.controller;

import com.kgc.kmall.bean.PmsSearchSkuInfo;
import com.kgc.kmall.bean.PmsSkuInfo;
import com.kgc.kmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * @author shkstart
 * @create 2020-09-26 16:41
 *//*

@Controller
public class TestController {

    @Reference
    SkuService skuService;

    @Reference
    JestClient jestClient;

   @RequestMapping("/test")
   @ResponseBody
    public List<PmsSearchSkuInfo> test01() {
       List<PmsSkuInfo> allSku = skuService.getAllSku();
       List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
       for (PmsSkuInfo pmsSkuInfo : allSku) {
           PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
           BeanUtils.copyProperties(pmsSkuInfo, pmsSearchSkuInfo);
           pmsSearchSkuInfo.setProductId(pmsSkuInfo.getSpuId());
           pmsSearchSkuInfos.add(pmsSearchSkuInfo);
       }

       for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
           System.out.println(pmsSearchSkuInfo.toString());
           Index index = new Index.Builder(pmsSearchSkuInfo).index("kmall").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId() + "").build();
           try {
               jestClient.execute(index);
           } catch (IOException e) {
               e.printStackTrace();
           }

       }
   return pmsSearchSkuInfos;
   }
}
*/
