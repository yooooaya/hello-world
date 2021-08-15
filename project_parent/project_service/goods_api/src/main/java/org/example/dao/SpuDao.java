package org.example.dao;

import org.example.pojo.Spu;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface SpuDao extends Mapper<Spu> {
}
