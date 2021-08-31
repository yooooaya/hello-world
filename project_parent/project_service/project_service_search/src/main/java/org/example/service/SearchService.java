package org.example.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SearchService {
    /**
     * 全文检索
     * @param paramMap  查询参数
     * @return
     */
    public Map search(Map<String, String> paramMap) throws Exception;
    public Map<String, Set<String>> formartSpec(List<String> specList);
}
