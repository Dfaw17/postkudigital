package com.postku.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postku.app.models.Toko;
import com.postku.app.models.User;

import java.util.List;

public class CreateTokoResponse {
    @Expose
    @SerializedName("msg")
    private String msg;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("data")
    private Toko toko;

    @Expose
    @SerializedName("pegawai_toko")
    private List<User> userList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Toko getToko() {
        return toko;
    }

    public void setToko(Toko toko) {
        this.toko = toko;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
