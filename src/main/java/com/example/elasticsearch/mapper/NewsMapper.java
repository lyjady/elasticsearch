package com.example.elasticsearch.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author linyongjin
 * @date 2019/6/23 20:28
 */
@Mapper
public interface NewsMapper {

    int queryCount();
}
