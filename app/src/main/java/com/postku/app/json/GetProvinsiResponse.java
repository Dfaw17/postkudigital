package com.postku.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postku.app.models.location.Provinsi;

import java.util.List;

public class GetProvinsiResponse {
    @Expose
    @SerializedName("provinsi")
    private List<Provinsi> provinsiList;

    public List<Provinsi> getProvinsiList() {
        return provinsiList;
    }

    public void setProvinsiList(List<Provinsi> provinsiList) {
        this.provinsiList = provinsiList;
    }
}
