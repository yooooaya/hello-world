package org.example.service;

import org.example.pojo.Spec;

import java.util.List;
import java.util.Map;

public interface SpecService {
    public List<Map> findByCategoryName(String name);
}
