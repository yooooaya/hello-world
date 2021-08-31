package org.example.goods.feign;


import org.example.goods.pojo.Sku;
import org.example.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//goods服务
@FeignClient(name="goods")
public interface SkuFeign {
    @GetMapping("/sku/spu/{spu_id}")
    public List<Sku> findSkuListBySpuId(@PathVariable("spu_id") String spu_id);
    @GetMapping("/sku/{id}")
    public Result<Sku> findById(@PathVariable("id")String id);
    @PostMapping("/sku/decr/count")
    public boolean decrCount(@RequestParam("username") String username);

    @RequestMapping("/sku/resumeStockNum")
    public Result resumeStockNum(@RequestParam("sku_id") String skuId,@RequestParam("num")Integer num);
}
