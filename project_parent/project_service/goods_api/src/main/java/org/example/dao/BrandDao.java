package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.Brand;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface BrandDao extends Mapper<Brand> {
    /**
     * 根据分类名称查询品牌列表
     * @param
     * @return
     */
    @Select("select *from tb_brand where id in(select brand_id from tb_category_brand where category_id in(select id from tb_category WHERE name=#{name}))")
    public List<Brand> findByCategoryName(@Param("name") String name);
}
