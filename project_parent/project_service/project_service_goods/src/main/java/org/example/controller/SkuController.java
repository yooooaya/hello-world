package org.example.controller;


import org.example.goods.pojo.Category;
import org.example.goods.pojo.Goods;
import org.example.goods.pojo.Sku;
import org.example.goods.pojo.Spu;
import org.example.pojo.StatusCode;
import org.example.service.CategoryService;
import org.example.service.SkuService;
import org.example.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.pojo.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sku")
public class SkuController {
    @Autowired
    private SkuService skuService;

    @Autowired
    private SpuService spuService;

    @Autowired
    private CategoryService categoryService;
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
        skuService.add(goods);
        return new Result(true, StatusCode.OK,"已添加成功");
    }
    @GetMapping
    public Result findSpu(){

        List<Spu> spuList= skuService.findSpu();
        return new Result(true,StatusCode.OK,"已添加成功",spuList);
    }
    @GetMapping("/goods/{id}")
    public Result findSpuOne(@PathVariable String id){

        Goods goods= skuService.findGoodById(id);
        return new Result(true,StatusCode.OK,"已查询成功",goods);
    }
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id){

        Sku sku= skuService.findById(id);
        return new Result(true,StatusCode.OK,"已查询成功",sku);
    }
    @PutMapping
    public Result update(@RequestBody Goods goods){
        skuService.update(goods);
        return new Result(true,StatusCode.OK,"修改成功");
    }
    /**
     * 审核
     * @param id
     * @return
     */
    @PutMapping("/audit/{id}")
    public Result audit(@PathVariable String id){
        skuService.audit(id);
        return new Result(true,StatusCode.OK,"审核通过");
    }
    @PutMapping("/pull/{id}")
    public Result pull(@PathVariable String id){
        skuService.pull(id);
        return new Result(true,StatusCode.OK,"下架完成");
    }
    @PutMapping("/put/{id}")
    public Result put(@PathVariable String id){
        skuService.put(id);
        return new Result(true,StatusCode.OK,"上架完成");
    }
    @PutMapping("/delete/{id}")
    public Result delete(@PathVariable String id){
        skuService.delete(id);
        return new Result(true,StatusCode.OK,"删除完成");
    }
    @PutMapping("/restore/{id}")
    public Result restore(@PathVariable String id){
        skuService.restore(id);
        return new Result(true,StatusCode.OK,"已恢复数据");
    }
    @DeleteMapping("/realDelete/{id}")
    public Result realDelete(@PathVariable String id){
        skuService.realDelete(id);
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
    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap){
        List<Sku> list = skuService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

@GetMapping("/spu/{spuId}")
public List<Sku> findSkuListBySpuId(@PathVariable("spuId") String spu_id){
    Map<String,Object> searchMap = new HashMap<>();

    if (!"all".equals(spu_id)){
        searchMap.put("spuId",spu_id);
    }
    //设置审核状态为1（审核通过）
    searchMap.put("status","1");
    List<Sku> skuList = skuService.findList(searchMap);
    System.out.println(skuList.get(0));
    return skuList;
}
//    @PostMapping("/decr/count")
//    public Result decrCount(@RequestParam("username") String username){
//        skuService.decrCount(username);
//        return new Result(true,StatusCode.OK,"库存扣减成功");
//    }
//    @RequestMapping("/resumeStockNum")
//    public Result resumeStockNum(@RequestParam("skuId") String skuId,@RequestParam("num")Integer num){
//        skuService.resumeStockNum(skuId,num);
//        return new Result(true,StatusCode.OK,"回滚库存成功");
//    }
    @GetMapping("/category/{id}")
    public Result<Category> findCategoryById(@PathVariable("id") Integer id){
        Category category=categoryService.findById(id);
        return new Result<>(true,StatusCode.OK,"查询类",category);
    }

    @GetMapping("/spu/findSpuById/{id}")
    public Result<Spu> findSpuById(@PathVariable("id") String id){
        Spu spu= spuService.findById(id);
        return new Result<>(true,StatusCode.OK,"查询类",spu);
    }

    /***
     * 库存递减
     * @param username
     * @return
     */
    @PostMapping(value = "/decr/count")
    public boolean decrCount(@RequestParam("username") String username){
        //库存递减
        boolean flag= skuService.decrCount(username);
        return flag;
    }
}
