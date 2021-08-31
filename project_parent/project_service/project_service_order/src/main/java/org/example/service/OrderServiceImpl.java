package org.example.service;

import com.alibaba.fastjson.JSON;
import org.example.config.RabbitMQConfig;
import org.example.dao.OrderItemMapper;
import org.example.dao.OrderMapper;
import org.example.dao.TaskMapper;
import org.example.goods.feign.SkuFeign;
import org.example.pojo.Order;
import org.example.pojo.OrderItem;
import org.example.pojo.Task;
import org.example.util.IdWorker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private CartService cartService;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired

    private TaskMapper taskMapper;

    @Autowired

    private RabbitTemplate rabbitTemplate;
    /**
     * 增加
     * @param order
     * @return
     */
    @Override
    public String add(Order order){
        //1)获取所有购物项
        Map cartMap = cartService.list(order.getUsername());
        List<OrderItem> orderItemList = (List<OrderItem>) cartMap.get("orderItemList");
        //3）填充订单数据并保存
        order.setTotalNum((Integer) cartMap.get("totalNum"));
        order.setTotalMoney((Integer) cartMap.get("totalMoney"));
        order.setPayMoney((Integer) cartMap.get("totalMoney"));
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setBuyerRate("0");        //0:未评价，1：已评价
        order.setSourceType("1");       //来源，1：WEB
        order.setOrderStatus("0");      //0:未完成,1:已完成，2：已退货
        order.setPayStatus("0");        //0:未支付，1：已支付，2：支付失败
        order.setConsignStatus("0");    //0:未发货，1：已发货，2：已收货
        String orderId=idWorker.nextId()+"";
        order.setId(orderId);
        int count = orderMapper.insertSelective(order);
        //添加订单明细
        for (OrderItem orderItem : orderItemList) {
            orderItem.setId(idWorker.nextId()+"");
            orderItem.setIsReturn("0");
            orderItem.setOrderId(order.getId());
            orderItemMapper.insertSelective(orderItem);
        }
        //库存减少
        boolean b=skuFeign.decrCount(order.getUsername());

        //增加任务表记录
        Task task=new Task();
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setMqExchange(RabbitMQConfig.EX_BUYING_ADDPOINTUSER);
        task.setMqRoutingkey(RabbitMQConfig.CG_BUYING_ADDPOINT_KEY);
        Map map=new HashMap();
        map.put("username",order.getUsername() );
        map.put("orderId",order.getId());
        map.put("point",order.getPayMoney());
        task.setRequestBody(JSON.toJSONString(map));
        taskMapper.insertSelective(task);
        //5.清除Redis缓存购物车数据
        redisTemplate.delete("Cart_"+order.getUsername());
        //向订单生成队列中发送订单编号,同时该队列还设置了相应的过期时间10s,

        // 如果超时，会自动触发消息的转发，发送到Dead Letter Exchange中去

        //在queue.ordertimeout队列上也设置了过期时间，如果超时将被丢弃
        //rabbitTemplate.convertAndSend("","queue.ordercreate",orderId);

        return orderId;
    }
}
