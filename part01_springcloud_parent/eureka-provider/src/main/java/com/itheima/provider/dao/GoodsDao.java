package com.itheima.provider.dao;

import com.itheima.provider.domain.Goods;
import org.springframework.stereotype.Repository;

/**
 * @Program: SpringCloud
 * @ClassName: GoodsDao
 * @Description: 模拟商品Dao
 * @Author: KyleSun
 **/
@Repository
public class GoodsDao {

    //TODO 模拟从数据库查询
    public Goods findOne(int id) {
        return new Goods(1, "华为手机", 4999, 10000);
    }
}
