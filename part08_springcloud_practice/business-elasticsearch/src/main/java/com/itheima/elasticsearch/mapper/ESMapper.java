package com.itheima.elasticsearch.mapper;

import com.itheima.domain.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Program: SpringCloud
 * @InterfaceName: ESMapper
 * @Description:
 * @Author: KyleSun
 **/
@Mapper
public interface ESMapper {

    @Select("select * from goods")
    List<Goods> findAll();

}
