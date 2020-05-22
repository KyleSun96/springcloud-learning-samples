package com.itheima.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Program: SpringCloud
 * @ClassName: goods
 * @Description: 商品信息
 * @Author: KyleSun
 **/
@Getter
@Setter
@ToString
public class Goods {

    private double id;
    private String title;
    private double price;
    private double stock;
    private double saleNum;
    private Date createTime;
    private String categoryName;
    private String brandName;
    private String spec;
    private String seller;
    private String company;

}
