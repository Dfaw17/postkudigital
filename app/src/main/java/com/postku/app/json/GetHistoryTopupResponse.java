package com.postku.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postku.app.models.HistoryTopup;
import com.postku.app.models.HistoryWallet;

import java.util.List;

public class GetHistoryTopupResponse {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("data")
    private List<HistoryTopup> dataList;

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

    public List<HistoryTopup> getDataList() {
        return dataList;
    }

    public void setDataList(List<HistoryTopup> dataList) {
        this.dataList = dataList;
    }
}
