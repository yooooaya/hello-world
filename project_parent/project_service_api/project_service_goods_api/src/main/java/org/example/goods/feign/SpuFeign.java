package org.example.goods.feign;

import org.example.goods.pojo.Spu;
import org.example.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "goods")
public interface SpuFeign {
    @GetMapping("/sku/spu/findSpuById/{id}")
    public Result<Spu> findSpuById(@PathVariable("id") String id);
}
