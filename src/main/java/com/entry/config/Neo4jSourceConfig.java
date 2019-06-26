package com.entry.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import javax.persistence.Entity;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.entry.repository.neo4j")
@EntityScan(basePackages = "com.entry.entity.neo4j")
public class Neo4jSourceConfig {

}
