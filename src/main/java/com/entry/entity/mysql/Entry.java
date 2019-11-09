package com.entry.entity.mysql;

import com.alibaba.fastjson.JSONArray;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Entry {

    @Column(columnDefinition = "INT default -1", nullable = false)
    protected Integer originalId;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    protected String entryName;

    @Column(columnDefinition ="varchar(255)", nullable = false)
    protected String field;

    @Column(columnDefinition = "TEXT")
    protected String intro;

    @Column
    protected String imageUrl;

    @Column(columnDefinition = "TEXT")
    protected String infoBox;

    @Column(columnDefinition = "TEXT")
    protected String content;

    @Column(columnDefinition = "TEXT")
    protected String relation;

    public Integer getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Integer originalId) {
        this.originalId = originalId;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public JSONArray getField() {
        return JSONArray.parseArray(field);
    }

    public void setField(JSONArray field) {
        if(field!=null)
            this.field = field.toJSONString();
        else
            this.field = (new JSONArray()).toJSONString();
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public JSONArray getInfoBox() {
        return JSONArray.parseArray(infoBox);
    }

    public void setInfoBox(JSONArray infoBox) {
        this.infoBox = infoBox.toJSONString();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public JSONArray getRelation() {
        return JSONArray.parseArray(this.relation);
    }

    public void setRelation(JSONArray relation) {
        this.relation = relation.toJSONString();
    }

}
