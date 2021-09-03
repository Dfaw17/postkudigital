package com.postku.app.actvity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.helpers.DHelper;
import com.postku.app.models.User;
import com.postku.app.utils.NetworkUtils;
import com.postku.app.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {
    private Context context;
    private User user;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        user = BaseApp.getInstance(context).getLoginUser();
        sessionManager = new SessionManager(context);
        if(NetworkUtils.isConnected(context)){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(sessionManager.isLogin()){
                        if(sessionManager.getIdToko().equalsIgnoreCase("")){
                            Intent intent = new Intent(context, SelectOutletActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(context, SelectOutletActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Intent intent = new Intent(context, BeginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 2000L);
        }else {
            DHelper.pesan(context, context.getString(R.string.error_connection));
        }

    }
}