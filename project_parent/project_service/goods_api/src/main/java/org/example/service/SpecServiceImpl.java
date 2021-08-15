package org.example.service;

import org.example.dao.SpecDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SpecServiceImpl implements SpecService{
    @Autowired
    private SpecDao specDao;
    @Override
    public List<Map> findByCategoryName(String name) {
        List<Map> specList= specDao.findByCategoryName(name);
        for(Map spec:specList){
            String[] options = ((String) spec.get("options")).split(",");//规格选项列表
            spec.put("options",options);
        }
        return specList;
    }
}
