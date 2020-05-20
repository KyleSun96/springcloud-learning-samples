package com.itheima.elasticsearch_demo2;

import com.alibaba.fastjson.JSON;
import com.itheima.elasticsearch_demo2.domain.Goods;
import com.itheima.elasticsearch_demo2.mapper.GoodsMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ElasticsearchDemo2ApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private GoodsMapper goodsMapper;


    /**
     * @description: //TODO Bulk 批量操作
     * @param: []
     * @return: void
     */
    @Test
    void testBulk() throws IOException {

        // 创建BulkRequest对象，整合所有操作
        BulkRequest bulkRequest = new BulkRequest();

        /*
            1. 删除 2号记录
            2. 添加 3号记录
            3. 修改 4号记录 名称为 “4号”
        */

        // 1. 删除 2号记录
        DeleteRequest deleterequest = new DeleteRequest("person", "2");
        bulkRequest.add(deleterequest);

        // 2. 添加 3号记录
        Map<String, Object> map = new HashMap<>();
        map.put("name", "3号");
        IndexRequest indexRequest = new IndexRequest("person").id("3").source(map);
        bulkRequest.add(indexRequest);

        // 3. 修改 4号记录 名称为 “4号”
        Map<String, Object> mapUpdate = new HashMap<>();
        mapUpdate.put("name", "4号");
        UpdateRequest updateRequest = new UpdateRequest("person", "4").doc(mapUpdate);
        bulkRequest.add(updateRequest);

        // 4. 执行批量操作
        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }


    /**
     * @description: //TODO 数据库中的数据批量导入处理到 ES 中
     * <p>
     * 公司中不能这么写业务代码，因为此时我们测试的数据量较少
     * 公司中服务器数据远远高于我们现在测试的数据量
     * 因此先进行分页查询，然后进行双重嵌套才能使用
     * 比如：公司数据库中 900W 条数据，我们本机一次可以导入 1W 条数据
     * 需要先分900页，然后再嵌套查询900次以下代码
     */
    @Test
    public void importData() throws IOException {
        // 查询所有数据，mysql
        List<Goods> goodsList = goodsMapper.findAll();

        // bulk导入
        BulkRequest bulkRequest = new BulkRequest();

        // 循环goodsList，创建IndexRequest添加数据
        for (Goods goods : goodsList) {

            // 获取specStr数据：string
            String specStr = goods.getSpecStr();
            // string --> map
            Map map = JSON.parseObject(specStr, Map.class);
            // 设置goods的 spec
            goods.setSpec(map);
            // goods --> json
            String data = JSON.toJSONString(goods);
            IndexRequest indexRequest = new IndexRequest("goods")
                    .id(goods.getId() + "").source(data, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }


    /**
     * 查询所有 -- 分页查询
     * 1. matchAll查询
     * 2. 将查询结果封装为Goods对象，装载到List中
     * 3. 分页。默认显示10条
     */
    @Test
    public void matchAll() throws IOException {

        // 2.构建查询请求对象，指定查询的索引名称
        SearchRequest searchRequest = new SearchRequest("goods");

        // 4.创建查询条件构建器 SearchSourceBuilder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 6.查询条件
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        // 5.指定查询条件
        sourceBuilder.query(queryBuilder);

        // 3.添加查询条件构建器 SearchSourceBuilder
        searchRequest.source(sourceBuilder);

        // 8.添加分页信息  不设置 默认10条
        // sourceBuilder.from(0);
        // sourceBuilder.size(100);

        // 1.查询,获取查询结果
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 7.获取命中对象 SearchHits
        SearchHits searchHits = searchResponse.getHits();

        // 7.1获取总记录数
        long total = searchHits.getTotalHits().value;
        System.out.println("总记录数：" + total);

        // 7.2获取Hits数据  数组
        SearchHit[] hits = searchHits.getHits();
        // 获取json字符串格式的数据
        List<Goods> goodsList = new ArrayList<>();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            // 转为java对象
            Goods goods = JSON.parseObject(sourceAsString, Goods.class);
            goodsList.add(goods);
        }

        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }


    /**
     * 词条查询
     */
    public void termQuery() throws IOException {
        
    }
}
