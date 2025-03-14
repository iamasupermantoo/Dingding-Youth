package com.youshi.zebra.admin.adminuser.model.impl;

import com.youshi.zebra.admin.adminuser.model.AdminUserPassport;

/**
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public class AdminUserPassportModel implements AdminUserPassport {

    private final int id;

    private final String username;

    private final String signature;

    private final String password;

    private final int status;

    public AdminUserPassportModel(int id, String username, String password, int status,
            String signature) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.signature = signature;
    }

    /**
     * @return the signature
     */
    @Override
    public String getSignature() {
        return signature;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "AdminUserPassportImpl [id=" + id + ", username=" + username + ", signature="
                + signature + ", password=" + password + ", status=" + status + "]";
    }

}
