package com.itheima.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Program: SpringCloud
 * @ClassName: Result
 * @Description:
 * @Author: KyleSun
 **/
@Getter
@Setter
@ToString
public class Result implements Serializable {

    private boolean flag;   //执行结果，true为执行成功 false为执行失败
    private String message; //返回结果信息，主要用于页面提示信息
    private Object data;    //返回数据

    public Result() {
    }

    public Result(boolean flag, String message) {
        super();
        this.flag = flag;
        this.message = message;
    }

    public Result(boolean flag, String message, Object data) {
        this.flag = flag;
        this.message = message;
        this.data = data;
    }
}
