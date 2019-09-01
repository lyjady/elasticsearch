package com.example.elasticsearch.repository;

import com.example.elasticsearch.domain.Hotel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LinYongJin
 * @date 2019/9/1 13:37
 */
@Repository
public interface HotelRepository extends ElasticsearchRepository<Hotel, Long> {
}
