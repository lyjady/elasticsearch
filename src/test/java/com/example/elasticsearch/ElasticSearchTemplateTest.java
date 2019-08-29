package com.example.elasticsearch;

import com.example.elasticsearch.domain.Phone;
import com.example.elasticsearch.repository.PhoneRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author linyongjin
 * @date 2019/7/25 21:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchTemplateTest {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private PhoneRepository repository;

    @Test
    public void createIndex() {
        //创建索引
        template.createIndex(Phone.class);
        //映射索引
        template.putMapping(Phone.class);
    }

    @Test
    public void saveData() {
        Phone phone1 = new Phone(1, "iPhone 8", 6999, "Apple", "苹果手机");
        repository.save(phone1);
    }

    @Test
    public void saveAllData() {
        List<Phone> phones = new ArrayList<>();
        Phone phone1 = new Phone(2, "iPhone 8 Plus", 7999, "Apple", "苹果手机");
        Phone phone2 = new Phone(3, "iPhone X", 8999, "Apple", "苹果手机");
        Phone phone3 = new Phone(4, "iPhone XS", 9999, "Apple", "苹果手机");
        Phone phone4 = new Phone(5, "Samsung S8", 5999, "Samsung", "三星手机");
        Phone phone5 = new Phone(6, "Samsung S9", 6999, "Samsung", "三星手机");
        Phone phone6 = new Phone(7, "Samsung S10", 7999, "Samsung", "三星手机");
        Phone phone7 = new Phone(8, "HuaWei P8", 4999, "HuaWei", "华为手机");
        Phone phone8 = new Phone(9, "HuaWei P9", 5999, "HuaWei", "华为手机");
        Phone phone9 = new Phone(10, "HuaWei P10", 6999, "HuaWei", "华为手机");

        phones.add(phone1);
        phones.add(phone2);
        phones.add(phone3);
        phones.add(phone4);
        phones.add(phone5);
        phones.add(phone6);
        phones.add(phone7);
        phones.add(phone8);
        phones.add(phone9);

        repository.saveAll(phones);

    }

    @Test
    public void deleteData() {
        Phone phone = new Phone();
        phone.setId(1L);
        repository.deleteById(1L);
    }

    @Test
    public void updateData() {
        repository.save(new Phone());
    }

    @Test
    public void findData() {
        repository.findById(1L);
        Iterable<Phone> all = repository.findAll();
        for (Phone phone : all) {
            System.out.println(phone);
        }
    }

    @Test
    public void matchQuery() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("name", "iPhone"));
        //查询，search 默认就是分页查找
        Page<Phone> search = repository.search(builder.build());
        //总页数
        int totalPages = search.getTotalPages();
        //查询的总数
        long totalElements = search.getTotalElements();
        System.out.println("总页数:" + totalPages);
        System.out.println("总数量:" + totalElements);
        for (Phone phone : search) {
            System.out.println(phone);
        }
    }

    @Test
    public void termQuery() {
        //一般用来查询数值类型,因为term不分词.所以用来查询不分词的属性.
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.termQuery("price", 6999));
        Page<Phone> search = repository.search(builder.build());
        //总页数
        int totalPages = search.getTotalPages();
        //查询的总数
        long totalElements = search.getTotalElements();
        System.out.println("总页数:" + totalPages);
        System.out.println("总数量:" + totalElements);
        for (Phone phone : search) {
            System.out.println(phone);
        }
    }

    @Test
    public void pageQuery() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //查询全部
        builder.withQuery(QueryBuilders.matchAllQuery())
                //分页条件(page:等于from,size:等于size)
                .withPageable(PageRequest.of(1, 2));
        Page<Phone> search = repository.search(builder.build());
        //总页数
        int totalPages = search.getTotalPages();
        //查询的总数
        long totalElements = search.getTotalElements();
        //当前页数
        int number = search.getNumber();
        //每页数量
        int size = search.getSize();
        System.out.println("总页数:" + totalPages);
        System.out.println("总数量:" + totalElements);
        System.out.println("当前页数:" + number);
        System.out.println("每页数量:" + size);
        for (Phone phone : search) {
            System.out.println(phone);
        }
    }

    @Test
    public void sortQuery() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchAllQuery())
                .withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        Page<Phone> search = repository.search(builder.build());
        //总页数
        int totalPages = search.getTotalPages();
        //查询的总数
        long totalElements = search.getTotalElements();
        //当前页数
        int number = search.getNumber();
        //每页数量
        int size = search.getSize();
        System.out.println("总页数:" + totalPages);
        System.out.println("总数量:" + totalElements);
        System.out.println("当前页数:" + number);
        System.out.println("每页数量:" + size);
        for (Phone phone : search) {
            System.out.println(phone);
        }

    }

    @Test
    public void boolQuery() {
        //查询不是华为,最好是苹果,并且价格大于5000的手机
        //过滤条件为三星手机
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.boolQuery()
                    .must(QueryBuilders.rangeQuery("price").gt(5000))
                    .mustNot(QueryBuilders.matchQuery("tag", "HuaWei"))
                    .should(QueryBuilders.matchQuery("tag", "Apple")))
                .withFilter(QueryBuilders.matchQuery("tag", "Samsung"));
        Page<Phone> search = repository.search(builder.build());
        //总页数
        int totalPages = search.getTotalPages();
        //查询的总数
        long totalElements = search.getTotalElements();
        //当前页数
        int number = search.getNumber();
        //每页数量
        int size = search.getSize();
        System.out.println("总页数:" + totalPages);
        System.out.println("总数量:" + totalElements);
        System.out.println("当前页数:" + number);
        System.out.println("每页数量:" + size);
        for (Phone phone : search) {
            System.out.println(phone);
        }
    }

    @Test
    public void fuzzyQuery() {
       NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
       builder.withQuery(QueryBuilders.fuzzyQuery("name", "phone"));
        Page<Phone> search = repository.search(builder.build());
        //总页数
        int totalPages = search.getTotalPages();
        //查询的总数
        long totalElements = search.getTotalElements();
        //当前页数
        int number = search.getNumber();
        //每页数量
        int size = search.getSize();
        System.out.println("总页数:" + totalPages);
        System.out.println("总数量:" + totalElements);
        System.out.println("当前页数:" + number);
        System.out.println("每页数量:" + size);
        for (Phone phone : search) {
            System.out.println(phone);
        }
    }

    @Test
    public void aggregationQuery() {
        //聚合查询,先查询全部然后根据tag先进行聚合,在根据价格区间在聚合,最后求出平均价格
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchAllQuery())
                .addAggregation(AggregationBuilders.terms("group_tag").field("tag.raw")
                    .subAggregation(AggregationBuilders.range("range_price")
                        .field("price")
                        .addRange(5000, 6000)
                        .addRange(6001, 8000)
                        .addRange(8001, 10000)
                            .subAggregation(AggregationBuilders.avg("avg_price").field("price"))));
        AggregatedPage<Phone> result = (AggregatedPage<Phone>) repository.search(builder.build());

        //获得最外层的聚合
        StringTerms groupTag = (StringTerms) result.getAggregation("group_tag");
        //获得桶
        List<StringTerms.Bucket> buckets = groupTag.getBuckets();
        //遍历
        for (StringTerms.Bucket bucket : buckets) {
            String tag = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            System.out.println("品牌: " + tag + "----" + "数量:" + docCount);
            InternalRange internalRange = (InternalRange) bucket.getAggregations().asMap().get("range_price");
            List<InternalRange.Bucket> ranges = internalRange.getBuckets();
            for (InternalRange.Bucket range : ranges) {
                System.out.println(range.getFrom() + "----" +range.getTo());
                String rangeKey = range.getKeyAsString();
                long rangeCount = range.getDocCount();
                InternalAvg avg = (InternalAvg) range.getAggregations().asMap().get("avg_price");
                System.out.println("在价格区间" + range.getFrom() + "到" + range.getTo() + "有" + rangeCount + "个手机,平均价格是" + avg.getValue());
            }
        }
    }
}
