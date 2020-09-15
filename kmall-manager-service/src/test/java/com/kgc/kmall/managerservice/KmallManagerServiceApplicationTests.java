package com.kgc.kmall.managerservice;

import com.kgc.kmall.service.AttrService;
import com.kgc.kmall.service.CatalogService;
import com.kgc.kmall.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

@SpringBootTest
class KmallManagerServiceApplicationTests {

    @Resource
    CatalogService catalogService;
    @Resource
    AttrService attrService;
    @Resource
    RedisUtil redisUtil;
    @Test
    void contextLoads() {
       /* List<PmsBaseCatalog1> catalog1 = catalogService.getCatalog1();

        for (PmsBaseCatalog1 pmsBaseCatalog1 : catalog1) {
            System.out.println(pmsBaseCatalog1.getName());
        }*/

      /*  List<PmsBaseAttrInfo> select = attrService.select((long) 1);
        for (PmsBaseAttrInfo pmsBaseAttrInfo : select) {
            System.out.println(pmsBaseAttrInfo.getAttrName());
        }*/

            Jedis jedis = redisUtil.getJedis();
            jedis.set("username","张三");
            String username = jedis.get("username");
            System.out.println(username);

    }

}
