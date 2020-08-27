package com.kgc.kmall.managerservice;

import com.kgc.kmall.bean.PmsBaseAttrInfo;
import com.kgc.kmall.service.AttrService;
import com.kgc.kmall.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class KmallManagerServiceApplicationTests {

    @Resource
    CatalogService catalogService;
    @Resource
    AttrService attrService;
    @Test
    void contextLoads() {
       /* List<PmsBaseCatalog1> catalog1 = catalogService.getCatalog1();

        for (PmsBaseCatalog1 pmsBaseCatalog1 : catalog1) {
            System.out.println(pmsBaseCatalog1.getName());
        }*/

        List<PmsBaseAttrInfo> select = attrService.select((long) 1);
        for (PmsBaseAttrInfo pmsBaseAttrInfo : select) {
            System.out.println(pmsBaseAttrInfo.getAttrName());
        }
    }

}
