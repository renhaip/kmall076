package com.kgc.kmall.kmallcartservice.service;

import com.alibaba.fastjson.JSON;
import com.kgc.kmall.bean.OmsCartItem;
import com.kgc.kmall.bean.OmsCartItemExample;
import com.kgc.kmall.kmallcartservice.mapper.OmsCartItemMapper;
import com.kgc.kmall.kmallcartservice.util.RedisUtil;
import com.kgc.kmall.service.CartService;
import org.apache.dubbo.config.annotation.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-10-09 16:04
 */
@Service
public class CartServiceImpl implements CartService {

    @Resource
    OmsCartItemMapper omsCartItemMapper;

    @Resource
    RedisUtil redisUtil;

    @Override
    public OmsCartItem ifCartExistByUser(String memberId, long skuId) {
        OmsCartItemExample example=new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(Long.parseLong(memberId)).andProductSkuIdEqualTo(skuId);
        List<OmsCartItem> omsCartItems = omsCartItemMapper.selectByExample(example);
        /*if (omsCartItems!=null&&omsCartItems.size()>0){
            return omsCartItems.get(0);
        }else {
            return null;
        }*/
    return  omsCartItems.size()>0?omsCartItems.get(0):null;
    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {
        omsCartItemMapper.insert(omsCartItem);
    }

    @Override
    public void updateCart(OmsCartItem omsCartItemFromDb) {
        omsCartItemMapper.updateByPrimaryKeySelective(omsCartItemFromDb);
    }

    @Override
    public void flushCartCache(String memberId) {
        OmsCartItemExample example=new OmsCartItemExample();
        OmsCartItemExample.Criteria criteria = example.createCriteria();
        criteria.andMemberIdEqualTo(Long.parseLong(memberId));
        List<OmsCartItem> omsCartItems = omsCartItemMapper.selectByExample(example);


        //同步到redis缓存中
        Jedis jedis = redisUtil.getJedis();
        Map<String,String> map=new HashMap<>();
        for (OmsCartItem cartItem : omsCartItems) {
             map.put(cartItem.getProductSkuId().toString(), JSON.toJSONString(cartItem));
        }

        //删除
            jedis.del("user:"+memberId+":cart");
        //增加
        jedis.hmset("user:"+memberId+":cart",map);

        jedis.close();
    }

    @Override
    public List<OmsCartItem> cartList(String memberId) {

        Jedis jedis=null;
        List<OmsCartItem> omsCartItems=new ArrayList<>();


        try {
          jedis=redisUtil.getJedis();
            List<String> hvals = jedis.hvals("user:" + memberId + ":cart");
            for (String hval : hvals) {
                OmsCartItem omsCartItem = JSON.parseObject(hval, OmsCartItem.class);
                omsCartItems.add(omsCartItem);
            }
        } catch (Exception e) {
            // 处理异常，记录系统日志
            e.printStackTrace();
            //String message = e.getMessage();
            //logService.addErrLog(message);
            return null;
        }finally {
            jedis.close();
        }
        return omsCartItems;
    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {
        OmsCartItemExample example=new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(omsCartItem.getMemberId()).andProductSkuIdEqualTo(omsCartItem.getProductSkuId());
        omsCartItemMapper.updateByExampleSelective(omsCartItem,example);

        flushCartCache(omsCartItem.getMemberId().toString());
    }
}
