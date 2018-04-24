package com.example.a40782.mqtt;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by XingruiLi on 2018/4/23.
 */

public class MyApp extends Application {
    int apiVersion;
    String url = "";
    String username;
    public static SharedPreferences sp;
    public void setApiVersion(int version) {
        apiVersion = version;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public int getApiVersion() {
        return apiVersion;
    }

    public String getUsername(){
        return username;
    }
    public String getUrl() {
        return url;
    }
}
