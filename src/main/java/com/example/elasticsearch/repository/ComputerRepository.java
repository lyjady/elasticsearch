package com.example.elasticsearch.repository;

import com.example.elasticsearch.domain.Computer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LinYongJin
 * @date 2019/8/29 20:02
 */
@Repository
public interface ComputerRepository extends ElasticsearchRepository<Computer, Long> {

}
