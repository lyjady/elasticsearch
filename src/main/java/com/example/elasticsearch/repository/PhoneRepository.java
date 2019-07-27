package com.example.elasticsearch.repository;

import com.example.elasticsearch.domain.Phone;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author linyongjin
 * @date 2019/7/25 20:13
 */
@Repository
public interface PhoneRepository extends ElasticsearchRepository<Phone, Long> {

}
