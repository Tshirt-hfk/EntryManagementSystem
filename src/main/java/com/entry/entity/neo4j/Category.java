package com.entry.entity.neo4j;

import javax.persistence.*;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String introduce;

    public Category(){

    }

    public Category(String name, String introduce){
        this.name=name;
        this.introduce=introduce;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getName(){
        return name;
    }

}
