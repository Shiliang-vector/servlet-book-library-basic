package com.test.pojo;

/**
 * 登录用户基础信息，用于 Session 储存与身份判断。
 * type 字段在登录后被赋值为 admin/reader，方便过滤器与页面区分权限。
 */
public class User {
    private Integer id;
    private String username;// 用户名
    private String nickname;
    private String password;// 密码
    private String type; // 用户类型（admin/reader），登录后补充

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
