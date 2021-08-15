package org.example.dao;

import org.example.pojo.Category;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface CategoryDao extends Mapper<Category> {
}
