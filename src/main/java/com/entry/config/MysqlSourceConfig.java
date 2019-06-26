package com.entry.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories( basePackages = "com.entry.repository.mysql") //设置Repository所在位置
@EntityScan(basePackages = "com.entry.entity.mysql")
public class MysqlSourceConfig {

}
