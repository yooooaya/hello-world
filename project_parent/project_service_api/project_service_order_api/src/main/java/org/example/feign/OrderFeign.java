package org.example.feign;

import org.example.pojo.Order;
import org.example.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="order")
public interface OrderFeign {
    @PostMapping("/order/add")
    public Result add(@RequestBody Order order);
    @GetMapping("/order/{id}")
    public Result<Order> findById(@PathVariable("id") String orderId);
}
