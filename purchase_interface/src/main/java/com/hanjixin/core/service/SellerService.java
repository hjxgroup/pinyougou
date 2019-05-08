package com.hanjixin.core.service;

import com.hanjixin.core.pojo.seller.Seller;

public interface SellerService {
    void add(Seller seller);

    Seller findOne(String username);
}
