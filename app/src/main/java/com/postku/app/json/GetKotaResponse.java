package com.postku.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postku.app.models.location.Kota;


import java.util.List;

public class GetKotaResponse {
    @Expose
    @SerializedName("kota_kabupaten")
    private List<Kota> kotaList;

    public List<Kota> getKotaList() {
        return kotaList;
    }

    public void setKotaList(List<Kota> kotaList) {
        this.kotaList = kotaList;
    }
}
