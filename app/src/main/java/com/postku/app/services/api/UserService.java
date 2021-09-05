package com.postku.app.services.api;

import com.postku.app.json.GetKategoriResponseJson;
import com.postku.app.json.GetMenuResponseJson;
import com.postku.app.json.GetOutletResponseJson;
import com.postku.app.json.HomeResponseJson;
import com.postku.app.json.LoginResponseJson;
import com.postku.app.json.RegisterResponseJson;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @Multipart
    @POST("token/register")
    Call<RegisterResponseJson> register (@Part("username") RequestBody username,
                                         @Part("email") RequestBody email,
                                         @Part("password") RequestBody password);

    @Multipart
    @POST("token/login")
    Call<LoginResponseJson> login (@Part("username") RequestBody username,
                                   @Part("password") RequestBody password);

    @GET("beranda/{id}")
    Call<HomeResponseJson> home(@Path("id") String id);

    @GET("toko")
    Call<GetOutletResponseJson> getToko(@Query("id_owner") String id);

    @GET("menu")
    Call<GetMenuResponseJson> getMenu(@Query("id_toko") String id);

    @GET("kategorimenu")
    Call<GetKategoriResponseJson> getKategori(@Query("id_toko") String id);
}
