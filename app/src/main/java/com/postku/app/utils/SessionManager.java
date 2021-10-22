package com.postku.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SessionManager {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Context context;
    private static SessionManager mInstance;
    private static final String IS_LOGIN = "isLogin";
    private static final String TOKEN = "token";
    private static final String ID_TOKO = "idToko";
    private static final String NAMA_TOKO = "namaToko";
    private static final String ALAMAT_TOKO = "alamatToko";
    private static final String LOGO_TOKO = "logoToko";
    private static final String KATEGORI_TOKO = "kategoriToko";
    private static final String ACTIVE_CART = "activeCart";
    private static final String IS_CART_ACTIVE = "isCartActive";
    private static final String ID_WALLET = "idWallet";

    private static final String DISCOUNT = "discount";
    private static final String PELANGGAN = "pelanggan";
    private static final String MEJA = "meja";
    private static final String TIPE_ORDER = "tipeorder";
    private static final String LABEL_ORDER = "labelorder";
    private static final String PAJAK = "pajak";
    private static final String SERVICE_FEE = "servicefee";

    private static final String ID_DISCOUNT = "iddiscount";
    private static final String ID_PELANGGAN = "idpelanggan";
    private static final String ID_MEJA = "idmeja";
    private static final String ID_TIPE_ORDER = "idtipeorder";
    private static final String ID_LABEL_ORDER = "idlabelorder";
    private static final String ID_PAJAK = "idpajak";


    public SessionManager(Context context){
        this.context = context;
        sharedPref = context.getSharedPreferences("postkuSharedPref",
                Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }
    public static synchronized SessionManager getInstance(Context context){
        if(mInstance==null)
            mInstance = new SessionManager(context);
        return mInstance;
    }

    public void createSessionLogin(String token){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(TOKEN, token);
        editor.commit();
    }
    public void logout(){
        editor.remove("token");
        editor.remove("idToko");
        editor.remove("namaToko");
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();
    }

    public void createCart(String id){
        editor.putBoolean(IS_CART_ACTIVE, true);
        editor.putString(ACTIVE_CART, id);
        editor.commit();
    }

    public void deleteCart(){
        editor.putBoolean(IS_CART_ACTIVE,false);
        editor.remove("activeCart");
        editor.remove(DISCOUNT);
        editor.remove(ID_DISCOUNT);
        editor.remove(PELANGGAN);
        editor.remove(ID_PELANGGAN);
        editor.remove(TIPE_ORDER);
        editor.remove(ID_TIPE_ORDER);
        editor.remove(LABEL_ORDER);
        editor.remove(ID_LABEL_ORDER);
        editor.remove(PAJAK);
        editor.remove(ID_PAJAK);
        editor.remove(MEJA);
        editor.remove(ID_MEJA);
        editor.remove(SERVICE_FEE);

        editor.commit();
    }

    public boolean isCartActive(){
        return sharedPref.getBoolean(IS_CART_ACTIVE, false);
    }

    public String getActiveCart(){
        return sharedPref.getString(ACTIVE_CART, "");
    }

    public void setActiveCart(String s){
        editor.putString(ACTIVE_CART, s);
        editor.commit();
    }

    public boolean isLogin(){
        return sharedPref.getBoolean(IS_LOGIN,false);
    }
    public String getToken(){
        return sharedPref.getString(TOKEN,"");
    }
    public void setToken(String s){
        editor.putString(TOKEN, s);
        editor.commit();
    }

    public String getIdToko(){
        return sharedPref.getString(ID_TOKO, "");
    }

    public void setIdToko(String s){
        editor.putString(ID_TOKO, s);
        editor.commit();
    }

    public String getNamaToko(){
        return sharedPref.getString(NAMA_TOKO, "");
    }

    public void setNamaToko(String s){
        editor.putString(NAMA_TOKO, s);
        editor.commit();
    }

    public void setAlamatToko(String s){
        editor.putString(ALAMAT_TOKO, s);
        editor.commit();
    }

    public String getAlamatToko(){
        return sharedPref.getString(ALAMAT_TOKO, "");
    }

    public void setKategoriToko(String s){
        editor.putString(KATEGORI_TOKO, s);
        editor.commit();
    }

    public String getKategoriToko(){
        return sharedPref.getString(KATEGORI_TOKO, "");
    }

    public void setLogoToko(String s){
        editor.putString(LOGO_TOKO, s);
        editor.commit();
    }

    public String getLogoToko(){
        return sharedPref.getString(LOGO_TOKO, "");
    }

    public void setIdWallet(String s){
        editor.putString(ID_WALLET, s);
        editor.commit();
    }

    public String getIdWallet(){
        return sharedPref.getString(ID_WALLET, "");
    }

    public void saveServiceFee(List<Integer> list, String s){
        Gson gson = new Gson();
        String data = gson.toJson(list);
        editor.putString(SERVICE_FEE, data);
        editor.apply();
    }

    public List<Integer> getSeviceList(){
        Gson gson = new Gson();
        String json = sharedPref.getString(SERVICE_FEE, null);
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void setDiscount(String s){
        editor.putString(DISCOUNT, s);
        editor.commit();
    }

    public String getDiscount(){
        return sharedPref.getString(DISCOUNT, "");
    }

    public void setPelanggan(String s){
        editor.putString(PELANGGAN, s);
        editor.commit();
    }

    public String getPelanggan(){
        return sharedPref.getString(PELANGGAN, "");
    }

    public void setMeja(String s){
        editor.putString(MEJA, s);
        editor.commit();
    }

    public String getMeja(){
        return sharedPref.getString(MEJA, "");
    }

    public void setTipeOrder(String s){
        editor.putString(TIPE_ORDER, s);
        editor.commit();
    }

    public String getTipeOrder(){
        return sharedPref.getString(TIPE_ORDER, "");
    }

    public void setLabelOrder(String s){
        editor.putString(LABEL_ORDER, s);
        editor.commit();
    }

    public String getLabelOrder(){
        return sharedPref.getString(LABEL_ORDER, "");
    }

    public void setPajak(String s){
        editor.putString(PAJAK, s);
        editor.commit();
    }

    public String getPajak(){
        return sharedPref.getString(PAJAK, "");
    }

    public void setIdDiscount(String s){
        editor.putString(ID_DISCOUNT, s);
        editor.commit();
    }

    public String getIdDiscount(){
        return sharedPref.getString(ID_DISCOUNT, "");
    }

    public void setIdPelanggan(String s){
        editor.putString(ID_PELANGGAN, s);
        editor.commit();
    }

    public String getIdPelanggan(){
        return sharedPref.getString(ID_PELANGGAN, "");
    }

    public void setIdMeja(String s){
        editor.putString(ID_MEJA, s);
        editor.commit();
    }

    public String getIdMeja(){
        return sharedPref.getString(ID_MEJA, "");
    }

    public void setIdTipeOrder(String s){
        editor.putString(ID_TIPE_ORDER, s);
        editor.commit();
    }

    public String getIdTipeOrder(){
        return sharedPref.getString(ID_TIPE_ORDER, "");
    }

    public void setIdLabelOrder(String s){
        editor.putString(ID_LABEL_ORDER, s);
        editor.commit();
    }

    public String getIdLabelOrder(){
        return sharedPref.getString(ID_LABEL_ORDER, "");
    }

    public void setIdPajak(String s){
        editor.putString(ID_PAJAK, s);
        editor.commit();
    }

    public String getIdPajak(){
        return sharedPref.getString(ID_PAJAK, "");
    }
}
