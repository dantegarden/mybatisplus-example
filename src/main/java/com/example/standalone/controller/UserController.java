package com.example.standalone.controller;

import com.example.standalone.model.entity.User;
import com.example.standalone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Callable;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/findall")
    public Callable<ResponseEntity> getUser(){
        Callable<ResponseEntity> callable = new Callable<ResponseEntity>() {
            @Override
            public ResponseEntity call() throws Exception {
                ResponseEntity respEntity =  ResponseEntity.ok(userService.lambdaQuery().list());
                return respEntity;
            }
        };
        return callable;
    }

}
