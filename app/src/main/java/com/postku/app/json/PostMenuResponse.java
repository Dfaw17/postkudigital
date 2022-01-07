package com.postku.app.json;

import android.view.Menu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postku.app.models.MenuModel;
import com.postku.app.models.Menus;

public class PostMenuResponse {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status")
    private int status;

    @Expose
    @SerializedName("data")
    private MenuModel menus;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MenuModel getMenus() {
        return menus;
    }

    public void setMenus(MenuModel menus) {
        this.menus = menus;
    }
}
