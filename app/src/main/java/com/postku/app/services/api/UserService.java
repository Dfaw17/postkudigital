package com.postku.app.services.api;

import androidx.transition.Slide;

import com.postku.app.json.ActiveStockResponse;
import com.postku.app.json.CallbackQrisResponse;
import com.postku.app.json.CreateCartResponse;
import com.postku.app.json.CreateQrisResponse;
import com.postku.app.json.CreateTokoResponse;
import com.postku.app.json.CustomerPostResponseJson;
import com.postku.app.json.DetailCartResponse;
import com.postku.app.json.DetailMenuResponse;
import com.postku.app.json.DetailTransactionResponse;
import com.postku.app.json.GetCartResponse;
import com.postku.app.json.GetCustomerResponseJson;
import com.postku.app.json.GetDetailArtikelResponse;
import com.postku.app.json.GetDetailBannerResponse;
import com.postku.app.json.GetHistoryTransResponse;
import com.postku.app.json.GetKategoriResponseJson;
import com.postku.app.json.GetMenuResponseJson;
import com.postku.app.json.GetOutletResponseJson;
import com.postku.app.json.GetPromoResponseJson;
import com.postku.app.json.GetReportResponseJson;
import com.postku.app.json.GetServiceResponseJson;
import com.postku.app.json.GetTableResponse;
import com.postku.app.json.GetTaxResponseJson;
import com.postku.app.json.HomeResponseJson;
import com.postku.app.json.InsertItemResponse;
import com.postku.app.json.KategoriPostResponse;
import com.postku.app.json.LoginResponseJson;
import com.postku.app.json.PostMenuResponse;
import com.postku.app.json.PromoPostResponseJson;
import com.postku.app.json.RegisterResponseJson;
import com.postku.app.json.ServicePostResponseJson;
import com.postku.app.json.StockResponseJson;
import com.postku.app.json.StockTrxResponse;
import com.postku.app.json.TablePostResponse;
import com.postku.app.json.TaxPostResponseJson;
import com.postku.app.json.TransactionResponse;

import java.util.Map;

import javax.annotation.Nullable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
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
//    auth
    @FormUrlEncoded
    @POST("token/register")
    Call<RegisterResponseJson> createUser(@Field("username") String username,
                                          @Field("email") String email,
                                          @Field("password") String password);
    @Multipart
    @PUT("updateowner")
    Call<ResponseBody> updateOwner (@Part MultipartBody.Part image,
                                    @PartMap Map<String, RequestBody> text);
