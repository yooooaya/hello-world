package org.example.controller;

import com.github.pagehelper.Page;
import org.example.pojo.Brand;
import org.example.pojo.PageResult;
import org.example.pojo.Result;
import org.example.pojo.StatusCode;
import org.example.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping
    public Result findAll(){
        List<Brand> brandList= brandService.findAll();
        if(brandList.size()>0){
            return new Result(true, StatusCode.OK,"查询成功",brandList);
        }
        return new Result(false, StatusCode.OK,"没有数据",brandList);
    }
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){

        Brand brand = brandService.findById(id);
        if(brand!=null){
            return new Result(true,StatusCode.OK,"查询成功",brand);
        }
        return new Result(false,StatusCode.OK,"没有数据");
    }
    @PostMapping
    public Result add(@RequestBody Brand brand){
        int num=brandService.add(brand);
        return new Result(true,StatusCode.OK,"已添加"+num+"条数据");
    }
    @PutMapping(value="/{id}")
    public Result update(@RequestBody Brand brand,@PathVariable Integer id){
        brand.setId(id);
        int num=brandService.update(brand);
        return new Result(true,StatusCode.OK,"已更新"+num+"条数据");
    }
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Integer id){
        int num=brandService.delete(id);
        return new Result(true,StatusCode.OK,"已删除"+num+"条数据");
    }
    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap){
        List<Brand> list = brandService.findList(searchMap);
        if(list.size()>0){
            return new Result(true,StatusCode.OK,"查询成功",list);
        }
        return new Result(true,StatusCode.OK,"没有相关数据");
    }
    @GetMapping(value = "/search/{page}/{size}" )
    public Result findPage(@PathVariable  int page, @PathVariable  int size){
        Page<Brand> pageList = brandService.findPage(page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }
    @GetMapping(value = "/searchBy/{page}/{size}" )
    public Result findPage(@RequestParam Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<Brand> pageList = brandService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }
    @GetMapping(value = "/searchByCategoryName/{name}")
    public Result findByCategoryName(@PathVariable String name){
        List<Brand> brandList= brandService.findByCategoryName(name);
        if(brandList.size()>0){
            return new Result(true, StatusCode.OK,"查询成功",brandList);
        }
        return new Result(false, StatusCode.OK,"没有数据",brandList);
    }
}
