package com.postku.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wallet implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("wallet_code")
    private String walletCode;

    @Expose
    @SerializedName("balance")
    private long balance;

    @Expose
    @SerializedName("balance_req")
    private long balanceReq;

    @Expose
    @SerializedName("status_req_deposit")
    private int statusReqDepo;

    public long getBalanceReq() {
        return balanceReq;
    }

    public void setBalanceReq(long balanceReq) {
        this.balanceReq = balanceReq;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWalletCode() {
        return walletCode;
    }

    public void setWalletCode(String walletCode) {
        this.walletCode = walletCode;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public int getStatusReqDepo() {
        return statusReqDepo;
    }

    public void setStatusReqDepo(int statusReqDepo) {
        this.statusReqDepo = statusReqDepo;
    }
}
