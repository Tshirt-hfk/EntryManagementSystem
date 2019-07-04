package com.entry.entity.neo4j;

import javax.persistence.*;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Entry {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String content;

    public Entry(){

    }

    public Entry(String name,String content){
        this.name=name;
        this.content=content;
    }

    public String getName() {
        return this.name;
    }

    public String getContent(){
        return this.content;
    }

}