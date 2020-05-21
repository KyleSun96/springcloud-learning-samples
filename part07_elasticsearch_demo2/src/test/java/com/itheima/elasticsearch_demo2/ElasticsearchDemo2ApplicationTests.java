package com.itheima.elasticsearch_demo2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
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
     * matchAll查询所有 -- 分页查询
     * <p>
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
     * term词条查询
     */
    @Test
    public void termQuery() throws IOException {

        SearchRequest searchRequest = new SearchRequest("goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//=====================================================================================================
        QueryBuilder query = QueryBuilders.termQuery("title", "华为");
//=====================================================================================================
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

        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }


    /**
     * match词条分词查询
     */
    @Test
    public void matchQuery() throws IOException {

        SearchRequest searchRequest = new SearchRequest("goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//=====================================================================================================
        // 求并集 and      交集 or
        MatchQueryBuilder query = QueryBuilders.matchQuery("title", "华为手机").operator(Operator.AND);
//=====================================================================================================
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

        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }


    /**
     * 模糊查询：wildcard 通配符查询
     */
    @Test
    public void wildcardQuery() throws IOException {

        SearchRequest searchRequest = new SearchRequest("goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//=====================================================================================================
        WildcardQueryBuilder query = QueryBuilders.wildcardQuery("title", "华*");
//=====================================================================================================
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

        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }


    /**
     * 模糊查询：regexp 正则表达式查询
     */
    @Test
    public void regexpQuery() throws IOException {

        SearchRequest searchRequest = new SearchRequest("goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//=====================================================================================================
        RegexpQueryBuilder query = QueryBuilders.regexpQuery("title", "\\w+(.)*");
//=====================================================================================================
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

        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }


    /**
     * 模糊查询：prefix 前缀查询
     */
    @Test
    public void prefixQuery() throws IOException {

        SearchRequest searchRequest = new SearchRequest("goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//=====================================================================================================
        PrefixQueryBuilder query = QueryBuilders.prefixQuery("brandName", "三");
//=====================================================================================================
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

        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }


    /**
     * rangeQuery 范围查询 并 排序
     */
    @Test
    public void rangeQuery() throws IOException {

        SearchRequest searchRequest = new SearchRequest("goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//=====================================================================================================
        RangeQueryBuilder query = QueryBuilders.rangeQuery("price");
        // 范围查询需要指定上限和下限
        query.gte(2000);
        query.lte(3000);

        sourceBuilder.query(query);

        // 排序  ASC 或 DESC
        sourceBuilder.sort("price", SortOrder.ASC);
//=====================================================================================================
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

        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }


    /**
     * queryString 多条件查询
     */
    @Test
    public void queryString() throws IOException {

        SearchRequest searchRequest = new SearchRequest("goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//=====================================================================================================
        QueryStringQueryBuilder query = QueryBuilders.queryStringQuery("华为手机")
                .field("title").field("brandName").field("categoryName").defaultOperator(Operator.OR);
//=====================================================================================================
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

        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }


    /**
     * boolQuery 布尔查询
     * <p>
     * 需求：
     * 1. 查询品牌名称为:华为
     * 2. 查询标题包含：手机
     * 3. 查询价格在：2000-3000
     */
    @Test
    public void boolQuery() throws IOException {

        SearchRequest searchRequest = new SearchRequest("goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//=====================================================================================================
        // 1.构建 boolQuery
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        // 2.构建各个查询条件
        // 2.1 查询品牌名称为:华为
        TermQueryBuilder termQuery = QueryBuilders.termQuery("brandName", "华为");
        query.must(termQuery);

        // 2.2. 查询标题包含：手机
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("title", "手机");
        query.filter(matchQuery);

        // 2.3 查询价格在：2000-3000
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("price");
        rangeQuery.gte(2000);
        rangeQuery.lte(3000);
        query.filter(rangeQuery);
//=====================================================================================================
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

        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }


    /**
     * 聚合查询：桶聚合，分组查询
     * 1. 查询title包含手机的数据
     * 2. 查询品牌列表
     */
    @Test
    public void aggQuery() throws IOException {

        SearchRequest searchRequest = new SearchRequest("goods");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//=====================================================================================================
        // 查询title包含手机的数据
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "手机");

        sourceBuilder.query(queryBuilder);

        // 查询品牌列表  只展示前100条  自定义名称terms和分组字段field
        AggregationBuilder aggregation = AggregationBuilders.terms("goods_brands").field("brandName").size(100);
        sourceBuilder.aggregation(aggregation);
//=====================================================================================================
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 获取命中对象 SearchHits
        SearchHits hits = searchResponse.getHits();

        // 获取总记录数
        Long total = hits.getTotalHits().value;
        System.out.println("总数：" + total);
//=====================================================================================================
        // aggregations 对象
        Aggregations aggregations = searchResponse.getAggregations();
        // 将aggregations 转化为map
        Map<String, Aggregation> aggregationMap = aggregations.asMap();

        // 通过key获取goods_brands 对象 使用Aggregation的子类接收  buckets属性在Terms接口中体现
        // Aggregation goods_brands1 = aggregationMap.get("goods_brands");
        Terms goods_brands = (Terms) aggregationMap.get("goods_brands");

        // 获取buckets 数组集合
        List<? extends Terms.Bucket> buckets = goods_brands.getBuckets();

        Map<String, Object> map = new HashMap<>();

        // 遍历buckets   key 属性名，doc_count 统计聚合数
        for (Terms.Bucket bucket : buckets) {
            System.out.println(bucket.getKey());
            map.put(bucket.getKeyAsString(), bucket.getDocCount());
        }
//=====================================================================================================
        System.out.println(map);
    }


    /**
     * 高亮查询：
     * 1. 设置高亮
     * * 高亮字段
     * * 前缀
     * * 后缀
     * 2. 将高亮了的字段数据，替换原有数据
     */
    @Test
    public void highLightQuery() throws IOException {

        SearchRequest searchRequest = new SearchRequest("goods");

        SearchSourceBuilder sourceBulider = new SearchSourceBuilder();

        // 查询title包含手机的数据
        MatchQueryBuilder query = QueryBuilders.matchQuery("title", "手机");

        sourceBulider.query(query);
//=====================================================================================================
        // 设置高亮
        HighlightBuilder highlighter = new HighlightBuilder();
        // 设置三要素
        highlighter.field("title");
        // 设置前后缀标签
        highlighter.preTags("<font color='red'>");
        highlighter.postTags("</font>");

        // 加载已经设置好的高亮配置
        sourceBulider.highlighter(highlighter);
//=====================================================================================================
        searchRequest.source(sourceBulider);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


        SearchHits searchHits = searchResponse.getHits();
        // 获取记录数
        long value = searchHits.getTotalHits().value;
        System.out.println("总记录数：" + value);

        List<Goods> goodsList = new ArrayList<>();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();

            // 转为java
            Goods goods = JSON.parseObject(sourceAsString, Goods.class);
//=====================================================================================================
            /*
                得到高亮的所有的域，得到map，map中的key就是我们所指定的字段，
                后面的value就是当前字段的高亮内容。
                此处我们拿出"title"的高亮，再拿出文本的片段，遍历拼接在一起。

                此处我们指定 key：highlightFields.get("title");
                此时我们只有一个片段，因此直接取第一个即可：fragments[0]
                当文本较多时，会自动分片段，我们将他遍历后再拼接在一起。
            */
            // 获取高亮结果，替换goods中的title
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("title");
            Text[] fragments = highlightField.fragments();
            // highlight title替换 替换goods中的title
            goods.setTitle(fragments[0].toString());
            goodsList.add(goods);
        }
//=====================================================================================================
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }

}
