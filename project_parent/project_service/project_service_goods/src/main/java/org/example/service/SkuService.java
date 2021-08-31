package org.example.service;

import org.example.goods.pojo.Goods;
import org.example.goods.pojo.Sku;
import org.example.goods.pojo.Spu;
import org.example.pojo.Result;

import java.util.List;
import java.util.Map;

public interface SkuService {
    void add(Goods goods);
    Sku findById(String id);
    public List<Spu> findSpu();
    public Goods findGoodById(String key);
    public void audit(String id);
    void update(Goods goods);
    public void pull(String id);
    public void put(String id);
    public void delete(String id);
    public void restore(String id);
    public void realDelete(String id);
    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<Sku> findList(Map<String, Object> searchMap);
    /***
     * 库存递减
     * @param username
     */
    boolean decrCount(String username);
}
