package com.kgc.kmall.kmallcartweb.controller;

import com.alibaba.fastjson.JSON;
import com.kgc.kmall.annotations.LoginRequired;
import com.kgc.kmall.bean.OmsCartItem;
import com.kgc.kmall.bean.PmsSkuInfo;
import com.kgc.kmall.service.CartService;
import com.kgc.kmall.service.SkuService;
import com.kgc.kmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

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

    @LoginRequired(value = false)
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
        String memberId = "1";

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
                            cartItem.setDeleteStatus(0);
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
                omsCartItemFromDb.setDeleteStatus(0);
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

    /*@RequestMapping("/cartList")
    public String cartList(ModelMap model, HttpServletRequest request) {

        List<OmsCartItem> omsCartItems=new ArrayList<>();

        List<Long> skuIds=new ArrayList<>();

        String memberId="1";

        if(StringUtils.isNotBlank(memberId)){
            //已经登查询db  调用服务 修改状态
            omsCartItems = cartService.cartList(memberId);
        }else{
            //没有登陆查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isNotBlank(cartListCookie)) {
                omsCartItems=JSON.parseArray(cartListCookie,OmsCartItem.class);
            }
        }

        //计算小价
    *//*    for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(new BigDecimal(omsCartItem.getQuantity())));
        }*//*


        //总价
        BigDecimal totalAmount = getTotalAmount(omsCartItems);
        model.addAttribute("totalAmount",totalAmount);
        model.addAttribute("cartList",omsCartItems);
        return  "cartList";
    }*/
    @LoginRequired(false)
    @RequestMapping("/cartList")
    public String cartList(ModelMap modelMap,HttpServletRequest request){
        List<OmsCartItem> omsCartItems=new ArrayList<>();
        List<Long> skuIds=new ArrayList<>();
        List<Long> skuIds2=new ArrayList<>();
        String memberId = "1";
        if(StringUtils.isNotBlank(memberId)){//已登录
            List<OmsCartItem> items=cartService.cartList(memberId);
            for (OmsCartItem item : items) {
                //显示状态为0的加入到集合
                if(item.getDeleteStatus()==0){
                    omsCartItems.add(item);
                    if(item.getIsChecked().equals("1")){//将选中的
                        //将skuId添加
                        skuIds.add(item.getProductSkuId());
                    }
                    skuIds2.add(item.getProductSkuId());//全部的
                }
            }
        }else{//未登录
            // cookie里原有的购物车数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isNotBlank(cartListCookie)){
                List<OmsCartItem> items=JSON.parseArray(cartListCookie,OmsCartItem.class);
                for (OmsCartItem item : items) {
                    //显示状态为0的加入到集合
                    if(item.getDeleteStatus()==0){
                        omsCartItems.add(item);
                        if(item.getIsChecked().equals("1")){//将选中的
                            //将skuId添加
                            skuIds.add(item.getProductSkuId());
                        }
                        skuIds2.add(item.getProductSkuId());//全部的
                    }
                }
            }
        }
        BigDecimal totalAmount =getTotalAmount(omsCartItems);
        modelMap.put("totalAmount",totalAmount);
        modelMap.put("cartList",omsCartItems);
        //获取已选中的skuId
        modelMap.put("skuIdscheck",skuIds);
        //获取全部的skuId
        modelMap.put("skuIdsAll",skuIds2);
        return "cartList";
    }


    @LoginRequired(false)
    @RequestMapping("/checkCart")
    @ResponseBody
    public Map<String,Object> checkCart(String isChecked,Long skuId,HttpServletRequest request,HttpServletResponse response){

        Map<String,Object> map=new HashMap<>();
        String memberId="1";

        //判断如果用户登陆的话
        if(StringUtils.isNotBlank(memberId)){
            //调用服务,修改状态
            OmsCartItem omsCartItem=new OmsCartItem();
            omsCartItem.setMemberId(Long.parseLong(memberId));
            omsCartItem.setProductSkuId(skuId);
            omsCartItem.setIsChecked(isChecked);
            cartService.checkCart(omsCartItem);

            //计算总价
            List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
            BigDecimal totalAmount =getTotalAmount(omsCartItems);
            map.put("totalAmount",totalAmount);

        }else{
            // 没有登录 查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            //如果cookie里面没有值的话
            if(StringUtils.isNotBlank(cartListCookie)){
                List<OmsCartItem> omsCartItems = JSON.parseArray(cartListCookie,OmsCartItem.class);

                //修改
                for (OmsCartItem omsCartItem : omsCartItems) {
                        if(omsCartItem.getProductSkuId()==skuId){
                            omsCartItem.setIsChecked(isChecked);
                            break;
                        }
                }
                //保存cookie
                CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
                //计算总价
                BigDecimal totalAmount =getTotalAmount(omsCartItems);
                map.put("totalAmount",totalAmount);
            }
        }
        return map;
    }


    //计算总价
    public BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems){
        BigDecimal totalMonye=new BigDecimal(0);
        for (OmsCartItem omsCartItem : omsCartItems) {
            //计算小计
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(new BigDecimal(omsCartItem.getQuantity())));
            if (omsCartItem.getIsChecked().equals("1"))
                totalMonye=totalMonye.add(omsCartItem.getTotalPrice());
        }
        return totalMonye;
    }


    @RequestMapping("/delCart")
    @ResponseBody
    public int DelCart(@RequestBody List<Long> skuIdlist, HttpServletRequest request, HttpServletResponse response){
        System.out.println(skuIdlist);
        //获取前台传来的集合
        String memberId = "1";
        if(StringUtils.isNotBlank(memberId)){//登录
            //调用服务 修改选中
            cartService.UpdateDelCart(skuIdlist,memberId);
            return 1;
        }else{//没有登录
            //cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isNotBlank(cartListCookie)) {
                List<OmsCartItem> omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
                //修改
                for (OmsCartItem omsCartItem : omsCartItems) {
                    for (Long skuId : skuIdlist) {
                        if(omsCartItem.getProductSkuId()==(long)skuId){
                            omsCartItem.setQuantity(0);
                            omsCartItem.setDeleteStatus(1);
                        }
                    }

                }
                //保存cookie
                CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
            }
            return 1;
        }
    }

    @RequestMapping("/updateNum")
    @ResponseBody
    public void Update(Long skuId,int num,HttpServletRequest request,HttpServletResponse response){
        String memberId = "1";
        if(StringUtils.isNotBlank(memberId)){//登录
            //调用服务 修改选中
            OmsCartItem omsCartItemFromDb = cartService.ifCartExistByUser(memberId,skuId);
            omsCartItemFromDb.setQuantity(omsCartItemFromDb.getQuantity()+num);
            System.out.println(omsCartItemFromDb);
            if (omsCartItemFromDb.getQuantity()>0) {
                cartService.updateCart(omsCartItemFromDb);
                cartService.flushCartCache(memberId);
            }
        }else{//没有登录
            //cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isNotBlank(cartListCookie)) {
                List<OmsCartItem> omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
                //修改
                for (OmsCartItem omsCartItem : omsCartItems) {
                    if(omsCartItem.getProductSkuId()==(long)skuId){
                        omsCartItem.setQuantity(omsCartItem.getQuantity()+num);
                        if(omsCartItem.getQuantity()==0){
                            return;
                        }
                    }
                }
                //保存cookie
                CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
            }
        }
    }

    @LoginRequired(false)
    @RequestMapping("toTrade")
    public String toTrade() {

        return "toTrade";
    }


}
