package com.itheima.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Program: SpringCloud
 * @ClassName: Seller
 * @Description: 商家信息
 * @Author: KyleSun
 **/
@Getter
@Setter
@ToString
public class Seller {

    private int id;
    private String seller;
    private String company;
    private String telephone;
}
