package org.example.dao;

import org.example.pojo.CategoryBrand;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface CategoryBrandDao extends Mapper<CategoryBrand> {
}
