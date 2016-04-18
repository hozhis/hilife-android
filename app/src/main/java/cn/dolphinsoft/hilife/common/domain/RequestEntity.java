package cn.dolphinsoft.hilife.common.domain;

import java.io.Serializable;

/**
 * Created by hozhis on 2016/4/15.
 */
public class RequestEntity implements Serializable{

    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
