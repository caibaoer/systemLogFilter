package com.huangpan.systemlogfilter.controller;



import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * @desc：
 * @title：HelloController
 * @author: huangwencai
 * @date: 2019-08-30 10:05
 * @version: v4.40.0
 */
@RestController
@Validated
public class HelloController {

    //测试请求地址：http://localhost:8080/hello11?id=5

    @GetMapping(value = {"/hello11","/helloDemo11"})
    public  String say(@RequestParam(value = "id")  String id){
        return id;
    }

}