package com.postku.app.actvity.profil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.fragment.ReferenceFragment;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.models.User;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.NetworkUtils;
import com.postku.app.utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class DataBankActivity extends AppCompatActivity implements ReferenceFragment.UpdateText {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private EditText selectBank, noRekening;
    private ImageView backButton, imgBuku;
    private TextView caption;
    private Button submit;
    private ProgressBar progressBar;
    private String jenisbank="";
    File imageFileOwner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_bank);
        context = this;
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        selectBank = findViewById(R.id.kategori);
        noRekening = findViewById(R.id.edt_phone);
        imgBuku = findViewById(R.id.img_owner);
        submit = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar3);

        caption.setText("Profile Owner");

        Glide.with(context)
                .load(user.getProfilePic())
                .placeholder(R.drawable.image_placeholder)
                .into(imgBuku);

        if(user.getJenisBank() != null){
            selectBank.setText(user.getJenisBank());
        }

        if(user.getNoRekening() != null){
            noRekening.setText(user.getNoRekening());
        }

        final ReferenceFragment dialogFragment = new ReferenceFragment();
        selectBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.METHOD, Constants.BANK);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, TAG);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jenisbank.equalsIgnoreCase("")){
                    DHelper.pesan(context, "Pilih bank");
                    return;
                }else if(noRekening.getText().toString().isEmpty()) {
                    noRekening.setError(context.getString(R.string.error_empty));
                    noRekening.requestFocus();
                    return;
                }

                if(NetworkUtils.isConnected(context)){
                    Log.e(TAG, "completed data");
                    updateAccount(String.valueOf(user.getId()));

                }else{
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }
        });
    }

    private void updateAccount(String idUser){
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_user", createPartFromString(idUser));
        map.put("no_rekening", createPartFromString(noRekening.getText().toString().trim()));
        map.put("jenis_bank", createPartFromString(selectBank.getText().toString().trim()));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFileOwner);
        MultipartBody.Part body = MultipartBody.Part.createFormData("rekening_book_pic", imageFileOwner.getName(), reqFile);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.updateOwner(body, map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    DHelper.pesan(context, "Success");
                    finish();
                }else {
                    Log.e(TAG, response.errorBody().toString());
                    DHelper.pesan(context, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.CAMERA_PROFILE_REQUEST){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgBuku.setImageBitmap(bitmap);
                imageFileOwner = DHelper.createTempFile(context, bitmap);
            }else if(requestCode == Constants.GALERY_PROFILE_REQUEST){
                if(data != null){
                    Uri contentUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                        imgBuku.setImageBitmap(bitmap);
                        imageFileOwner = DHelper.createTempFile(context, bitmap);

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
        if(metode.equalsIgnoreCase(Constants.BANK)){
            selectBank.setText(nama);
            jenisbank = nama;
        }
    }
}