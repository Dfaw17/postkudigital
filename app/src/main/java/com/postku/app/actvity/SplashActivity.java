package com.postku.app.actvity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.helpers.DHelper;
import com.postku.app.models.GetContactUsResponse;
import com.postku.app.models.User;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.NetworkUtils;
import com.postku.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                        if(user != null){
                            if(sessionManager.getIdToko().equalsIgnoreCase("")){

                                Intent intent = new Intent(context, SelectOutletActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }else {
                                getKontak();
                            }
                        }else {
                            Intent intent = new Intent(context, BeginActivity.class);
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

    private void getKontak(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.contact().enqueue(new Callback<GetContactUsResponse>() {
            @Override
            public void onResponse(Call<GetContactUsResponse> call, Response<GetContactUsResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getKontakList().size() > 0){
                            sessionManager.saveContact(response.body().getKontakList(), "contactus");
                            Intent intent = new Intent(context, SelectOutletActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetContactUsResponse> call, Throwable t) {

            }
        });
    }

}