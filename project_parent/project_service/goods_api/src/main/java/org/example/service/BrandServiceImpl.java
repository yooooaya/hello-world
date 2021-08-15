package org.example.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.dao.BrandDao;
import org.example.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService{
    @Autowired
    private BrandDao brandDao;
    @Override
    public List<Brand> findAll() {
        return brandDao.selectAll();
    }
    @Override
    public Brand findById(Integer id){
        return  brandDao.selectByPrimaryKey(id);
    }
    @Override
    public int add(Brand brand){

        return brandDao.insertSelective(brand);
    }
    @Override
    public int update(Brand brand){
        return brandDao.updateByPrimaryKeySelective(brand);
    }
    @Override
    public int delete(Integer id){
        return brandDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<Brand> findList(Map<String, Object> searchMap) {
        Example example=new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 品牌名称
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }
            // 品牌的首字母
            if(searchMap.get("letter")!=null && !"".equals(searchMap.get("letter"))){
                criteria.andEqualTo("letter",searchMap.get("letter"));
            }
        }
        return brandDao.selectByExample(example);
    }
    @Override
    public Page<Brand> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Brand>)brandDao.selectAll();
    }
    @Override
    public Page<Brand> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example=new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 品牌名称
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }
            // 品牌的首字母
            if(searchMap.get("letter")!=null && !"".equals(searchMap.get("letter"))){
                criteria.andEqualTo("letter",searchMap.get("letter"));
            }
        }
        return (Page<Brand>)brandDao.selectByExample(example);
    }

    @Override
    public List<Brand> findByCategoryName(String name) {
        return brandDao.findByCategoryName(name);
    }
}
