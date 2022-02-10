package com.bean.search.service.Impl;

import com.bean.search.entity.BeanMeta;
import com.bean.search.entity.SearchParam;
import com.bean.search.entity.SearchSql;
import com.bean.search.entity.SqlResult;
import com.bean.search.entity.param.FetchType;
import com.bean.search.exception.SearchException;
import com.bean.search.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：
 */
@Service
public abstract class AbstractSearcher implements Searcher {

    @Autowired
    private SqlExecutor sqlExecutor;

    private ParamResolver paramResolver = new DefaultParamResolver();

    private SqlResolver sqlResolver = new DefaultSqlResolver();

    private MetaResolver metaResolver = new DefaultMetaResolver();

    private List<SqlInterceptor> interceptors = new ArrayList<>();

    public AbstractSearcher() {
    }

    public AbstractSearcher(SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public <T> Number searchCount(Class<T> beanClass, Map<String, Object> paraMap) {
        try (SqlResult<T> result = doSearch(beanClass, paraMap, new FetchType(FetchType.ONLY_TOTAL))) {
            return getCountFromSqlResult(result);
        } catch (SQLException e) {
            throw new SearchException("A exception occurred when collect sql result!", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> Number searchSum(Class<T> beanClass, Map<String, Object> paraMap, String field) {
        String[] fields = { Objects.requireNonNull(field) };
        Number[] results = searchSum(beanClass, paraMap, fields);
        return results != null && results.length > 0 ? results[0] : null;
    }

    @Override
    public <T> Number[] searchSum(Class<T> beanClass, Map<String, Object> paraMap, String[] fields) {
        if (fields == null || fields.length == 0) {
            throw new SearchException("检索该 Bean【" + beanClass.getName()
                    + "】的统计信息时，必须要指定需要统计的属性！");
        }
        try (SqlResult<T> result = doSearch(beanClass, paraMap, new FetchType(FetchType.ONLY_SUMMARY, fields))) {
            return getSummaryFromSqlResult(result);
        } catch (SQLException e) {
            throw new SearchException("A exception occurred when collect sql result!", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Number getCountFromSqlResult(SqlResult<?> sqlResult) throws SQLException {
        return (Number) sqlResult.getAlreadyClusterResult().getObject(sqlResult.getSearchSql().getCountAlias());
    }

    protected Number[] getSummaryFromSqlResult(SqlResult<?> sqlResult) throws SQLException {
        List<String> summaryAliases = sqlResult.getSearchSql().getSummaryAliases();
        ResultSet countResultSet = sqlResult.getAlreadyClusterResult();
        Number[] summaries = new Number[summaryAliases.size()];
        for (int i = 0; i < summaries.length; i++) {
            String summaryAlias = summaryAliases.get(i);
            summaries[i] = (Number) countResultSet.getObject(summaryAlias);
        }
        return summaries;
    }

    /**
     * @param beanClass 所要查询的实体类
     * @param paraMap 参数
     * @param fetchType 查询方式
     **/
    protected <T> SqlResult<T> doSearch(Class<T> beanClass, Map<String, Object> paraMap, FetchType fetchType) {
        if (sqlExecutor == null) {
            throw new SearchException("you must set a sqlExecutor before search.");
        }
        BeanMeta<T> beanMeta = metaResolver.resolve(beanClass);
        SearchParam searchParam = paramResolver.resolve(beanMeta, fetchType, paraMap);
        SearchSql<T> searchSql = sqlResolver.resolve(beanMeta, searchParam);
        return sqlExecutor.execute(intercept(searchSql, paraMap));
    }

    protected <T> SearchSql<T> intercept(SearchSql<T> searchSql, Map<String, Object> paraMap) {
        for (SqlInterceptor interceptor : interceptors) {
            searchSql = interceptor.intercept(searchSql, paraMap);
        }
        return searchSql;
    }

    public ParamResolver getParamResolver() {
        return paramResolver;
    }

    public void setParamResolver(ParamResolver paramResolver) {
        this.paramResolver = Objects.requireNonNull(paramResolver);
    }

    public SqlResolver getSqlResolver() {
        return sqlResolver;
    }

    public void setSqlResolver(SqlResolver sqlResolver) {
        this.sqlResolver = Objects.requireNonNull(sqlResolver);
    }

    public SqlExecutor getSqlExecutor() {
        return sqlExecutor;
    }

    public void setSqlExecutor(SqlExecutor sqlExecutor) {
        this.sqlExecutor = Objects.requireNonNull(sqlExecutor);
    }

    public MetaResolver getMetaResolver() {
        return metaResolver;
    }

    public void setMetaResolver(MetaResolver metaResolver) {
        this.metaResolver = Objects.requireNonNull(metaResolver);
    }

    public List<SqlInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<SqlInterceptor> interceptors) {
        this.interceptors = Objects.requireNonNull(interceptors);
    }
}
