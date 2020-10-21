package com.kgc.kmall.kmallorderweb.controller;

import com.kgc.kmall.annotations.LoginRequired;
import com.kgc.kmall.bean.MemberReceiveAddress;
import com.kgc.kmall.bean.OmsCartItem;
import com.kgc.kmall.bean.OmsOrder;
import com.kgc.kmall.bean.OmsOrderItem;
import com.kgc.kmall.service.CartService;
import com.kgc.kmall.service.MemberService;
import com.kgc.kmall.service.OrderService;
import com.kgc.kmall.service.SkuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-10-19 16:51
 */
@Controller
public class OrderController {


    @Reference
    MemberService memberService;


    @Reference
    CartService cartService;
    
    @Reference
    OrderService orderService;

    @Reference
    SkuService skuService;

    @RequestMapping("/toTrade")
    @LoginRequired(value = true)
    public String toTrade(HttpServletRequest request, Model model) {

        //从拦截器中获取用户memberId和nickname
        Integer memberId = (Integer) request.getAttribute("memberId");

        //收件人地址列表
        List<MemberReceiveAddress> umsMemberReceiveAddresses = memberService.getReceiveAddressByMemberId(Long.valueOf(memberId));
        model.addAttribute("userAddressList", umsMemberReceiveAddresses);


        //将购物车集合转化为页面计算清单集合
        List<OmsCartItem> omsCartItems = cartService.cartList(memberId + "");
        //页面计算
        List<OmsOrderItem> omsOrderItems = new ArrayList<>();
        for (OmsCartItem omsCartItem : omsCartItems) {
            //每循环一个购物车对象,就封装一个商品的详情到omsOrderItem
            if (omsCartItem.getIsChecked().equals("1")) {
                OmsOrderItem omsOrderItem = new OmsOrderItem();
                omsOrderItem.setProductName(omsCartItem.getProductName());
                omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                omsOrderItem.setProductPic(omsCartItem.getProductPic());
                omsOrderItems.add(omsOrderItem);
            }
        }

        model.addAttribute("omsOrderItems", omsOrderItems);
        model.addAttribute("totalAmount", getTotalAmount(omsCartItems));

        //生成交易码

        String tradeCode = orderService.genTradeCode(Long.valueOf(memberId));
        System.out.println(tradeCode);
        model.addAttribute("tradeCode", tradeCode);

        return "trade";
    }

    //计算总价格
    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0");

        for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(new BigDecimal(omsCartItem.getQuantity())));

            BigDecimal totalPrice = omsCartItem.getTotalPrice();

            if (omsCartItem.getIsChecked().equals("1")) {
                totalAmount = totalAmount.add(totalPrice);
            }
        }
        return totalAmount;
    }


    @RequestMapping("/submitOrder")
    @LoginRequired(true)
    public String submitOrder(String receiveAddressId,
                              BigDecimal totalAmount,
                              String tradeCode,
                              HttpServletRequest request){

        //从拦截器中获取用户memberid和nickname
        Integer memberId = (Integer) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

        // 检查交易码
        String success = orderService.checkTradeCode(Long.valueOf(memberId), tradeCode);

        if (success.equals("success")) {
            System.out.println("提交订单");
            System.out.println(receiveAddressId);
            System.out.println(totalAmount);


            List<OmsOrderItem> omsOrderItems=new ArrayList<>();
            //订单对象
            OmsOrder omsOrder=new OmsOrder();
            omsOrder.setAutoConfirmDay(7);
            omsOrder.setCreateTime(new Date());
            omsOrder.setDiscountAmount(null);
            //omsOrder.setFreightAmount(); 运费，支付后，在生成物流信息时
            omsOrder.setMemberId(Long.valueOf(memberId));
            omsOrder.setMemberUsername(nickname);
            omsOrder.setNote("快点发货");

            String outTradeNo="kmall";
            outTradeNo=outTradeNo+System.currentTimeMillis();//将毫秒时间拼接到外部订单号

            SimpleDateFormat sdf=new SimpleDateFormat("YYYYMMDDHHmmss");
            outTradeNo=outTradeNo=sdf.format(new Date());
            omsOrder.setOrderSn(outTradeNo);  //外部订单号

            omsOrder.setPayAmount(totalAmount);
            omsOrder.setOrderType(1);

            MemberReceiveAddress umsMemberReceiveAddress = memberService.getReceiveAddressById(Long.parseLong(receiveAddressId));
            omsOrder.setReceiverCity(umsMemberReceiveAddress.getCity());
            omsOrder.setReceiverDetailAddress(umsMemberReceiveAddress.getDetailAddress());
            omsOrder.setReceiverName(umsMemberReceiveAddress.getName());
            omsOrder.setReceiverPhone(umsMemberReceiveAddress.getPhoneNumber());
            omsOrder.setReceiverPostCode(umsMemberReceiveAddress.getPostCode());
            omsOrder.setReceiverProvince(umsMemberReceiveAddress.getProvince());
            omsOrder.setReceiverRegion(umsMemberReceiveAddress.getRegion());
            // 当前日期加一天，一天后配送
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE,1);
            Date time = c.getTime();
            omsOrder.setReceiveTime(time);
            omsOrder.setSourceType(0);
            omsOrder.setStatus(0);
            omsOrder.setOrderType(0);
            omsOrder.setTotalAmount(totalAmount);

            //根据用户id获得要购买的商品列表(购物车)，和总价格
            List<OmsCartItem> omsCartItems = cartService.cartList(memberId.toString());

            for (OmsCartItem omsCartItem : omsCartItems) {
                if(omsCartItem.getIsChecked().equals("1")){
                    //获得订单详情列表
                    OmsOrderItem omsOrderItem=new OmsOrderItem();
                    //检价
                    boolean b=skuService.checkPrice(omsCartItem.getProductSkuId(),omsCartItem.getPrice());

                    if(b==false){
                        return "tradeFail";
                    }
                    // 验库存,远程调用库存系统
                    omsOrderItem.setProductPic(omsCartItem.getProductPic());
                    omsOrderItem.setProductName(omsCartItem.getProductName());

                    omsOrderItem.setOrderSn(outTradeNo);// 外部订单号，用来和其他系统进行交互，防止重复
                    omsOrderItem.setProductCategoryId(omsCartItem.getProductCategoryId());
                    omsOrderItem.setProductPrice(omsCartItem.getPrice());
                    omsOrderItem.setRealAmount(omsCartItem.getTotalPrice());
                    omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                    omsOrderItem.setProductSkuCode("111111111111");
                    omsOrderItem.setProductSkuId(omsCartItem.getProductSkuId());
                    omsOrderItem.setProductId(omsCartItem.getProductId());
                    omsOrderItem.setProductSn("仓库对应的商品编号");// 在仓库中的skuId

                    omsOrderItems.add(omsOrderItem);
                }
            }
            omsOrder.setOrderItems(omsOrderItems);

            // 将订单和订单详情写入数据库
            // 删除购物车的对应商品,暂时不进行删除，因为接下来需要频繁的测试

            orderService.saveOrder(omsOrder);


        }else{
            return "tradeFail";
        }
        //重定向到支付系统
        return null;
    }
}
