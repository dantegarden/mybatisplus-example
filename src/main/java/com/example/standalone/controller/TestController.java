package com.example.standalone.controller;

import com.google.common.collect.ImmutableList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.Callable;

@RequestMapping("/dict")
@RestController
public class TestController {

    @RequestMapping("/testapi")
    public Callable<ResponseEntity> testApi(){
        Callable<ResponseEntity> callable = new Callable<ResponseEntity>() {
            @Override
            public ResponseEntity call() throws Exception {
                ResponseEntity respEntity =  ResponseEntity.ok(ImmutableList.of(1,2,3,4,5));
                return respEntity;
            }
        };
        return callable;
    }
}
