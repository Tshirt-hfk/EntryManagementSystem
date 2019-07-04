package com.entry.controller;


import com.entry.entity.neo4j.Category;
import com.entry.entity.neo4j.Entry;
import com.entry.dto.BaseResultFactory;
import com.entry.repository.neo4j.CategoryRepository;
import com.entry.repository.neo4j.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpServletRequest;

@RestController
public class EntryController {

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    CategoryRepository categoryRepository;



}
