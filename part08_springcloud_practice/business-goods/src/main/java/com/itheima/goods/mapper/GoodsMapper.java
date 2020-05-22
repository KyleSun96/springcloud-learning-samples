package com.itheima.goods.mapper;

import com.itheima.domain.Goods;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Program: SpringCloud
 * @InterfaceName: GoodsMapper
 * @Description:
 * @Author: KyleSun
 **/
@Mapper
public interface GoodsMapper {

    @Insert("INSERT INTO `goods` ( `title`, `price`, `stock`, `saleNum`, `createTime`, `categoryName`, `brandName`, `spec`, `seller`, `company` )\n" +
                          "VALUES(#{title},#{price},#{stock},#{saleNum},#{createTime},#{categoryName},#{brandName},#{spec},#{seller},#{company})")
    public void addGoods(Goods good);

}
