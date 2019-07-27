package com.example.elasticsearch;

import com.example.elasticsearch.domain.Item;
import com.example.elasticsearch.domain.News;
import com.example.elasticsearch.mapper.NewsMapper;
import com.example.elasticsearch.repository.NewsRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchApplicationTests {

    @Autowired
    private NewsMapper mapper;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private NewsRepository repository;

    @Test
    public void testMysqlConnection() {
        System.out.println(mapper.queryCount());
    }

    @Test
    public void createIndex() {
        //创建索引
        boolean index = template.createIndex(News.class);
        template.putMapping(News.class);
    }

    @Test
    public void createItemIndex() {
        template.createIndex(Item.class);
        template.putMapping(Item.class);
    }

    @Test
    public void deleteIndex() {
        template.deleteIndex(News.class);
    }

    //批量插入数据
    @Test
    public void insertNews() {
        List<News> news = mapper.queryNews();
        repository.saveAll(news);
    }

    //查询数据
    @Test
    public void queryNewsAll() {
        Iterable<News> news = repository.findAll();
        for (News n : news) {
            System.out.println(n);
        }
    }

    @Test
    public void findByTitle() {
        List<News> news = repository.findByTitle("福州宝龙城管不作为");
        System.out.println(news);
    }

    @Test
    public void findByTitleAndPosition() {
        List<News> news = repository.findByTitleAndPosition("福州宝龙城管不作为", "宝龙");
        System.out.println(news);
    }

    @Test
    public void findByTitleLike() {
        List<News> news = repository.findByTitleLike("影响");
        System.out.println(news);
    }

    @Test
    public void matchNews() {
        //match查询(分词)
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("title", "影响日常生活"));
        Page<News> news = repository.search(builder.build());
        long total = news.getTotalElements();
        int pages = news.getTotalPages();
        System.out.println("查询到" + total + "条记录");
        System.out.println("共" + pages + "页");
        for (News n : news) {
            System.out.println(n);
        }
    }

    @Test
    public void termNews() {
        //term查询(不分词)
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.termQuery("title", "影响日常生活"));
        Page<News> news = repository.search(builder.build());
        long total = news.getTotalElements();
        int pages = news.getTotalPages();
        System.out.println("查询到" + total + "条记录");
        System.out.println("共" + pages + "页");
        for (News n : news) {
            System.out.println(n);
        }
    }

    @Test
    public void boolNews() {
        //bool查询
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("title", "影响日常生活"))
                .mustNot(QueryBuilders.termQuery("level", "三级"))
        );
        Page<News> news = repository.search(builder.build());
        long total = news.getTotalElements();
        int pages = news.getTotalPages();
        System.out.println("查询到" + total + "条记录");
        System.out.println("共" + pages + "页");
        for (News n : news) {
            System.out.println(n);
        }
    }

    @Test
    public void fuzzyNews() {
        //fuzzy查询
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.fuzzyQuery("title", "影响"));
        Page<News> news = repository.search(builder.build());
        long total = news.getTotalElements();
        int pages = news.getTotalPages();
        System.out.println("查询到" + total + "条记录");
        System.out.println("共" + pages + "页");
        for (News n : news) {
            System.out.println(n);
        }
    }

    @Test
    public void pageNews() {
        //分页查询
        int pageSize = 10;
        int pageNum = 0;
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("title", "影响日常生活"));
        builder.withPageable(PageRequest.of(pageNum, pageSize));
        Page<News> news = repository.search(builder.build());
        System.out.println("总数" + news.getTotalElements());
        System.out.println("总页数" + news.getTotalPages());
        System.out.println("当前页" + news.getNumber());
        System.out.println("每页大小" + news.getSize());
        for (News n : news) {
            System.out.println(n);
        }
    }

    /**
     * News{id=104, title='虾溜外卖油烟直排小区严重影响楼上居民日常生活', eventTime=Sun Apr 07 00:00:00 CST 2019, position='鼓楼区西营里新村楼下虾溜外卖', analysisType='环保', level='三级', way='12345平台', origin='人工接收', areaId=2, conetents='null', attachUrl='(NULL)'}
     * News{id=148908, title='环境严重污染，影响群众生活', eventTime=Sun Apr 07 00:00:00 CST 2019, position='义洲', analysisType='环保', level='三级', way='视频采集', origin='人工接收', areaId=6, conetents='null', attachUrl='(NULL)'}
     * News{id=2047, title='占道经营油烟排放到小区里影响生活', eventTime=Tue Apr 09 00:00:00 CST 2019, position='前屿东路76号金辉东景', analysisType='民政(含社区)', level='三级', way='网格员', origin='人工接收', areaId=4, conetents='null', attachUrl='img/event/civiladministration/'}
     * News{id=30870, title='粉尘严重影响周边居民生活', eventTime=Sun Apr 07 00:00:00 CST 2019, position='鼓山镇樟林村', analysisType='环保', level='二级', way='网格员', origin='人工接收', areaId=4, conetents='null', attachUrl='(NULL)'}
     * News{id=29795, title='违规排气，晚间噪音严重影响居民生活', eventTime=Mon Apr 08 00:00:00 CST 2019, position='道山西路电影制片厂西侧店面', analysisType='环保', level='三级', way='12345平台', origin='人工接收', areaId=2, conetents='null', attachUrl='(NULL)'}
     * News{id=2519, title='1号楼下面餐饮店油烟排放不达标，严重影响楼上居民生活', eventTime=Mon Apr 08 00:00:00 CST 2019, position='快安路西侧3号安居佳苑', analysisType='环保', level='三级', way='网络舆情', origin='人工接收', areaId=5, conetents='null', attachUrl='(NULL)'}
     * News{id=71504, title='油烟污染楼上居民正常生活', eventTime=Sun Apr 07 00:00:00 CST 2019, position='福州市马尾区快安路50号', analysisType='环保', level='三级', way='12345平台', origin='人工接收', areaId=5, conetents='null', attachUrl='(NULL)'}
     * News{id=123275, title='影响交通及行车安全', eventTime=Sun Apr 07 00:00:00 CST 2019, position='福州市晋安区新东线', analysisType='交通', level='三级', way='网络舆情', origin='自动感知', areaId=4, conetents='null', attachUrl='img/event/traffaic/'}
     * News{id=71507, title='饭店油烟污染楼上居民正常生活', eventTime=Mon Apr 08 00:00:00 CST 2019, position='福州市马尾区快安路40', analysisType='环保', level='三级', way='12345平台', origin='人工接收', areaId=5, conetents='null', attachUrl='(NULL)'}
     * News{id=148813, title='严重影响行车安全', eventTime=Mon Apr 08 00:00:00 CST 2019, position='福州市晋安区北三环路', analysisType='交通', level='三级', way='12345平台', origin='人工接收', areaId=6, conetents='null', attachUrl='img/event/traffaic/'}
     */
    @Test
    public void sortNews() {
        //排序
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("title", "影响日常生活"));
        builder.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));
        Page<News> news = repository.search(builder.build());
        for (News n : news) {
            System.out.println(n);
        }
    }

    @Test
    public void countNews() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("title", "影响日常生活"));
        builder.addAggregation(
                AggregationBuilders.terms("analysisTypes").field("analysisType")
        );
        AggregatedPage<News> search = (AggregatedPage<News>) repository.search(builder.build());
        StringTerms terms = (StringTerms) search.getAggregation("analysisTypes");
        List<StringTerms.Bucket> buckets = terms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            System.out.println("类型: " + bucket.getKeyAsString() + ", 数量: " + bucket.getDocCount());
        }
    }

    @Test
    public void avgNews() {
        //求平均值
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("title", "影响日常生活"));
        builder.addAggregation(
                AggregationBuilders.terms("analysisTypes").field("analysisType")
                .subAggregation(AggregationBuilders.avg("avgId").field("id"))
        );
        AggregatedPage<News> search = (AggregatedPage<News>) repository.search(builder.build());
        StringTerms terms = (StringTerms) search.getAggregation("analysisTypes");
        List<StringTerms.Bucket> buckets = terms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            InternalAvg avg = (InternalAvg) bucket.getAggregations().asMap().get("avgId");
            System.out.println("类型:" + bucket.getKeyAsString() + ", 数量: " + bucket.getDocCount() + ", 平均id:" + avg);
        }
    }

    @Test
    public void sumNews() {
        //求总和
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("title", "影响日常生活"));
        builder.addAggregation(
                AggregationBuilders.terms("analysisTypes").field("analysisType")
                        .subAggregation(AggregationBuilders.sum("sumId").field("id"))
        );
        AggregatedPage<News> search = (AggregatedPage<News>) repository.search(builder.build());
        StringTerms terms = (StringTerms) search.getAggregation("analysisTypes");
        List<StringTerms.Bucket> buckets = terms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            InternalSum sum = (InternalSum) bucket.getAggregations().asMap().get("sumId");
            System.out.println("类型:" + bucket.getKeyAsString() + ", 数量: " + bucket.getDocCount() + ", id总和:" + sum);
        }
    }
}
