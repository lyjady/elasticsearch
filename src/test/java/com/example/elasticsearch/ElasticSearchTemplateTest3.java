package com.example.elasticsearch;

import com.example.elasticsearch.domain.Computer;
import com.example.elasticsearch.domain.Hotel;
import com.example.elasticsearch.repository.ComputerRepository;
import com.example.elasticsearch.repository.HotelRepository;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.InternalGeoDistance;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange;
import org.elasticsearch.search.aggregations.metrics.geobounds.InternalGeoBounds;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LinYongJin
 * @date 2019/9/1 13:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchTemplateTest3 {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private ComputerRepository computerRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public static <T> void printResult(Page<T> search) {
        for (T t : search) {
            System.out.println(t);
        }
    }

    @Test
    public void createIndex() {
        template.createIndex(Hotel.class);
        template.putMapping(Hotel.class);
    }

    @Test
    public void testMatchQuery() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("name", "MacBook"))
               .withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC));
        Page<Computer> search = computerRepository.search(builder.build());
        for (Computer computer : search) {
            System.out.println(computer);
        }
    }

    @Test
    public void testMultiMatchQuery() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.multiMatchQuery("MacBook Samsung", "name", "tag"))
               .withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC));
        Page<Computer> search = computerRepository.search(builder.build());
        printResult(search);
    }

    @Test
    public void testTermQuery() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.termQuery("tag", "Samsung"))
               .withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC));
        Page<Computer> search = computerRepository.search(builder.build());
        printResult(search);
    }

    @Test
    public void testBoolSearch() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("name", "MacBook"))
                    .mustNot(QueryBuilders.termQuery("tag", "HuaWei"))
                    .should(QueryBuilders.rangeQuery("price").gte(5000))
               .filter(QueryBuilders.matchQuery("name", "Pro")));
        Page<Computer> search = computerRepository.search(builder.build());
        printResult(search);
    }

    @Test
    public void createHotel() {
        GeoPoint point = new GeoPoint(40.42, -71.34);
        Hotel hotel = new Hotel(1L, "如家快捷酒店", "打造全国最好的连锁酒店", point);
        Hotel save = hotelRepository.save(hotel);
    }

    @Test
    public void testGeoBoundingBox() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.geoBoundingBoxQuery("position").setCorners(new GeoPoint(42.0,-72.0), new GeoPoint(40.0, -74.0)));
        Page<Hotel> search = hotelRepository.search(builder.build());
        printResult(search);
    }

    @Test
    public void testPrefixSearch() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.prefixQuery("name", "Mac"));
        Page<Computer> search = computerRepository.search(builder.build());
        printResult(search);
    }

    @Test
    public void testGeoPolygon() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        List<GeoPoint> points = new ArrayList<>();
        points.add(new GeoPoint(40.73, -74.1));
        points.add(new GeoPoint(40.01, -71.12));
        points.add(new GeoPoint(50.56, -90.58));
        builder.withQuery(QueryBuilders.geoPolygonQuery("position",points));
        Page<Hotel> search = hotelRepository.search(builder.build());
        printResult(search);
    }

    @Test
    public void testGeoDistance() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.geoDistanceQuery("position").distance(200, DistanceUnit.KILOMETERS).point(40.0, -70.0));
        Page<Hotel> search = hotelRepository.search(builder.build());
        printResult(search);
    }

    @Test
    public void testAggGeoDistance() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchAllQuery())
               .addAggregation(AggregationBuilders.geoDistance("group_by_position", new GeoPoint(40.0, -70.0))
                       .unit(DistanceUnit.KILOMETERS)
                       .distanceType(GeoDistance.PLANE)
                       .addRange(0.0, 100.0)
                       .addRange(101.0, 200.0)
                       .addRange(201.0, 300)
                       .field("position"));
        AggregatedPageImpl<Hotel> search = (AggregatedPageImpl<Hotel>) hotelRepository.search(builder.build());
        InternalRange result = (InternalRange) search.getAggregation("group_by_position");
        List<InternalRange.Bucket> buckets = result.getBuckets();
        for (InternalRange.Bucket bucket : buckets) {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            System.out.println(key + ": " + docCount);

        }
    }
}
