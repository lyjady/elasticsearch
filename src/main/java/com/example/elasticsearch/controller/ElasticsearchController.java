package com.example.elasticsearch.controller;

import com.example.elasticsearch.domain.News;
import com.example.elasticsearch.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author linyongjin
 * @date 2019/6/26 10:25
 */
@RestController
public class ElasticsearchController {

    @Autowired
    NewsRepository newsRepository;


    /**
    * @author linyongjin
    * @param map
    * @return java.util.List<com.example.elasticsearch.domain.News>
    * @date 10:46 2019/6/26
    * @description 更具条件查询事件信息
    **/
    @RequestMapping("/queryNewsByCondition")
    public List<News> queryNewsByCondition(@RequestParam Map<String, Object> map) {
        return null;
    }

    @RequestMapping("receive")
    public void getMessage(@RequestBody List<Map<String, String>> map) {
        System.out.println(map);
    }
}
