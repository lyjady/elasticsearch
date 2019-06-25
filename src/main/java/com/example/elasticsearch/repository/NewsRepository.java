package com.example.elasticsearch.repository;

import com.example.elasticsearch.domain.News;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends ElasticsearchRepository<News, Long> {

    List<News> findByTitle(String title);

    List<News> findByTitleAndPosition(String title, String poition);

    List<News> findByTitleLike(String title);
}
