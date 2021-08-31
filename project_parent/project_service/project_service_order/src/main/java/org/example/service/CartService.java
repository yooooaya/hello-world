package org.example.service;

import java.util.Map;

public interface CartService {
    public void add(String skuId, Integer num,String username);
    public Map list(String username);
}
