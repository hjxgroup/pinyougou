package com.hanjixin.core.service;

import com.hanjixin.core.pojo.seller.Seller;
import entity.PageResult;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface SellerService {
    void add(Seller seller);

    Seller findOne(String username);

    PageResult search(Integer page, Integer rows, Seller seller);


    List<Seller> dataExport();

}
