package com.bean.search.demo;

import com.bean.search.bean.DbField;
import com.bean.search.bean.SearchBean;

/**
 * 用户信息表
 */

@SearchBean(tables = "psp_user")
public class User {

    @DbField("fid")
    private String fid;
    @DbField("name")
    private String name;
    @DbField("account")
    private String account;
    @DbField("password")
    private String password;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
