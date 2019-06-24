package com.entry.repository;

import com.entry.model.Entry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;


public interface EntryRepository extends Neo4jRepository<Entry, Long> {

    @Query("MATCH (entry:Entry) WHERE id(entry)={0} RETYRN entry")
    Entry findEntryById(Long id);

    @Query("MATCH (entry:Entry) WHERE name(entry)={0} RETYRN entry")
    Entry findEntryByName(String name);

}
