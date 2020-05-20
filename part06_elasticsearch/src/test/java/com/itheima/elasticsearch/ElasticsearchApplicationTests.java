package com.itheima.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.itheima.elasticsearch.domain.Person;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
class ElasticsearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Test
    void contextLoads() {
        System.out.println(client);
    }


    /**
     * @description: //TODO 添加索引，并添加映射
     * @param: []
     * @return: void
     */
    @Test
    public void addIndexAndMapping() throws IOException {

        // 1.使用client获取操作索引对象
        IndicesClient indices = client.indices();

        // 2.具体操作，获取返回值
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("person1");

        // 2.1 设置mappings
        /*String mapping = "{\n" +
                "      \"properties\" : {\n" +
                "        \"address\" : {\n" +
                "          \"type\" : \"text\",\n" +
                "          \"analyzer\" : \"ik_max_word\"\n" +
                "        },\n" +
                "        \"age\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"name\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "    }";

        createIndexRequest.mapping(mapping, XContentType.JSON);*/

        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest, RequestOptions.DEFAULT);

        // 3.根据返回值判断结果
        System.out.println(createIndexResponse.isAcknowledged());
    }


    /**
     * @description: //TODO 查询索引
     * @param: []
     * @return: void
     */
    @Test
    public void queryIndex() throws IOException {

        IndicesClient indices = client.indices();

        GetIndexRequest getRequest = new GetIndexRequest("person1");
        GetIndexResponse response = indices.get(getRequest, RequestOptions.DEFAULT);
        Map<String, MappingMetaData> mappings = response.getMappings();

        //iter 提示foreach
        for (String key : mappings.keySet()) {
            System.out.println(key + "===" + mappings.get(key).getSourceAsMap());
        }
    }


    /**
     * @description: //TODO 删除索引
     * @param: []
     * @return: void
     */
    @Test
    public void deleteIndex() throws IOException {

        IndicesClient indices = client.indices();

        DeleteIndexRequest deleteRequest = new DeleteIndexRequest("person1");
        AcknowledgedResponse delete = indices.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }


    /**
     * @description: //TODO 索引是否存在
     * @param: []
     * @return: void
     */
    @Test
    public void existIndex() throws IOException {

        IndicesClient indices = client.indices();

        GetIndexRequest getIndexRequest = new GetIndexRequest("person1");
        boolean exists = indices.exists(getIndexRequest, RequestOptions.DEFAULT);

        System.out.println(exists);

    }


    /**
     * @description: //TODO 添加文档,使用对象作为数据
     * @param: []
     * @return: void
     */
    @Test
    public void addDoc() throws IOException {

        // 数据对象
        Person person = new Person("1", "某人", 24, "上海");

        // 将对象转为json
        String date = JSON.toJSONString(person);

        // 获取操作文档的对象
        IndexRequest request = new IndexRequest("person1").id(person.getId()).source(date, XContentType.JSON);

        // 添加数据，获取结果
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

        System.out.println(response.getId());
    }


    /**
     * @description: //TODO 修改文档：添加文档时，如果id存在则修改，id不存在则添加
     * @param: []
     * @return: void
     */
    @Test
    public void UpdateDoc() throws IOException {
        Person person = new Person("1", "某人", 27, "上海");

        String data = JSON.toJSONString(person);

        IndexRequest request = new IndexRequest("person1").id(person.getId()).source(data, XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getId());
    }


    /**
     * @description: //TODO 根据id查询文档
     * @param: []
     * @return: void
     */
    @Test
    public void getDoc() throws IOException {

        //设置查询的索引、文档
        GetRequest indexRequest = new GetRequest("person1", "5");
        GetResponse response = client.get(indexRequest, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
    }


    /**
     * @description: //TODO 根据id删除文档
     * @param: []
     * @return: void
     */
    @Test
    public void delDoc() throws IOException {

        //设置要删除的索引、文档
        DeleteRequest deleteRequest = new DeleteRequest("person1", "1");

        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(response.getId());
    }
}
