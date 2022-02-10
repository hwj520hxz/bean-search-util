package com.bean.search.entity;

import com.bean.search.exception.SearchException;
import com.bean.search.service.*;
import com.bean.search.service.Impl.AbstractSearcher;
import com.bean.search.service.Impl.DefaultBeanSearcher;
import com.bean.search.service.Impl.DefaultMapSearcher;

import java.util.ArrayList;
import java.util.List;

/***
 * 检索器 Builder
 *
 * @author hwj
 *
 * */
public class SearcherBuilder {

    /**
     * 用于构建一个 BeanSearcher 实例
     * @return BeanSearcherBuilder
     */
    public static BeanSearcherBuilder beanSearcher() {
        return new BeanSearcherBuilder();
    }

    /**
     * 用于构建一个 MapSearcher 实例
     * @return MapSearcherBuilder
     */
    public static MapSearcherBuilder mapSearcher() {
        return new MapSearcherBuilder();
    }

    @SuppressWarnings("unchecked")
    static class DefaultSearcherBuilder<Builder extends DefaultSearcherBuilder<?>> {

        private ParamResolver paramResolver;

        private SqlResolver sqlResolver;

        private SqlExecutor sqlExecutor;

        private MetaResolver metaResolver;

        private final List<SqlInterceptor> interceptors = new ArrayList<>();

        public Builder paramResolver(ParamResolver paramResolver) {
            this.paramResolver = paramResolver;
            return (Builder) this;
        }

        public Builder sqlResolver(SqlResolver sqlResolver) {
            this.sqlResolver = sqlResolver;
            return (Builder) this;
        }

        public Builder sqlExecutor(SqlExecutor sqlExecutor) {
            this.sqlExecutor = sqlExecutor;
            return (Builder) this;
        }

        public Builder metaResolver(MetaResolver metaResolver) {
            this.metaResolver = metaResolver;
            return (Builder) this;
        }

        public Builder addInterceptor(SqlInterceptor interceptor) {
            if (interceptor != null) {
                interceptors.add(interceptor);
            }
            return (Builder) this;
        }

        protected void buildInternal(AbstractSearcher mainSearcher) {
            if (paramResolver != null) {
                mainSearcher.setParamResolver(paramResolver);
            }
            if (sqlResolver != null) {
                mainSearcher.setSqlResolver(sqlResolver);
            }
            if (sqlExecutor != null) {
                mainSearcher.setSqlExecutor(sqlExecutor);
            } else if (mainSearcher.getSqlExecutor() == null) {
                throw new SearchException("你必须配置一个 searchSqlExecutor，才能建立一个检索器！ ");
            }
            if (metaResolver != null) {
                mainSearcher.setMetaResolver(metaResolver);
            }
            mainSearcher.setInterceptors(interceptors);
        }

    }


    public static class BeanSearcherBuilder extends DefaultSearcherBuilder<BeanSearcherBuilder> {

        private BeanReflector beanReflector;

        public BeanSearcherBuilder beanReflector(BeanReflector beanReflector) {
            this.beanReflector = beanReflector;
            return this;
        }

        public BeanSearcher build() {
            DefaultBeanSearcher beanSearcher = new DefaultBeanSearcher();
            buildInternal(beanSearcher);
            if (beanReflector != null) {
                beanSearcher.setBeanReflector(beanReflector);
            }
            return beanSearcher;
        }

    }

    public static class MapSearcherBuilder extends DefaultSearcherBuilder<MapSearcherBuilder> {

        private final List<FieldConvertor.MFieldConvertor> convertors = new ArrayList<>();

        public MapSearcher build() {
            DefaultMapSearcher beanSearcher = new DefaultMapSearcher();
            buildInternal(beanSearcher);
            beanSearcher.setConvertors(convertors);
            return beanSearcher;
        }

        public MapSearcherBuilder addFieldConvertor(FieldConvertor.MFieldConvertor convertor) {
            if (convertor != null) {
                this.convertors.add(convertor);
            }
            return this;
        }

    }
}
