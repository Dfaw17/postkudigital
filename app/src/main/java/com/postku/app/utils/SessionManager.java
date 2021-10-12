package com.postku.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

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
}
