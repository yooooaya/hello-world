package org.example.dao;


import org.example.pojo.OrderItem;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface OrderItemMapper extends Mapper<OrderItem> {

}
