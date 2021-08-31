package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.goods.pojo.Spec;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface SpecDao extends Mapper<Spec> {
    @Select("select id,name,options,seq,template_id from tb_spec where template_id in(select template_id from tb_category where name=#{name})")
    public List<Map> findByCategoryName(@Param("name") String name);
}
