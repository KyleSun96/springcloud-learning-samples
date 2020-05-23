package com.itheima.goods.mapper;

import com.itheima.domain.Goods;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

/**
 * @Program: SpringCloud
 * @InterfaceName: GoodsMapper
 * @Description: 添加商品信息，并且返回主键ID，
 * 否则传到 ES消息内容中goods没有id,ES文档操作变成了覆盖.
 * @Author: KyleSun
 **/
@Mapper
public interface GoodsMapper {

    @Insert("INSERT INTO `goods` ( `title`, `price`, `stock`, `saleNum`, `createTime`, `categoryName`, `brandName`, `spec`, `seller`, `company` )\n" +
                          "VALUES(#{title},#{price},#{stock},#{saleNum},#{createTime},#{categoryName},#{brandName},#{spec},#{seller},#{company})")
    @Options(keyProperty = "id", useGeneratedKeys = true)
    public void addGoods(Goods good);

}
