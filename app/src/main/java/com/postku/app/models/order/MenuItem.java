package com.postku.app.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.annotations.PrimaryKey;

public class MenuItem implements Serializable {
    @PrimaryKey
    @Expose
    @SerializedName("id_cart_item")
    private String idMenu;

    @Expose
    @SerializedName("qty")
    private String qty;

    @Expose
    @SerializedName("disc")
    private String disc;

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }
}
