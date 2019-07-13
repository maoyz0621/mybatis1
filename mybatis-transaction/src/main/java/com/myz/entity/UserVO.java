/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author maoyz on 2018/6/21
 * @version: v1.0
 */
@Data
public class UserVO implements Serializable {

    private Long id;

    private String username;

    private String password;

    private Character sex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserVO{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", sex=").append(sex);
        sb.append('}');
        return sb.toString();
    }
}
