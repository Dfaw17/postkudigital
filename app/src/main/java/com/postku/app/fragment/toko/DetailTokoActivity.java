package com.postku.app.fragment.toko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.actvity.RegisterActivity;
import com.postku.app.fragment.ReferenceFragment;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.CreateTokoResponse;
import com.postku.app.json.DetailMenuResponse;
import com.postku.app.json.GetDesaResponse;
import com.postku.app.json.GetKecamatanResponse;
import com.postku.app.json.GetKotaResponse;
import com.postku.app.json.GetProvinsiResponse;
import com.postku.app.json.InsertItemResponse;
import com.postku.app.json.PostMenuResponse;
import com.postku.app.models.Menus;
import com.postku.app.models.Toko;
import com.postku.app.models.User;
import com.postku.app.models.location.Kecamatan;
import com.postku.app.models.location.Kelurahan;
import com.postku.app.models.location.Kota;
import com.postku.app.models.location.Provinsi;
import com.postku.app.services.ApiLocationService;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.ReferenceService;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class DetailTokoActivity extends AppCompatActivity implements ReferenceFragment.UpdateText {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private ImageView backButton, imgToko;
    private Button submit;
    private TextView caption, delete;
    private EditText edtNamaToko, edtAlamatToko;
    private AutoCompleteTextView selectKategori, selectProvinsi, selectKota, selectKecamata, selectDesa;
    String[] listCategory;
    ArrayList<String> listProvinsi = new ArrayList<>();
    ArrayList<String> listKab = new ArrayList<>();
    ArrayList<String> listKecamatan = new ArrayList<>();
    ArrayList<String> listDesa = new ArrayList<>();
    List<Provinsi> provinsiList = new ArrayList<>();
    List<Kota> kotaKabList = new ArrayList<>();
    List<Kecamatan> kecamatanList = new ArrayList<>();
    List<Kelurahan> desaList = new ArrayList<>();
    ArrayAdapter<String> adapterKota;
    ArrayAdapter<String> adapterKecamatan;
    ArrayAdapter<String> adapterKelurahan;
    File imageFileToko;
    private ProgressBar progressBar;
    int idProvinsi = 0;
    int idKota = 0;
    int idKecamatan = 0;
    private int id= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_toko);
        context = this;
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        edtNamaToko = findViewById(R.id.edt_nama_toko);
        edtAlamatToko = findViewById(R.id.edt_alamat_toko);
        selectKategori = findViewById(R.id.kategori);
        selectProvinsi = findViewById(R.id.provinsi);
        selectKota = findViewById(R.id.kabkota);
        selectKecamata = findViewById(R.id.kecamatan);
        selectDesa = findViewById(R.id.desa);
        imgToko = findViewById(R.id.img_toko);
        submit = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        delete = findViewById(R.id.text_delete);

        if(getIntent().getStringExtra(Constants.METHOD).equalsIgnoreCase(Constants.ADD)){
            caption.setText("Tambah Toko");
            delete.setVisibility(View.GONE);
        }else {
            caption.setText("Edit Toko");
            delete.setVisibility(View.VISIBLE);
            id = getIntent().getIntExtra(Constants.ID, 0);
            detail(String.valueOf(id));

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteToko(String.valueOf(id));
                }
            });
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        listCategory = getResources().getStringArray(R.array.shop_category);
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listCategory);
        selectKategori.setAdapter(catAdapter);
        selectKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectKategori.showDropDown();
            }
        });

        final ReferenceFragment dialogFragment = new ReferenceFragment();
        selectProvinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.METHOD, Constants.PROVINSI);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, TAG);
            }
        });


        selectKota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.METHOD, Constants.KABKOTA);
                bundle.putInt(Constants.ID, idProvinsi);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, TAG);
            }
        });

        selectKecamata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.METHOD, Constants.KECAMATAN);
                bundle.putInt(Constants.ID, idKota);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, TAG);
            }
        });

        selectDesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.METHOD, Constants.KELURAHAN);
                bundle.putInt(Constants.ID, idKecamatan);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, TAG);
            }
        });

        imgToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailTokoActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.PERMISSION_CAMERA_CODE);
                    return;

                }else if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailTokoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_READ_DATA);
                    return;
                }
                dialogImagePicker();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNamaToko.getText().toString().isEmpty()){
                    edtNamaToko.setError(context.getString(R.string.error_empty));
                    edtNamaToko.requestFocus();
                    return;
                }else if(selectKategori.getText().toString().isEmpty()){
                    selectKategori.setError(context.getString(R.string.error_empty));
                    selectKategori.requestFocus();
                    return;
                }else if(selectProvinsi.getText().toString().isEmpty()){
                    selectProvinsi.setError(context.getString(R.string.error_empty));
                    selectProvinsi.requestFocus();
                    return;
                }else if(selectKota.getText().toString().isEmpty()){
                    selectKota.setError(context.getString(R.string.error_empty));
                    selectKota.requestFocus();
                    return;
                }else if(selectKecamata.getText().toString().isEmpty()){
                    selectKecamata.setError(context.getString(R.string.error_empty));
                    selectKecamata.requestFocus();
                    return;
                }else if(selectDesa.getText().toString().isEmpty()){
                    selectDesa.setError(context.getString(R.string.error_empty));
                    selectDesa.requestFocus();
                    return;
                }

                if(getIntent().getStringExtra(Constants.METHOD).equalsIgnoreCase(Constants.ADD)){
                    createToko();
                }else {
                    updateToko(String.valueOf(id));
                }
            }
        });
    }

    private void getProvinsi(){
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.provinsi().enqueue(new Callback<GetProvinsiResponse>() {
            @Override
            public void onResponse(Call<GetProvinsiResponse> call, Response<GetProvinsiResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getProvinsiList().isEmpty()){
                        Log.e(TAG, "data provinsi not found");
                    }else {

                        provinsiList = response.body().getProvinsiList();
                        for(int i =0; i < provinsiList.size();i++){
                            listProvinsi.add(provinsiList.get(i).getNama());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_dropdown, listProvinsi);
                        selectProvinsi.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProvinsiResponse> call, Throwable t) {

            }
        });
    }

    private void getKotaKabupaten(String id){
//        adapterKota.clear();
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.kota(id).enqueue(new Callback<GetKotaResponse>() {
            @Override
            public void onResponse(Call<GetKotaResponse> call, Response<GetKotaResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getKotaList().size() > 0){
                        kotaKabList = response.body().getKotaList();
                        for(int i =0; i < kotaKabList.size();i++){
                            listKab.add(kotaKabList.get(i).getNama());
                        }
                        adapterKota = new ArrayAdapter<String>(context, R.layout.item_dropdown, listKab);
                        selectKota.setAdapter(adapterKota);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetKotaResponse> call, Throwable t) {

            }
        });
    }

    private void getKecamatan(String id){
//        adapterKecamatan.clear();
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.kecamatan(id).enqueue(new Callback<GetKecamatanResponse>() {
            @Override
            public void onResponse(Call<GetKecamatanResponse> call, Response<GetKecamatanResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getKecamatanList().size() > 0){
                        kecamatanList = response.body().getKecamatanList();
                        for(int i =0; i < kecamatanList.size();i++){
                            listKecamatan.add(kecamatanList.get(i).getNama());
                        }
                        adapterKecamatan = new ArrayAdapter<String>(context, R.layout.item_dropdown, listKecamatan);
                        selectKecamata.setAdapter(adapterKecamatan);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetKecamatanResponse> call, Throwable t) {

            }
        });
    }

    private void getKelurahan(String id){
//        adapterKelurahan.clear();
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.desa(id).enqueue(new Callback<GetDesaResponse>() {
            @Override
            public void onResponse(Call<GetDesaResponse> call, Response<GetDesaResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getKelurahanList().size() > 0){
                        desaList = response.body().getKelurahanList();
                        for(int i =0; i < desaList.size();i++){
                            listDesa.add(desaList.get(i).getNama());
                        }
                        adapterKelurahan = new ArrayAdapter<String>(context, R.layout.item_dropdown, listDesa);
                        selectDesa.setThreshold(2);
                        selectDesa.setAdapter(adapterKelurahan);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDesaResponse> call, Throwable t) {

            }
        });
    }

    private void dialogImagePicker(){
        String[]items = {"Kamera", "Galery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ambil gambar");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, Constants.CAMERA_TOKO_REQUEST);
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivityForResult(intent, Constants.GALERY_TOKO_REQUEST);
                        break;
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void createToko(){
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_user", createPartFromString(user.getId()));
        map.put("nama", createPartFromString(edtNamaToko.getText().toString().trim()));
        map.put("alamat", createPartFromString(edtAlamatToko.getText().toString().trim()));
        map.put("kategori", createPartFromString(selectKategori.getText().toString().trim()));
        map.put("add_provinsi", createPartFromString(selectProvinsi.getText().toString().trim()));
        map.put("add_kab_kot", createPartFromString(selectKota.getText().toString().trim()));
        map.put("add_kecamatan", createPartFromString(selectKecamata.getText().toString().trim()));
        map.put("add_kel_des", createPartFromString(selectDesa.getText().toString().trim()));
        map.put("is_active", createPartFromString("1"));
        MultipartBody.Part body = null;
        if(imageFileToko != null){
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFileToko);
            body = MultipartBody.Part.createFormData("logo", imageFileToko.getName(), reqFile);
        }else{
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), "");
            body = MultipartBody.Part.createFormData("logo", "", reqFile);
        }

        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.createToko(body, map).enqueue(new Callback<CreateTokoResponse>() {
            @Override
            public void onResponse(Call<CreateTokoResponse> call, Response<CreateTokoResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    DHelper.pesan(context, response.body().getMsg());
                    finish();
                }else {
                    Log.e(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<CreateTokoResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

    }

    private void updateToko(String id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_toko", createPartFromString(id));
        map.put("id_user", createPartFromString(user.getId()));
        map.put("nama", createPartFromString(edtNamaToko.getText().toString().trim()));
        map.put("alamat", createPartFromString(edtAlamatToko.getText().toString().trim()));
        map.put("kategori", createPartFromString(selectKategori.getText().toString().trim()));
        map.put("add_provinsi", createPartFromString(selectProvinsi.getText().toString().trim()));
        map.put("add_kab_kot", createPartFromString(selectKota.getText().toString().trim()));
        map.put("add_kecamatan", createPartFromString(selectKecamata.getText().toString().trim()));
        map.put("add_kel_des", createPartFromString(selectDesa.getText().toString().trim()));
        map.put("is_active", createPartFromString("1"));
        MultipartBody.Part body = null;
        if(imageFileToko != null){
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFileToko);
            body = MultipartBody.Part.createFormData("logo", imageFileToko.getName(), reqFile);
        }else{
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), "");
            body = MultipartBody.Part.createFormData("logo", "", reqFile);
        }
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.updateToko(body, map).enqueue(new Callback<CreateTokoResponse>() {
            @Override
            public void onResponse(Call<CreateTokoResponse> call, Response<CreateTokoResponse> response) {
                if(response.isSuccessful()){
                   if(response.body().getStatusCode() == 200){
                       DHelper.pesan(context, response.body().getMsg());
                       finish();
                   }else {
                       DHelper.pesan(context, response.body().getMsg());
                   }
                }
            }

            @Override
            public void onFailure(Call<CreateTokoResponse> call, Throwable t) {

            }
        });
    }

    private void deleteToko(String id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_toko", createPartFromString(id));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.deleteToko(map).enqueue(new Callback<InsertItemResponse>() {
            @Override
            public void onResponse(Call<InsertItemResponse> call, Response<InsertItemResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        DHelper.pesan(context, response.body().getMessage());
                        finish();
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<InsertItemResponse> call, Throwable t) {

            }
        });
    }

    private void detail(String id){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailToko(id).enqueue(new Callback<CreateTokoResponse>() {
            @Override
            public void onResponse(Call<CreateTokoResponse> call, Response<CreateTokoResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        Toko toko = response.body().getToko();
                        edtNamaToko.setText(toko.getNama().toString());
                        edtAlamatToko.setText(toko.getAlamat().toString());
                        selectDesa.setText(toko.getKelDes().toString());
                        selectKecamata.setText(toko.getKecamatan().toString());
                        selectKota.setText(toko.getKabKota().toString());
                        selectProvinsi.setText(toko.getProvinsi().toString());

                        Glide.with(context)
                                .load(toko.getLogo())
                                .placeholder(R.drawable.image_placeholder)
                                .into(imgToko);
                        selectKategori.setText(toko.getKategori());
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateTokoResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, requestCode + "" + resultCode);
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.CAMERA_TOKO_REQUEST){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgToko.setImageBitmap(bitmap);
                imageFileToko = DHelper.createTempFile(context, bitmap);
            }else if(requestCode == Constants.GALERY_TOKO_REQUEST){
                if(data != null){
                    Uri contentUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                        imgToko.setImageBitmap(bitmap);
                        imageFileToko = DHelper.createTempFile(context, bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void updateResult(String id, String nama, String metode) {
        if(metode.equalsIgnoreCase(Constants.PROVINSI)){
            selectProvinsi.setText(nama);
            idProvinsi = Integer.parseInt(id);
        }else if(metode.equalsIgnoreCase(Constants.KABKOTA)){
            selectKota.setText(nama);
            idKota = Integer.parseInt(id);
        }else if(metode.equalsIgnoreCase(Constants.KECAMATAN)){
            selectKecamata.setText(nama);
            idKecamatan = Integer.parseInt(id);
        }else if(metode.equalsIgnoreCase(Constants.KELURAHAN)){
            selectDesa.setText(nama);
        }
    }
}