//    toko
    @Multipart
    @POST("toko")
    Call<CreateTokoResponse> createToko (@Part MultipartBody.Part image,
                                         @PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("toko")
    Call<CreateTokoResponse> updateToko (@Part MultipartBody.Part image,
                                         @PartMap Map<String, RequestBody> text);

    @GET("toko/detail/{id}")
    Call<CreateTokoResponse> detailToko(@Path("id") String id);

    @Multipart
    @PUT("toko")
    Call<InsertItemResponse> deleteToko(@PartMap Map<String, RequestBody> text);

    @Multipart
    @POST("token/login")
    Call<LoginResponseJson> login (@Part("username") RequestBody username,
                                   @Part("password") RequestBody password);
//    beranda
    @GET("beranda/{id}")
    Call<HomeResponseJson> home(@Path("id") String id);

    @GET("toko")
    Call<GetOutletResponseJson> getToko(@Query("id_owner") String id);

//  menu
    @GET("menu")
    Call<GetMenuResponseJson> getMenu(@Query("id_toko") String id);

    @GET("menu")
    Call<GetMenuResponseJson> getMenuByKategori(@Query("id_toko") String id,
                                                @Nullable @Query("id_kategori") String idcat);

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

    @Multipart
    @POST("stock")
    Call<ActiveStockResponse> activeStock(@PartMap Map<String, RequestBody> text);

    @GET("stock")
    Call<StockResponseJson> stockList(@Query("id_toko") String id);

    @GET("stock/trx")
    Call<StockTrxResponse> historyStock(@Query("id_menu") String id);

//    cart
    @GET("cart")
    Call<GetCartResponse> getCart(@Query("id_toko") String id);

    @GET("cart/detail/{id}")
    Call<DetailCartResponse> detailCart(@Path("id") String id);

    @Multipart
    @POST("cart")
    Call<CreateCartResponse> createCart(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("cart")
    Call<CreateCartResponse> simpanCart(@PartMap Map<String, RequestBody> text);

    @DELETE("cart")
    Call<InsertItemResponse> deleteCart(@Query("id_cart") String id);

    @Multipart
    @POST("cartitem")
    Call<InsertItemResponse> addItem(@PartMap Map<String, RequestBody> text);

    @DELETE("cartitem")
    Call<InsertItemResponse> deleteItem(@Query("id_cart_item") String id);

    @Multipart
    @PATCH("cartitem")
    Call<InsertItemResponse> updateItem(@PartMap Map<String, RequestBody> text);

    @Multipart
    @POST("transaction")
    Call<TransactionResponse> createTransaction(@PartMap Map<String, RequestBody> text);

    @GET("transaction")
    Call<GetHistoryTransResponse> historyTrans(@Query("id_toko") String id,
                                               @Query("date1") String date1,
                                               @Query("date2") String date2);

    @GET("transaction/detail/{code}")
    Call<DetailTransactionResponse> detail(@Path("code") String code);

    @GET("laporanbisnis")
    Call<GetReportResponseJson> laporan(@Query("id_toko") String id,
                                        @Query("date1") String date1,
                                        @Query("date2") String date2);

    @Multipart
    @POST("qris")
    Call<CreateQrisResponse> payQithQris(@PartMap Map<String, RequestBody> text);

    @GET("qris/callback")
    Call<CallbackQrisResponse> callbackQris(@Query("cart_code") String id,
                                            @Query("amount") String date1);

    @GET("qris/check/{invoice}")
    Call<CreateQrisResponse> checkQris(@Path("invoice") String invoice);

    @GET("banner/{id}")
    Call<GetDetailBannerResponse> detailbanner(@Path("id") String id);

    @GET("articles/{id}")
    Call<GetDetailArtikelResponse> detailArtikel(@Path("id") String id);

    @GET("table")
    Call<GetTableResponse> getTable(@Query("id_toko") String id);

    @Multipart
    @POST("table")
    Call<TablePostResponse> addTable(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("table")
    Call<TablePostResponse> updTable(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("table")
    Call<TablePostResponse> delTable(@PartMap Map<String, RequestBody> text);

    @GET("discount")
    Call<GetPromoResponseJson> getPromo(@Query("id_toko") String id);

    @Multipart
    @POST("discount")
    Call<PromoPostResponseJson> addPromo(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("discount")
    Call<PromoPostResponseJson> updPromo(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("discount")
    Call<PromoPostResponseJson> delPromo(@PartMap Map<String, RequestBody> text);

    @GET("pajak")
    Call<GetTaxResponseJson> getPajak(@Query("id_toko") String id);

    @Multipart
    @POST("pajak")
    Call<TaxPostResponseJson> addPajak(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("pajak")
    Call<TaxPostResponseJson> updPajak(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("pajak")
    Call<TaxPostResponseJson> delPajak(@PartMap Map<String, RequestBody> text);

    @GET("servicefee")
    Call<GetServiceResponseJson> getServiceFee(@Query("id_toko") String id);

    @Multipart
    @POST("servicefee")
    Call<ServicePostResponseJson> addServiceFee(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("servicefee")
    Call<ServicePostResponseJson> updServiceFee(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("servicefee")
    Call<ServicePostResponseJson> delServiceFee(@PartMap Map<String, RequestBody> text);

    @GET("pelanggan")
    Call<GetCustomerResponseJson> getCustomer(@Query("id_toko") String id);

    @Multipart
    @POST("pelanggan")
    Call<CustomerPostResponseJson> addCustomer(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("pelanggan")
    Call<CustomerPostResponseJson> updCustomer(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("pelanggan")
    Call<CustomerPostResponseJson> delCustomer(@PartMap Map<String, RequestBody> text);
}
