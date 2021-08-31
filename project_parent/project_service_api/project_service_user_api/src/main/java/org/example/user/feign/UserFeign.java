package org.example.user.feign;


import org.example.pojo.Result;
import org.example.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="user")
public interface UserFeign {
    @GetMapping("/user/load/{username}")
    public User findUserInfo(@PathVariable("username") String username);
    @GetMapping("/user")
    public Result findAll();
}
