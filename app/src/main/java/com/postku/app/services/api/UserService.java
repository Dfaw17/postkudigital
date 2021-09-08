package com.postku.app.services.api;

import com.postku.app.json.CreateTokoResponse;
import com.postku.app.json.DetailMenuResponse;
import com.postku.app.json.GetKategoriResponseJson;
import com.postku.app.json.GetMenuResponseJson;
import com.postku.app.json.GetOutletResponseJson;
import com.postku.app.json.HomeResponseJson;
import com.postku.app.json.KategoriPostResponse;
import com.postku.app.json.LoginResponseJson;
import com.postku.app.json.PostMenuResponse;
import com.postku.app.json.RegisterResponseJson;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @FormUrlEncoded
    @POST("token/register")
    Call<RegisterResponseJson> createUser(@Field("username") String username,
                                          @Field("email") String email,
                                          @Field("password") String password);

    @Multipart
    @PUT("updateowner")
    Call<ResponseBody> updateOwner (@Part MultipartBody.Part image,
                                    @PartMap Map<String, RequestBody> text);

    @Multipart
    @POST("toko")
    Call<CreateTokoResponse> createToko (@Part MultipartBody.Part image,
                                         @PartMap Map<String, RequestBody> text);

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

    @Multipart
    @POST("menu")
    Call<PostMenuResponse> addMenu(@Part MultipartBody.Part image,
                                       @PartMap Map<String, RequestBody> text);
    @Multipart
    @PATCH("menu")
    Call<PostMenuResponse> editMenu(@Part MultipartBody.Part image,
                                    @PartMap Map<String, RequestBody> text);

    @GET("menu/detail/{id}")
    Call<DetailMenuResponse> detailMenu(@Path("id") String id);

    @Multipart
    @PUT("menu")
    Call<ResponseBody> deleteMenu(@PartMap Map<String, RequestBody> text);

    @GET("kategorimenu")
    Call<GetKategoriResponseJson> getKategori(@Query("id_toko") String id);

    @Multipart
    @POST("kategorimenu")
    Call<KategoriPostResponse> submitKategori (@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("kategorimenu")
    Call<ResponseBody> updateKategori (@PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("kategorimenu")
    Call<ResponseBody> deleteKategori (@PartMap Map<String, RequestBody> text);
}
