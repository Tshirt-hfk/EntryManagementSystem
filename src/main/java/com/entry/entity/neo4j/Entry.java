package com.entry.entity.neo4j;

import javax.persistence.*;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Entry {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Long getId() {
        return this.id;
    }

}