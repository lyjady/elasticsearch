package com.example.elasticsearch.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author linyongjin
 * @date 2019/6/23 20:32
 */
@Document(indexName = "news", shards = 1, replicas = 0)
public class News {

    @Id
    private long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", store = true)
    private String title;

    @Field(type = FieldType.Date, index = false, store = true)
    private Date eventTime;

    @Field(type = FieldType.Keyword, store = true)
    private String position;

    @Field(type = FieldType.Keyword, store = true)
    private String analysisType;

    @Field(type = FieldType.Keyword, store = true)
    private String level;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String way;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String origin;

    @Field(type = FieldType.Integer, store = true)
    private int areaId;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", store = true)
    private String conetents;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String attachUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getConetents() {
        return conetents;
    }

    public void setConetents(String conetents) {
        this.conetents = conetents;
    }

    public String getAttachUrl() {
        return attachUrl;
    }

    public void setAttachUrl(String attachUrl) {
        this.attachUrl = attachUrl;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", eventTime=" + eventTime +
                ", position='" + position + '\'' +
                ", analysisType='" + analysisType + '\'' +
                ", level='" + level + '\'' +
                ", way='" + way + '\'' +
                ", origin='" + origin + '\'' +
                ", areaId=" + areaId +
                ", conetents='" + conetents + '\'' +
                ", attachUrl='" + attachUrl + '\'' +
                '}';
    }
}
