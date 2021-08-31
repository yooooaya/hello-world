package org.example.service;

import com.github.pagehelper.Page;
import org.example.dao.CategoryDao;
import org.example.goods.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryDao categoryDao;
    @Override
    public List<Category> findAll() {
        return null;
    }

    @Override
    public Category findById(Integer id) {
        return categoryDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(Category category) {

    }

    @Override
    public void update(Category category) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<Category> findList(Map<String, Object> searchMap) {
        return null;
    }

    @Override
    public Page<Category> findPage(int page, int size) {
        return null;
    }

    @Override
    public Page<Category> findPage(Map<String, Object> searchMap, int page, int size) {
        return null;
    }
}
