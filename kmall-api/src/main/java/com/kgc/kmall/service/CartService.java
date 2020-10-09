package com.kgc.kmall.service;

import com.kgc.kmall.bean.OmsCartItem;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-10-09 16:02
 */

public interface CartService {

    OmsCartItem ifCartExistByUser(String memberId, long skuId);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemFromDb);

    void flushCartCache(String memberId);

    List<OmsCartItem> cartList(String memberId);
}
