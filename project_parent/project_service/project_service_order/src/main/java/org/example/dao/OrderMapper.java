package org.example.dao;

import org.example.pojo.Order;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface OrderMapper extends Mapper<Order> {

}
