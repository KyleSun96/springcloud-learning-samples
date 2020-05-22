package com.itheima.seller.mapper;

import com.itheima.domain.Seller;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Program: SpringCloud
 * @InterfaceName: SellerMapper
 * @Description:
 * @Author: KyleSun
 **/
@Mapper
public interface SellerMapper {

    @Select("select * from seller where id = #{id}")
    public Seller findById(int id);

}
