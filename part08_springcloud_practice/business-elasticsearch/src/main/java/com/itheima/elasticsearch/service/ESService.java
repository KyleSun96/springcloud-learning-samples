package com.itheima.elasticsearch.service;

import com.alibaba.fastjson.JSON;
import com.itheima.domain.Goods;
import com.itheima.elasticsearch.mapper.ESMapper;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Program: SpringCloud
 * @ClassName: ESService
 * @Description:
 * @Author: KyleSun
 **/
@Service
public class ESService {

    @Autowired
    private ESMapper esMapper;

    @Autowired
    private RestHighLevelClient client;

    /**
     * @description: //TODO 同步所有数据
     * @param: []
     * @return: void
     */
    public void importAll() throws IOException {

        List<Goods> goodsList = esMapper.findAll();

        // bulk导入
        BulkRequest bulkRequest = new BulkRequest();

        // 循环goodsList，创建IndexRequest添加数据
        for (Goods goods : goodsList) {

            // 给同步到 ES 中的数据添加 ID，ID值为 mysql 中的 ID
            double goodsId = goods.getId();

            // 将 goods对象转换为json字符串
            String data = JSON.toJSONString(goods);

            IndexRequest indexRequest = new IndexRequest("goods").id(goodsId + "").source(data, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        // 同步所有数据状态：是否成功
        System.out.println(response.status());
    }


    /**
     * @description: //TODO 添加单个商品后，同步到 ES
     * @param: [jsonGoods]
     * @return: void
     */
    public void addGoods(String jsonGoods) throws IOException {

        // 转为 Goods 对象，拿到 id
        Goods goods = JSON.parseObject(jsonGoods, Goods.class);

        IndexRequest request = new IndexRequest("goods").id(goods.getId() + "").source(jsonGoods, XContentType.JSON);

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

    }


    /**
     * @description: //TODO 商品查询
     * @param: [param]
     * @return: java.util.List<com.itheima.domain.Goods>
     * <p>
     * 1. 如果没有输入查询关键字 keyword ,则不进行查询
     * 2. 使用 query String 对 keyword 进行查询，从 title,brandName,category 进行查询，操作 OR
     * 3. 添加 brandName 条件，filter
     * 4. 添加价格，最大值最小值：price=1000-2000
     */
    public List<Goods> findGoods(Map<String, String> param) throws IOException {
        SearchRequest searchRequest = new SearchRequest("goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder query = QueryBuilders.boolQuery();

        // 如果没有输入查询关键字 keyword ,则不进行查询
        if (param == null || StringUtils.isBlank(param.get("keyword"))) {
            return new ArrayList<>();
        }

        // 使用 query String 对 keyword 进行查询，从 title,brandName,category 进行查询，操作 OR
        QueryStringQueryBuilder queryStringQuery = QueryBuilders.queryStringQuery(param.get("keyword"))
                .field("title").field("categoryName").field("brandName")
                .defaultOperator(Operator.OR);
        query.must(queryStringQuery);

        // 添加 brandName 条件，filter
        if (StringUtils.isNotBlank(param.get("brandName"))) {
            TermQueryBuilder termQuery = QueryBuilders.termQuery("brandName", param.get("brandName"));
            query.filter(termQuery);
        }

        // 添加价格，最大值最小值：price = 1000-2000
        if (StringUtils.isNotBlank(param.get("price"))) {
            String[] priceArr = param.get("price").split("-");

            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("price");
            rangeQuery.gte(String.valueOf(priceArr[0]));
            rangeQuery.lte(String.valueOf(priceArr[1]));

            query.filter(rangeQuery);
        }

        // 使用 boolQuery 连接
        sourceBuilder.query(query);

        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits searchHits = searchResponse.getHits();
        long total = searchHits.getTotalHits().value;
        System.out.println("总记录数：" + total);

        List<Goods> goodsList = new ArrayList<>();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            Goods goods = JSON.parseObject(sourceAsString, Goods.class);
            goodsList.add(goods);
        }
        return goodsList;
    }
}
