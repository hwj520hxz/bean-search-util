package com.bean.search.web;

import com.bean.search.demo.User;
import com.bean.search.demo.WorkDynamic;
import com.bean.search.entity.SearchResult;
import com.bean.search.service.Searcher;
import com.bean.search.util.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：
 */

@RestController
@RequestMapping("demo")
public class demoController {

    @Autowired
    private Searcher searcher;

    @GetMapping("/index")
    public SearchResult<Map<String, Object>> index() {
        Map<String, Object> map = new HashMap<>();
        return (SearchResult<Map<String, Object>>) searcher.search(User.class, map);
    }

    /**
     * 带参数的全量查询
     **/
    @GetMapping("/index2")
    public SearchResult<Map<String, Object>> index2() {
        Map<String, Object> map = new HashMap<>();
        map.put("name","system");
        return (SearchResult<Map<String, Object>>) searcher.search(WorkDynamic.class, map);
    }

    /**
     * 动态表查询
     **/
    @GetMapping("/index3")
    public SearchResult<Map<String, Object>> index2(String table) {
        Map<String, Object> map = new HashMap<>();
        map.put("table","PSP_WORK_DYNAMIC");
        return (SearchResult<Map<String, Object>>) searcher.search(WorkDynamic.class, map);
    }
}
