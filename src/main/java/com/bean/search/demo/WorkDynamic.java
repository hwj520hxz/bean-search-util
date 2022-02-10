package com.bean.search.demo;

import com.bean.search.bean.DataSourceSearch;
import com.bean.search.bean.DbField;
import com.bean.search.bean.SearchBean;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：
 */

@DataSourceSearch()
@SearchBean(tables = "psp_user a, PSP_WORK_DYNAMIC b", joinCond = "a.fid = b.create_by")
public class WorkDynamic {

    @DbField("b.fid")
    private String fid;
    @DbField("b.title")
    private String title;
    @DbField("b.plain_text")
    private String plainText;
    @DbField("a.name")
    private String name;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
