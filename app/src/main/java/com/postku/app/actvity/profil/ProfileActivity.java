package com.postku.app.actvity.profil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.actvity.SelectOutletActivity;
import com.postku.app.actvity.SplashActivity;
import com.postku.app.actvity.plus.PostkuPlusActivity;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.GetCheckAbsenResponse;
import com.postku.app.models.User;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private Context context;
    private SessionManager preferences;
    private User user;
    private CircleImageView circleImageView;
    private TextView textNama, textUsername, textPhone, textEmail, logout;
    private RelativeLayout rlpostku, rledit, rlrekening, rltoko;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        preferences = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        circleImageView = findViewById(R.id.cv_profile);
        textNama = findViewById(R.id.text_nama);
        textUsername = findViewById(R.id.text_username);
        textPhone = findViewById(R.id.text_phone);
        textEmail = findViewById(R.id.text_email);
        rlpostku = findViewById(R.id.rlupgrade);
        rledit = findViewById(R.id.rledit);
        rlrekening = findViewById(R.id.rlrekening);
        rltoko = findViewById(R.id.rltoko);
        logout = findViewById(R.id.logout);
        progressBar = findViewById(R.id.progressBar);

        Glide.with(context)
                .load(user.getProfilePic())
                .placeholder(R.drawable.image_placeholder)
                .into(circleImageView);
        textNama.setText(user.getNama());
        textUsername.setText(user.getUsername());
        textPhone.setText(user.getPhone());
        textEmail.setText(user.getEmail());

        rlpostku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostkuPlusActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        rledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        rlrekening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DataBankActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        rltoko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectOutletActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah yakin akan logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                              actLogout();
                            }
                        }).create().show();
            }
        });
    }

    private void actLogout(){
        progressBar.setVisibility(View.VISIBLE);
        UserService service = ServiceGenerator.createService(UserService.class, preferences.getToken(), null, null, null);
        service.logout(user.getUsername()).enqueue(new Callback<GetCheckAbsenResponse>() {
            @Override
            public void onResponse(Call<GetCheckAbsenResponse> call, Response<GetCheckAbsenResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        Realm realm = BaseApp.getInstance(context).getRealmInstance();
                        realm.beginTransaction();
                        realm.delete(User.class);
                        realm.commitTransaction();
//                                removeNotif();
                        BaseApp.getInstance(context).setLoginUser(null);
                        preferences.logout();
                        Intent intent = new Intent(context, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        System.exit(0);

                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCheckAbsenResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }
}