package com.shrazavi.dadmehr.core.consts;

import com.shrazavi.dadmehr.G;

/**
 * Created by dds on 2020/4/19.
 * ddssingsong@163.com
 */
public class Urls {

    //    private final static String IP = "192.168.2.111";
//    public final static String IP = "42.192.40.58:5000";
    public final static String IP = G.nodeurl;

    private final static String HOST = "http://" + IP + "/";
    public final static String nodeurl = G.nodeurl;
    // Signaling address
//    public final static String WS = "ws://" + IP + "/ws";
    public final static String WS = "ws://" + IP  ;
    // Get user list
//    public static String getUserList() {
//        return HOST + "userList";
//    }

    // Get a list of rooms
//    public static String getRoomList() {
//        return HOST + "roomList";
//    }
}
