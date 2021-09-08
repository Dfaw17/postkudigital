package com.postku.app.services.api;

import com.postku.app.json.GetDesaResponse;
import com.postku.app.json.GetKategoriResponseJson;
import com.postku.app.json.GetKecamatanResponse;
import com.postku.app.json.GetKotaResponse;
import com.postku.app.json.GetProvinsiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ReferenceService {
    @GET("provinsi")
    Call<GetProvinsiResponse> provinsi();

    @GET("kota")
    Call<GetKotaResponse> kota(@Query("id_provinsi") String id);

    @GET("kecamatan")
    Call<GetKecamatanResponse> kecamatan(@Query("id_kota") String id);

    @GET("kelurahan")
    Call<GetDesaResponse> desa(@Query("id_kecamatan") String id);
}
