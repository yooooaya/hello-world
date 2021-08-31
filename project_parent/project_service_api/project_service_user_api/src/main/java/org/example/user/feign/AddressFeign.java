package org.example.user.feign;

import org.example.pojo.Result;
import org.example.user.pojo.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "user")
public interface AddressFeign {

    @GetMapping("/address/list")
    public Result<List<Address>> list();
}
