package org.example.dao;

import org.example.pojo.Sku;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface SkuDao extends Mapper<Sku> {
}
