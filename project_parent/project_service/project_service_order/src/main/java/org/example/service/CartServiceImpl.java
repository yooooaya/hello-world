package org.example.service;

import org.example.goods.feign.SkuFeign;
import org.example.goods.feign.SpuFeign;
import org.example.goods.pojo.Sku;
import org.example.goods.pojo.Spu;
import org.example.pojo.OrderItem;
import org.example.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService{
    public static final String CART="Cart_";
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SpuFeign spuFeign;
    /**
     * 添加购物车
     * @param skuId
     * @param num
     */
    @Override
    public void add(String skuId, Integer num, String username) {
        /**
         * 1）查询redis中的数据
         * 2）如果redis中已经有了，则追加数量，重新计算金额
         * 3）如果没有，将商品添加到缓存
         */
        OrderItem orderItem = (OrderItem) redisTemplate.boundHashOps(CART+username).get(skuId);
        if (orderItem != null){
            //存在，刷新购物车
            orderItem.setNum(orderItem.getNum()+num);
            if(orderItem.getNum()<=0){
                redisTemplate.boundHashOps(CART+username).delete(skuId);
                return;
            }
            orderItem.setMoney(orderItem.getNum()*orderItem.getPrice());
            orderItem.setPayMoney(orderItem.getNum()*orderItem.getPrice());
        }else{
            //不存在，新增购物车
            Result<Sku> skuResult = skuFeign.findById(skuId);
            Sku sku = skuResult.getData();
            Result<Spu> spuResult = spuFeign.findSpuById(sku.getSpuId());
            Spu spu=spuResult.getData();
            //将SKU转换成OrderItem
            orderItem = this.sku2OrderItem(sku,spu,num);
        }
        //存入redis
        redisTemplate.boundHashOps(CART+username).put(skuId,orderItem);
    }

    public Map list(String username){
        Map map=new HashMap();

        List<OrderItem> orderItemList=redisTemplate.boundHashOps(CART+username).values();

        map.put("orderItemList",orderItemList);
        //商品数量与总价格
        Integer totalNum = 0;
        Integer totalMoney = 0;

        for (OrderItem orderItem : orderItemList) {
            totalNum +=orderItem.getNum();
            totalMoney+=orderItem.getMoney();
        }
        map.put("totalNum",totalNum);
        map.put("totalMoney",totalMoney);
        return map;
    }


    //sku转换为orderItem
    private OrderItem sku2OrderItem(Sku sku, Spu spu, Integer num) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(num*orderItem.getPrice());       //单价*数量
        orderItem.setPayMoney(num*orderItem.getPrice());    //实付金额
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight()*num);           //重量=单个重量*数量

        //分类ID设置
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}
