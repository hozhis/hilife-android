package cn.dolphinsoft.hilife.auth.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import cn.dolphinsoft.hilife.common.domain.RequestEntity;

/**
 * Created by hozhis on 2016/4/15.
 */
@Table(name = "USER_INFO")
public class LoginEntity extends RequestEntity{

    @Id(column = "loginId")
    private String loginId;

    private String token;

    private String username;

    private String custImage;

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCustImage() {
        return custImage;
    }

    public void setCustImage(String custImage) {
        this.custImage = custImage;
    }
}
