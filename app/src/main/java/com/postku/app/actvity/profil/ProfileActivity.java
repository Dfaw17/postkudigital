package com.postku.app.actvity.profil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.actvity.SelectOutletActivity;
import com.postku.app.actvity.plus.PostkuPlusActivity;
import com.postku.app.models.User;
import com.postku.app.utils.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private Context context;
    private SessionManager preferences;
    private User user;
    private CircleImageView circleImageView;
    private TextView textNama, textUsername, textPhone, textEmail;
    private RelativeLayout rlpostku, rledit, rlrekening, rltoko;
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
    }
}