package org.example.service;

import org.example.pojo.Goods;
import org.example.pojo.Spu;

import java.util.List;

public interface GoodsService {
    void add(Goods goods);
    public List<Spu> findSpu();
    public Goods findGoodById(String key);
    public void audit(String id);
    void update(Goods goods);
    public void pull(String id);
    public void put(String id);
    public void delete(String id);
    public void restore(String id);
    public void realDelete(String id);
}
