package org.example.controller;

import org.example.pojo.Brand;
import org.example.pojo.Result;
import org.example.pojo.Spec;
import org.example.pojo.StatusCode;
import org.example.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spec")
public class SpecController {
    @Autowired
    private SpecService specService;
    @GetMapping(value = "/searchByCategoryName/{name}")
    public Result findByCategoryName(@PathVariable String name){
        List<Map> specList= specService.findByCategoryName(name);
        if(specList.size()>0){
            return new Result(true, StatusCode.OK,"查询成功",specList);
        }
        return new Result(false, StatusCode.OK,"没有数据",specList);
    }
}
