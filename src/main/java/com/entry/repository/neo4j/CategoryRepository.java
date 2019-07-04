package com.entry.repository.neo4j;

import com.entry.entity.neo4j.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CategoryRepository extends Neo4jRepository<Category, Long> {

    @Query(value = "MATCH(category:Category) WHERE id(category)={0} return category")
    Category findCategoryById(Long id);

    @Query(value = "MATCH(category:Category) WHERE name(category)={0} return category")
    Category findCategoryByName(String name);

}
