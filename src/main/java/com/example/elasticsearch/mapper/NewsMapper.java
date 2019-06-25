package com.example.elasticsearch.mapper;

import com.example.elasticsearch.domain.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author linyongjin
 * @date 2019/6/23 20:28
 */
@Mapper
public interface NewsMapper {

    int queryCount();

    @Select("select * from coc_event_top")
    List<News> queryNews();
}
