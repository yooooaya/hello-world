package org.example.service;

import com.github.pagehelper.Page;
import org.example.goods.pojo.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    public List<Brand> findAll();

    public Brand findById(Integer id);

    public int add(Brand brand);

    public int update(Brand brand);

    public int delete(Integer id);

    public List<Brand> findList(Map<String, Object> searchMap);

    public Page<Brand> findPage(int page, int size);

    Page<Brand> findPage(Map<String, Object> searchMap, int page, int size);

    public List<Brand> findByCategoryName(String name);
}
