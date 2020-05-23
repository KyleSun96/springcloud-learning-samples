package com.itheima.elasticsearch.controller;

import com.itheima.domain.Goods;
import com.itheima.elasticsearch.service.ESService;
import com.itheima.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Program: SpringCloud
 * @ClassName: ESController
 * @Description:
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/search")
public class ESController {

    @Autowired
    private ESService esService;

    /**
     * @description: //TODO 同步所有数据
     * @param: []
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/importAll")
    public Result importAll() {
        try {
            esService.importAll();
            return new Result(true, "同步所有数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "同步所有数据失败");
        }
    }


    /**
     * @description: //TODO 商品查询
     * @param: [query]
     * @return: com.itheima.entity.Result
     */
    @PostMapping("/findGoods")
    public Result findGoods(@RequestBody Map<String, String> param) {
        try {
            List<Goods> goodsList = esService.findGoods(param);
            return new Result(true, "商品查询成功", goodsList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "商品查询失败");
        }
    }

}
