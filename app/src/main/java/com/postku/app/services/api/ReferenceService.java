package com.postku.app.services.api;

import com.postku.app.json.GetDesaResponse;
import com.postku.app.json.GetKategoriResponseJson;
import com.postku.app.json.GetKecamatanResponse;
import com.postku.app.json.GetKotaResponse;
import com.postku.app.json.GetProvinsiResponse;
import com.postku.app.models.location.Kecamatan;
import com.postku.app.models.location.Kelurahan;
import com.postku.app.models.location.Kota;
import com.postku.app.models.location.Provinsi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReferenceService {
    @GET("provinces.json")
    Call<List<Provinsi>> provinsi();

    @GET("regencies/{id}.json")
    Call<List<Kota>> kota(@Path("id") String id);

    @GET("districts/{id}.json")
    Call<List<Kecamatan>> kecamatan(@Path("id") String id);

    @GET("villages/{id}.json")
    Call<List<Kelurahan>> desa(@Path("id") String id);
}
