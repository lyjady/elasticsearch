package com.example.elasticsearch;

import com.example.elasticsearch.domain.News;
import com.example.elasticsearch.mapper.NewsMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchApplicationTests {

    @Autowired
    private NewsMapper mapper;

    @Autowired
    private ElasticsearchTemplate template;

    @Test
    public void testMysqlConnection() {
        System.out.println(mapper.queryCount());
    }

    @Test
    public void createIndex() {
        //创建索引
        template.createIndex(News.class);
    }
}
