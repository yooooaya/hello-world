package org.example.controller;

import org.example.pojo.Result;
import org.example.pojo.StatusCode;
import org.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    //@Autowired
    //private TokenDecode tokenDecode;
    /**
     * 添加购物车
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping("/add")
    public Result add(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num){

        //String username=tokenDecode.getUserInfo().get("username");
        String username="changgou";
        cartService.add(skuId,num,username);

        return new Result(true, StatusCode.OK,"加入购物车成功");

    }
    /***
     * 查询用户购物车列表
     * @return
     */
    @GetMapping(value = "/list")
    public Map list(){
        //String username=tokenDecode.getUserInfo().get("username");
        String username="changgou";
        Map orderItem=cartService.list(username);
        return orderItem;
    }
}
