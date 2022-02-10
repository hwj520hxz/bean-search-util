package com.bean.search.service.Impl;

import com.bean.search.entity.*;
import com.bean.search.entity.param.FetchType;
import com.bean.search.exception.SearchException;
import com.bean.search.service.FieldConvertor;
import com.bean.search.service.MapSearcher;
import com.bean.search.service.SqlExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：
 */

@Service
public class DefaultMapSearcher extends AbstractSearcher implements MapSearcher {


    private List<FieldConvertor.MFieldConvertor> convertors = new ArrayList<>();

    public DefaultMapSearcher() {
    }

    public DefaultMapSearcher(SqlExecutor sqlExecutor) {
        super(sqlExecutor);
    }

    @Override
    public <T> SearchResult<Map<String, Object>> search(Class<T> beanClass, Map<String, Object> paraMap) {
        return search(beanClass, paraMap, new FetchType(FetchType.ALL));
    }

    @Override
    public <T> SearchResult<Map<String, Object>> search(Class<T> beanClass, Map<String, Object> paraMap, String[] summaryFields) {
        return search(beanClass, paraMap, new FetchType(FetchType.ALL, summaryFields));
    }

    @Override
    public <T> Map<String, Object> searchFirst(Class<T> beanClass, Map<String, Object> paraMap) {
        FetchType fetchType = new FetchType(FetchType.LIST_FIRST);
        List<Map<String, Object>> list = search(beanClass, paraMap, fetchType).getDataList();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public <T> List<Map<String, Object>> searchList(Class<T> beanClass, Map<String, Object> paraMap) {
        return search(beanClass, paraMap, new FetchType(FetchType.LIST_ONLY)).getDataList();
    }

    @Override
    public <T> List<Map<String, Object>> searchAll(Class<T> beanClass, Map<String, Object> paraMap) {
        return search(beanClass, paraMap, new FetchType(FetchType.LIST_ALL)).getDataList();
    }

    protected <T> SearchResult<Map<String, Object>> search(Class<T> beanClass, Map<String, Object> paraMap, FetchType fetchType) {
        try (SqlResult<T> sqlResult = doSearch(beanClass, paraMap, fetchType)) {
            ResultSet listResult = sqlResult.getListResult();
            ResultSet clusterResult = sqlResult.getAlreadyClusterResult();
            SearchResult<Map<String, Object>> result = new SearchResult<>();
            if (listResult != null) {
                SearchSql<T> searchSql = sqlResult.getSearchSql();
                BeanMeta<T> beanMeta = searchSql.getBeanMeta();
                List<String> fetchFields = searchSql.getFetchFields();
                while (listResult.next()) {
                    Map<String, Object> dataMap = new HashMap<>();
                    for (String field : fetchFields) {
                        FieldMeta meta = beanMeta.requireFieldMeta(field);
                        Object value = listResult.getObject(meta.getDbAlias());
                        dataMap.put(meta.getName(), convert(meta, value));
                    }
                    result.addData(dataMap);
                }
            }
            if (clusterResult != null) {
                result.setTotalCount(getCountFromSqlResult(sqlResult));
                result.setSummaries(getSummaryFromSqlResult(sqlResult));
            }
            return result;
        } catch (SQLException e) {
            throw new SearchException("A exception occurred when collecting sql result!", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Object convert(FieldMeta meta, Object value) {
        if (value != null && convertors.size() > 0) {
            Class<?> valueType = value.getClass();
            for (FieldConvertor convertor : convertors) {
                if (convertor.supports(meta, valueType, null)) {
                    return convertor.convert(meta, value, null);
                }
            }
        }
        return value;
    }


    public List<FieldConvertor.MFieldConvertor> getConvertors() {
        return convertors;
    }

    public void setConvertors(List<FieldConvertor.MFieldConvertor> convertors) {
        this.convertors = Objects.requireNonNull(convertors);
    }

    public void addConvertor(FieldConvertor.MFieldConvertor convertor) {
        if (convertor != null) {
            convertors.add(convertor);
        }
    }
}
