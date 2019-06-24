package com.entry.controller;

import com.entry.model.Entry;
import com.entry.dto.BaseResultFactory;
import com.entry.repository.EntryRepository;
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

    @GetMapping("/")
    @CrossOrigin
    public ResponseEntity<?> index(HttpServletRequest request){

        Entry entry = entryRepository.findEntryByName("hello world!");

        if (entry == null){
            entry = new Entry();
            entry.setName("hello world!");
            System.out.println(entry.getId());
            this.entryRepository.save(entry);
        }

        return new ResponseEntity<>(BaseResultFactory.build(entry), HttpStatus.OK);
    }

}
