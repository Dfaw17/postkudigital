package com.postku.app.actvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.fragment.HomeFragment;
import com.postku.app.fragment.riwayat.HistoryFragment;
import com.postku.app.helpers.DHelper;
import com.postku.app.models.User;
import com.postku.app.utils.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView textNama, textJabatan, textNamaToko;
    private CircleImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        user = BaseApp.getInstance(context).getLoginUser();
        sessionManager = new SessionManager(context);
        drawerLayout = findViewById(R.id.action_main);
        navigationView = findViewById(R.id.nav_view);
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, navigationView, false);
        navigationView.addHeaderView(headerView);


        toolbar = findViewById(R.id.toolbar);
        textNama = headerView.findViewById(R.id.text_nama);
        textJabatan = headerView.findViewById(R.id.text_jabatan);
        textNamaToko = headerView.findViewById(R.id.text_toko);
        avatar = headerView.findViewById(R.id.img_profile);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Glide.with(context)
                .load(user.getProfilePic())
                .placeholder(R.drawable.logo_app)
                .into(avatar);
        textNama.setText(user.getNama());
        if(user.isOwner()){
            textJabatan.setText("Owner");
        }else {
            textJabatan.setText("Kasir");
        }
        textNamaToko.setText(sessionManager.getNamaToko());



        loadFragment(new HomeFragment());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = null;
                toolbar.setTitle(item.getTitle());
                int itemId = item.getItemId();
                switch (itemId){
                    case R.id.home:
                        loadFragment(new HomeFragment());
                        break;
                    case R.id.riwayat:
                        loadFragment(new HistoryFragment());
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return true;
    }
}