package com.itheima.seller.controller;

import com.itheima.domain.Goods;
import com.itheima.domain.Seller;
import com.itheima.entity.Result;
import com.itheima.seller.feign.GoodsFeignClient;
import com.itheima.seller.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Program: SpringCloud
 * @ClassName: SellerController
 * @Description:
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private GoodsFeignClient goodsFeignClient;

    @GetMapping("/findById/{id}")
    public Result findById(@PathVariable("id") int id) {
        try {
            Seller seller = sellerService.findById(id);
            return new Result(true, "查询商家信息成功", seller);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询商家信息失败");
        }
    }


    @PostMapping("/addGoods/{sellerId}")
    public Result addGoods(@PathVariable("sellerId") int sellerId, @RequestBody Goods good) {

        Seller seller = sellerService.findById(sellerId);
        good.setSeller(seller.getSeller());
        good.setCompany(seller.getCompany());
        good.setCreateTime(new Date());
        good.setSpec("{\"机身内存\":\"16G\",\"网络\":\"联通3G\"}");

        Result result = goodsFeignClient.addGoods(good);
        return result;

    }

}
