package org.example.dao;

import org.example.goods.pojo.CategoryBrand;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface CategoryBrandDao extends Mapper<CategoryBrand> {
}
