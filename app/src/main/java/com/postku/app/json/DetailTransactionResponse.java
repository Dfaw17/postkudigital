package com.postku.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postku.app.models.Cart;
import com.postku.app.models.ItemCart;
import com.postku.app.models.Toko;
import com.postku.app.models.Transaction;
import com.postku.app.models.User;

import java.util.List;

public class DetailTransactionResponse {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("data_transaksi")
    private Transaction transaction;

    @Expose
    @SerializedName("data_cart")
    private Cart cart;

    @Expose
    @SerializedName("data_cart_items")
    private List<ItemCart> itemCarts;

    @Expose
    @SerializedName("data_pegawai")
    private User user;

    @Expose
    @SerializedName("data_toko")
    private Toko toko;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<ItemCart> getItemCarts() {
        return itemCarts;
    }

    public void setItemCarts(List<ItemCart> itemCarts) {
        this.itemCarts = itemCarts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Toko getToko() {
        return toko;
    }

    public void setToko(Toko toko) {
        this.toko = toko;
    }
}
