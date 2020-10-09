package com.kgc.kmall.kmallcartweb.controller;

import com.alibaba.fastjson.JSON;
import com.kgc.kmall.bean.OmsCartItem;
import com.kgc.kmall.bean.PmsSkuInfo;
import com.kgc.kmall.service.CartService;
import com.kgc.kmall.service.SkuService;
import com.kgc.kmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-09-28 15:49
 */
@Controller
public class CartController {

    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;

    @RequestMapping("/addToCart")
    public String addToCart(long skuId, Integer num, HttpServletResponse response, HttpServletRequest request) {


        List<OmsCartItem> omsCartItems = new ArrayList<>();

        //调用商品服务查询商品信息
//        PmsSkuInfo skuInfo = skuService.selectBySkuId(skuId,"");
        PmsSkuInfo skuInfo = skuService.selectBySkuId(skuId);
        //将商品对象封装成购物车 信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(new BigDecimal(skuInfo.getPrice()));
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getSpuId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("11111111111");
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(num);

        // 判断用户是否登录
        String memberId = "";

        if (StringUtils.isBlank(memberId)) {
            //cookie里原有的购物车数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);

            if (StringUtils.isBlank(cartListCookie)) {
                //cookie为空 没有购物车信息
                omsCartItems.add(omsCartItem);
            } else {
                //不为空  you购物车信息
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);

                if (omsCartItems != null && omsCartItems.size() > 0) {
                    //购物车集合中有商品
                    // 判断添加的购物车数据在cookie中是否存在
                    boolean exist = if_cart_exist(omsCartItems, omsCartItem);

                    // 之前添加过，更新购物车添加数量
                    for (OmsCartItem cartItem : omsCartItems) {
                        if (cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())) {
                            cartItem.setQuantity(cartItem.getQuantity() + omsCartItem.getQuantity());
                            break;
                        }
                    }
                } else {

                    //购物车集合中没有商品
                    omsCartItems.add(omsCartItem);
                }
            }

            System.out.println(JSON.toJSONString(omsCartItems));

            // 更新cookie
            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
        } else {
            //思路一：根据用户id查询购物车信息，如果不存在则添加，如果存在判断skuid是否存在，如果不存在则添加，如果存在则修改
            //思路二：根据用户id和skuid查询，如果不存在则添加，如果存在则修改
            // 用户已经登录
            // 从db中查出购物车数据

            OmsCartItem omsCartItemFromDb = cartService.ifCartExistByUser(memberId, skuId);
            if (omsCartItemFromDb == null) {
                // 该用户没有添加过当前商品
                omsCartItem.setMemberId(Long.parseLong(memberId));
                omsCartItem.setMemberNickname("test小明");
                cartService.addCart(omsCartItem);
            } else {
                // 该用户添加过当前商品
                Integer quantity = omsCartItemFromDb.getQuantity();
                quantity = quantity + num;
                omsCartItemFromDb.setQuantity(quantity);
                cartService.updateCart(omsCartItemFromDb);
            }

            // 同步缓存
            cartService.flushCartCache(memberId);


        }

        return "redirect:/success.html";
    }


    private boolean if_cart_exist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {
        boolean b = false;
        for (OmsCartItem cartItem : omsCartItems) {
            Long productSkuId = cartItem.getProductSkuId();
            if (productSkuId.equals(omsCartItem.getProductSkuId())) {
                b = true;
                break;
            }
        }

        return b;
    }

    @RequestMapping("/cartList")
    public String cartList(ModelMap model, HttpServletRequest request) {

        List<OmsCartItem> omsCartItems=new ArrayList<>();
        String memberId="";

        if(StringUtils.isNotBlank(memberId)){
            //已经登查询db

        }else{
            //没有登陆查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isNotBlank(cartListCookie)) {
                omsCartItems=JSON.parseArray(cartListCookie,OmsCartItem.class);
            }
        }

        for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(new BigDecimal(omsCartItem.getQuantity())));
        }

        model.addAttribute("cartList",omsCartItems);
        return  "cartList";
    }
}
