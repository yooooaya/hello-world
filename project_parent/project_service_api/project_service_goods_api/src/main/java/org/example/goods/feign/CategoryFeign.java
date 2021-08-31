package org.example.goods.feign;

import org.example.goods.pojo.Category;
import org.example.goods.pojo.Spu;
import org.example.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "goods")
public interface CategoryFeign {
    @GetMapping("/sku/category/{id}")
    public Result<Category> findById(@PathVariable("id") Integer id);

}
