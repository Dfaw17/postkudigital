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
    private static final String ACTIVE_CART = "activeCart";
    private static final String IS_CART_ACTIVE = "isCartActive";

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
}
