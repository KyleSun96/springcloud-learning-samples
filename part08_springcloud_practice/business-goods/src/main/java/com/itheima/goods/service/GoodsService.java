package com.itheima.goods.service;

import com.itheima.domain.Goods;
import com.itheima.goods.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Program: SpringCloud
 * @ClassName: GoodsService
 * @Description:
 * @Author: KyleSun
 **/
@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    public void addGoods(Goods good) {
        goodsMapper.addGoods(good);
    }
}
