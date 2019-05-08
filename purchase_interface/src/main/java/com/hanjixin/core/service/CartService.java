package com.hanjixin.core.service;

import com.hanjixin.core.pojo.item.Item;
import vo.Cart;

import java.util.List;

public interface CartService {

    Item findItemById(Long itemId);

    List<Cart> findCartList(List<Cart> cartList);

    void addCartListToRedis(List<Cart> cartList, String name);

    List<Cart> findCartListFromRedis(String name);
}
