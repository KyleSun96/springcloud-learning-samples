package com.itheima.elasticsearch_demo2.mapper;

import com.itheima.elasticsearch_demo2.domain.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Program: SpringCloud
 * @InterfaceName: GoodsMapper
 * @Description:
 **/
@Mapper
@Repository
public interface GoodsMapper {

    List<Goods> findAll();

}
