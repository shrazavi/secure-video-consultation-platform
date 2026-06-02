package com.shrazavi.dadmehr.core.model;

/**
 * Created by dds on 2020/4/11.
 * 用户信息
 */
public class UserInfo {

    private String userid;
    private String socketId;

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getUserId() {
        return userid;
    }

    public void setUserId(String userid) {
        this.userid = userid;
    }
}
