package com.iwbz.uploadsfile.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/uploads")
//几种不同的文件上传方式
public class FileUploadController {
    private static final Logger log = LoggerFactory
            .getLogger(FileUploadController.class);

    @GetMapping
    public String index(){
        return "index";
    }

    @PostMapping("/upload1")
    @ResponseBody
    //单个文件的上传
    public Map<String,String> upload1(
            @RequestParam("file")MultipartFile file) throws IOException {
        log.info("[文件类型]-[{}]",file.getContentType());
        log.info("[文件名称]-[{}]",file.getOriginalFilename());
        log.info("[文件大小]-[{}]",file.getSize());

        //将文件写入到指定目录中
        file.transferTo(new File("F:\\uploads\\singleuploads\\"
                +file.getOriginalFilename()));
        var result = new HashMap<String,String>(16);
        result.put("contentType",file.getContentType());
        result.put("fileName",file.getOriginalFilename());
        result.put("fileSize",file.getSize()+" ");
        return result;

    }

    @PostMapping("/upload2")
    @ResponseBody
    public List<Map<String,String>> upload2(
            @RequestParam("file") MultipartFile[] files) throws IOException {
        if (files==null || files.length==0){
            return null;
        }
        List<Map<String,String>> results = new ArrayList<>();
        //springmvc方式写入文件
        for (MultipartFile file: files) {
            file.transferTo(new File
                    ("F:\\uploads\\multiuploads\\"+file.getOriginalFilename()));
            var map = new HashMap<String,String>(16);
            map.put("contentType",file.getContentType());
            map.put("fileName",file.getOriginalFilename());
            map.put("fileSize",file.getSize()+" ");
            results.add(map);
        }
        return results;
    }

    @PostMapping("/upload3")
    @ResponseBody
    public void upload2(String base64) throws IOException {
        final var tempFile = new File("F:\\uploads\\test1\\test.jpg");
        var d = base64.split("base64,");

        final var bytes = Base64Utils.decodeFromString(d.length>1?d[1] : d[0]);
        FileCopyUtils.copy(bytes,tempFile);
    }
}
