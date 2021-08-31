package org.example.service;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.dao.*;
import org.example.goods.pojo.*;
import org.example.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    private SpuDao spuDao;


    @Override
    public List<Spu> findAll() {
        return null;
    }

    @Override
    public Spu findById(String id) {
        return spuDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(Goods goods) {

    }

    @Override
    public void update(Goods goods) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public List<Spu> findList(Map<String, Object> searchMap) {
        return null;
    }

    @Override
    public Page<Spu> findPage(int page, int size) {
        return null;
    }

    @Override
    public Page<Spu> findPage(Map<String, Object> searchMap, int page, int size) {
        return null;
    }

    @Override
    public Goods findGoodsById(String id) {
        return null;
    }

    @Override
    public void audit(String id) {

    }

    @Override
    public void pull(String id) {

    }

    @Override
    public void put(String id) {

    }

    @Override
    public void restore(String id) {

    }

    @Override
    public void realDel(String id) {

    }
}
