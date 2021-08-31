package org.example.controller;

import org.example.config.TokenDecode;
import org.example.pojo.Order;
import org.example.pojo.Result;
import org.example.pojo.StatusCode;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private TokenDecode tokenDecode;
    /***
     * 新增Order数据
     * @param order
     * @return
     */
    @PostMapping(value = "/add")
    public Result add(@RequestBody Order order){
        //获取用户名
        Map<String, String> userMap = tokenDecode.getUserInfo();
        String username = userMap.get("username");
        //设置购买用户
        order.setUsername(username);
        orderService.add(order);
        return new Result(true, StatusCode.OK,"添加成功");
    }
}
