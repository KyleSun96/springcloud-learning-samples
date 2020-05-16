package com.itheima.provider.service;

import com.itheima.provider.dao.GoodsDao;
import com.itheima.provider.domain.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Program: SpringCloud
 * @ClassName: GoodsService
 * @Description: 模拟商品业务层
 * @Author: KyleSun
 **/
@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public Goods findOne(int id) {
        return goodsDao.findOne(id);
    }
}
