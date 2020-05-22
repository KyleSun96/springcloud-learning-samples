package com.itheima.seller.service;

import com.itheima.domain.Seller;
import com.itheima.seller.mapper.SellerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Program: SpringCloud
 * @ClassName: SellerService
 * @Description:
 * @Author: KyleSun
 **/
@Service
public class SellerService {

    @Autowired
    private SellerMapper sellerMapper;

    public Seller findById(int id) {
        return sellerMapper.findById(id);
    }
}
