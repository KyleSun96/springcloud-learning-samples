package com.itheima.seller.controller;

import com.itheima.domain.Goods;
import com.itheima.domain.Seller;
import com.itheima.entity.Result;
import com.itheima.seller.feign.GoodsFeignClient;
import com.itheima.seller.feign.SearchFeignClient;
import com.itheima.seller.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * @Program: SpringCloud
 * @ClassName: SellerController
 * @Description: 服务调用端
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private GoodsFeignClient goodsFeignClient;

    @Autowired
    private SearchFeignClient searchFeignClient;

    /**
     * @description: //TODO 调用端查询商家信息
     * @param: [id]
     * @return: com.itheima.entity.Result
     */
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


    /**
     * @description: //TODO 调用端添加商品信息
     * @param: [sellerId, good]
     * @return: com.itheima.entity.Result
     */
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


    /**
     * @description: //TODO 调用端通过ES查询数据
     * @param: [param]
     * @return: com.itheima.entity.Result
     */
    @PostMapping("/findGoods")
    public Result findGoods(@RequestBody Map<String, String> param) {
        Result result = searchFeignClient.findGoods(param);
        return result;
    }

}
