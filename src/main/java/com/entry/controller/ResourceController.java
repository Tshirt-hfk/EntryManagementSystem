package com.entry.controller;

import com.entry.dto.BaseResultFactory;
import com.entry.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Controller
public class ResourceController {

    @Value("${file.upload-path}")
    private String filePath;

    @Value("${file.show-url}")
    private String showUrl;

    @Value("${server.port}")
    private String port;

    @Value("${file.server.ip}")
    private String ip;

    /**
     * 图片上传
     * @param file
     * @return
     */
    @PostMapping("/resource/image")
    @CrossOrigin
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String newName = FileUtil.saveFile(file,filePath,fileName);
            if(newName==null)
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"上传失败"), HttpStatus.NOT_FOUND);
            HashMap<String,String> result=new HashMap<>();
            result.put("url",ip+showUrl+newName);
            return new ResponseEntity<>(BaseResultFactory.build(result), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }
}
