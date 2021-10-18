package com.postku.app.actvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.actvity.plus.PostkuPlusActivity;
import com.postku.app.actvity.profil.ProfileActivity;
import com.postku.app.fragment.HomeFragment;
import com.postku.app.fragment.absensi.AbsensiFragment;
import com.postku.app.fragment.customer.CustomerFragment;
import com.postku.app.fragment.diskon.DiskonFragment;
import com.postku.app.fragment.meja.TableFragment;
import com.postku.app.fragment.pegawai.PegawaiFragment;
import com.postku.app.fragment.pos.PosFragment;
import com.postku.app.fragment.product.ProdukFragment;
import com.postku.app.fragment.report.ReportFragment;
import com.postku.app.fragment.riwayat.HistoryFragment;
import com.postku.app.fragment.setting.SettingFragment;
import com.postku.app.fragment.toko.TokoFragment;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.GetKritikResponse;
import com.postku.app.models.User;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView textNama, textJabatan, textNamaToko;
    private CircleImageView avatar;
    String[] listCategory;
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
        RelativeLayout rlsubs = headerView.findViewById(R.id.rlupgrade);
        RelativeLayout rlprofile = headerView.findViewById(R.id.rlprofile);



        rlsubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostkuPlusActivity.class);
                startActivity(intent);
            }
        });

        rlprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
            }
        });

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
                    case R.id.kasir:
                        loadFragment(new PosFragment());
                        break;
                    case R.id.report:
                        loadFragment(new ReportFragment());
                        break;
                    case R.id.toko:
                        loadFragment(new TokoFragment());
                        break;
                    case R.id.pegawai:
                        loadFragment(new PegawaiFragment());
                        break;
                    case R.id.menu:
                        loadFragment(new ProdukFragment());
                        break;
                    case R.id.absensi:
                        loadFragment(new AbsensiFragment());
                        break;
                    case R.id.meja:
                        loadFragment(new TableFragment());
                        break;
                    case R.id.diskon:
                        loadFragment(new DiskonFragment());
                        break;
                    case R.id.customer:
                        loadFragment(new CustomerFragment());
                        break;
                    case R.id.chat:
                        toolbar.setTitle("Beranda");
                        showContactUs();
                        break;
                    case R.id.spacer:
                        toolbar.setTitle("Beranda");
                        showDialogKritik();
                        break;
                    case R.id.setting:
                        loadFragment(new SettingFragment());
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        if(getIntent().getStringExtra(Constants.METHOD) != null){
            toolbar.setTitle("Kasir");
            loadFragment(new PosFragment());
        }else {
            toolbar.setTitle("Beranda");
            loadFragment(new HomeFragment());
        }

    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    private void showContactUs(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_contactus, null);
        builder.setView(dialogView);

        final RelativeLayout wasap = dialogView.findViewById(R.id.rlwa);
        final RelativeLayout instagram = dialogView.findViewById(R.id.rlinsta);

        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        wasap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void showDialogKritik(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_stock, null);
        builder.setView(dialogView);

        final TextView title = dialogView.findViewById(R.id.title);
        final AutoCompleteTextView selectKategori = dialogView.findViewById(R.id.kategori);
        final EditText edtStock = dialogView.findViewById(R.id.edittext);
        final EditText edtNote = dialogView.findViewById(R.id.edittext2);
        final Button submit = dialogView.findViewById(R.id.btn_submit);

        selectKategori.setHint("Pilih");
        title.setText("Saran dan Kritik");
        edtStock.setVisibility(View.GONE);

        listCategory = getResources().getStringArray(R.array.list_saran);
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listCategory);
        selectKategori.setAdapter(catAdapter);
        selectKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectKategori.showDropDown();
            }
        });
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "pos:" + selectKategori.getText().toString().trim());
                if(selectKategori.getText().toString().isEmpty()){
                    selectKategori.setError(context.getString(R.string.error_empty));
                    return;
                }else if(edtNote.getText().toString().isEmpty()){
                    edtNote.setError(context.getString(R.string.error_empty));
                    edtNote.requestFocus();
                    return;
                }

                postKritik(selectKategori.getText().toString(), edtNote.getText().toString());
                alertDialog.dismiss();
            }
        });
    }

    private void postKritik(String label, String isi){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.kritiksaran(user.getId(), label, isi).enqueue(new Callback<GetKritikResponse>() {
            @Override
            public void onResponse(Call<GetKritikResponse> call, Response<GetKritikResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 201){
                        DHelper.pesan(context, response.body().getMessage());
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<GetKritikResponse> call, Throwable t) {

            }
        });
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

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 1) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();
//            layout.setVisibility(View.VISIBLE);
        } else {
            // Otherwise, ask user if he wants to leave :)

            new AlertDialog.Builder(this)
                    .setTitle("Konfirmasi?")
                    .setMessage("Apakah yakin akan keluar aplikasi?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // MainActivity.super.onBackPressed();
                            finish();
                            moveTaskToBack(true);
                        }
                    }).create().show();
        }
    }
}