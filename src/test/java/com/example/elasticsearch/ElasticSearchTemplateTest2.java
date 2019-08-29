package com.example.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.elasticsearch.domain.Computer;
import com.example.elasticsearch.repository.ComputerRepository;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author LinYongJin
 * @date 2019/8/29 20:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchTemplateTest2 {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private ComputerRepository repository;

    @Test
    public void createMapping() {
        template.createIndex(Computer.class);
        template.putMapping(Computer.class);
    }

    @Test
    public void insertData() {
        Computer c1 = new Computer(1L, "MacBook Air", 9000.0, "Apple");
        Computer c2 = new Computer(2L, "MacBook Pro", 14000.0, "Apple");
        Computer c3 = new Computer(3L, "NP900X5T", 9200.0, "Samsung");
        Computer c4 = new Computer(4L, "NP350XAA", 4600.0, "Samsung");
        Computer c5 = new Computer(5L, "MateBook", 4600.0, "HuaWei");
        Computer c6 = new Computer(6L, "MateBook Pro", 6600.0, "HuaWei");
        List<Computer> computers = new ArrayList<>();
        computers.add(c1);
        computers.add(c2);
        computers.add(c3);
        computers.add(c4);
        computers.add(c5);
        computers.add(c6);
        repository.saveAll(computers);
    }

    @Test
    public void testMultiGet() {
        IdsQueryBuilder builder = new IdsQueryBuilder();
        NativeSearchQuery search = new NativeSearchQuery(builder);
        List<String> ids = new ArrayList<>();
        ids.add("1");
        ids.add("2");
        ids.add("3");
        search.setIds(ids);
        LinkedList<Computer> computers = template.multiGet(search, Computer.class);
        System.out.println(computers);
    }

    @Test
    public void testBulkIndex() {
        List<IndexQuery> indexQueries = new ArrayList<>();
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setId("7");
        indexQuery.setIndexName("computer");
        indexQuery.setType("computer");
        Computer c1 = new Computer(7L, "Mi Air", 3000.0, "Mi");
        indexQuery.setSource(JSON.toJSONString(c1));
        indexQueries.add(indexQuery);
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setIndexName("8");
        indexQuery2.setIndexName("computer");
        indexQuery2.setType("computer");
        Computer c2 = new Computer(8L, "Mi Pro", 5000.0, "Mi");
        indexQuery2.setSource(JSON.toJSONString(c2));
        indexQueries.add(indexQuery2);
        template.bulkIndex(indexQueries);
    }

    @Test
    public void bulkUpdateAndUpsert() {
        UpdateRequest updateRequest = new UpdateRequest();
        UpdateRequest updateRequest2 = new UpdateRequest();
        UpdateRequest updateRequest3 = new UpdateRequest();
        Computer c4 = new Computer(4L, "NP350XAA", 4611.0, "Samsung");
        Computer c5 = new Computer(5L, "MateBook", 4611.0, "HuaWei");
        Computer c6 = new Computer(6L, "MateBook Pro", 6611.0, "HuaWei");
        updateRequest.doc(JSON.toJSONString(c4), XContentType.JSON);
        updateRequest2.doc(JSON.toJSONString(c5), XContentType.JSON);
        updateRequest3.doc(JSON.toJSONString(c6), XContentType.JSON);
        UpdateQuery updateQuery1 = new UpdateQuery();
        updateQuery1.setDoUpsert(true);
        updateQuery1.setId("4");
        updateQuery1.setIndexName("computer");
        updateQuery1.setType("computer");
        updateQuery1.setUpdateRequest(updateRequest);
        UpdateQuery updateQuery2 = new UpdateQuery();
        updateQuery2.setDoUpsert(true);
        updateQuery2.setId("5");
        updateQuery2.setIndexName("computer");
        updateQuery2.setType("computer");
        updateQuery2.setUpdateRequest(updateRequest2);
        UpdateQuery updateQuery3 = new UpdateQuery();
        updateQuery3.setDoUpsert(true);
        updateQuery3.setId("6");
        updateQuery3.setIndexName("computer");
        updateQuery3.setType("computer");
        updateQuery3.setUpdateRequest(updateRequest3);
        List<UpdateQuery> updateQueries = new ArrayList<>();
        updateQueries.add(updateQuery1);
        updateQueries.add(updateQuery2);
        updateQueries.add(updateQuery3);
        template.bulkUpdate(updateQueries);
    }

    @Test
    public void testSearchTemplate() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("from", "0");
        params.put("size", "1");
        params.put("tag", "Apple");
        Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, "myScript.mustache", params);
        builder.withQuery(QueryBuilders.scriptQuery(script));
        Page<Computer> search = repository.search(builder.build());
        for (Computer computer : search) {
            System.out.println(computer);
        }
    }

    @Test
    public void testScroll() {
        NativeSearchQuery builder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withPageable(PageRequest.of(0, 1))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC))
                .build();
        int count = 1;
        AggregatedPageImpl<Computer> computers = (AggregatedPageImpl<Computer>) template.startScroll(30000L, builder, Computer.class);
        String scrollId = null;
        do {
            scrollId = computers.getScrollId();
            System.out.println("--------第" + count++ + "次scroll---------");
            for (Computer computer : computers) {
                System.out.println(computer);
            }
            computers = (AggregatedPageImpl<Computer>) template.continueScroll(scrollId, 30000L, Computer.class);
        } while (computers.getNumberOfElements() > 0);
        template.clearScroll(scrollId);
    }
}
