package org.example.oauth;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class test {
    public static void main(String[] args) {
        Object obj= JSON.parseObject("{key=dfdf}", Map.class);
        System.out.println(obj);
    }
}
