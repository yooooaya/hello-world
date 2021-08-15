package org.example.controller;

import org.example.pojo.*;
import org.example.service.BrandService;
import org.example.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
//    @GetMapping
//    public Result findAll(){
//        List<Brand> brandList= goodsService.findAll();
//        if(brandList.size()>0){
//            return new Result(true, StatusCode.OK,"查询成功",brandList);
//        }
//        return new Result(false, StatusCode.OK,"没有数据",brandList);
//    }
//    @GetMapping("/{id}")
//    public Result findById(@PathVariable Integer id){
//
//        Brand brand = brandService.findById(id);
//        if(brand!=null){
//            return new Result(true,StatusCode.OK,"查询成功",brand);
//        }
//        return new Result(false,StatusCode.OK,"没有数据");
//    }
    @PostMapping
    public Result add(@RequestBody Goods goods){
        goodsService.add(goods);
        return new Result(true,StatusCode.OK,"已添加成功");
    }
    @GetMapping
    public Result findSpu(){

        List<Spu> spuList=goodsService.findSpu();
        return new Result(true,StatusCode.OK,"已添加成功",spuList);
    }
    @GetMapping("/{id}")
    public Result findSpuOne(@PathVariable String id){

        Goods goods=goodsService.findGoodById(id);
        return new Result(true,StatusCode.OK,"已查询成功",goods);
    }
    @PutMapping
    public Result update(@RequestBody Goods goods){
        goodsService.update(goods);
        return new Result(true,StatusCode.OK,"修改成功");
    }
    /**
     * 审核
     * @param id
     * @return
     */
    @PutMapping("/audit/{id}")
    public Result audit(@PathVariable String id){
        goodsService.audit(id);
        return new Result(true,StatusCode.OK,"审核通过");
    }
    @PutMapping("/pull/{id}")
    public Result pull(@PathVariable String id){
        goodsService.pull(id);
        return new Result(true,StatusCode.OK,"下架完成");
    }
    @PutMapping("/put/{id}")
    public Result put(@PathVariable String id){
        goodsService.put(id);
        return new Result(true,StatusCode.OK,"上架完成");
    }
    @PutMapping("/delete/{id}")
    public Result delete(@PathVariable String id){
        goodsService.delete(id);
        return new Result(true,StatusCode.OK,"删除完成");
    }
    @PutMapping("/restore/{id}")
    public Result restore(@PathVariable String id){
        goodsService.restore(id);
        return new Result(true,StatusCode.OK,"已恢复数据");
    }
    @DeleteMapping("/realDelete/{id}")
    public Result realDelete(@PathVariable String id){
        goodsService.realDelete(id);
        return new Result(true,StatusCode.OK,"已完全删除数据");
    }
//    @PutMapping(value="/{id}")
//    public Result update(@RequestBody Brand brand,@PathVariable Integer id){
//        brand.setId(id);
//        int num=brandService.update(brand);
//        return new Result(true,StatusCode.OK,"已更新"+num+"条数据");
//    }
//    @DeleteMapping(value = "/{id}" )
//    public Result delete(@PathVariable Integer id){
//        int num=brandService.delete(id);
//        return new Result(true,StatusCode.OK,"已删除"+num+"条数据");
//    }
}
