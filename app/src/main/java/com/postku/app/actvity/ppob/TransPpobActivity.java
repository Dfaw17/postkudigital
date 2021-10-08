package com.postku.app.actvity.ppob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.adapter.BrandAdapter;
import com.postku.app.adapter.ProductPpobAdapter;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.GetProdukPponResponse;
import com.postku.app.json.PpobProductResponse;
import com.postku.app.models.ProductPpob;
import com.postku.app.models.User;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class TransPpobActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private ImageView backButton, logo;
    private TextView caption, namaProduk;
    private RecyclerView recyclerView;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private EditText nomor;
    private String category, brand, urlLogo;
    private LinearLayout lempty;
    private ProductPpobAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_ppob);
        context = this;
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        backButton = findViewById(R.id.back_button);
        logo = findViewById(R.id.img_logo);
        caption = findViewById(R.id.text_caption);
        namaProduk = findViewById(R.id.text_nama);
        nomor = findViewById(R.id.edt_nomor);
        recyclerView = findViewById(R.id.rec_product);
        lempty = findViewById(R.id.lempty);
        View bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        caption.setText("Pilih Produk");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        category = getIntent().getStringExtra(Constants.METHOD);
        brand = getIntent().getStringExtra(Constants.NAMA);
        urlLogo = getIntent().getStringExtra(Constants.ADD);

        namaProduk.setText(brand);
        Glide.with(context)
                .load(urlLogo)
                .placeholder(R.drawable.image_placeholder)
                .into(logo);
        nomor.setText("0" + user.getPhone());
        nomor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(nomor.getText().toString().length() > 0){
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                        if(motionEvent.getRawX() >= (nomor.getRight() - nomor
                                .getCompoundDrawables()[DRAWABLE_RIGHT]
                                .getBounds()
                                .width())){
                            nomor.setText("");
                            return true;
                        }
                    }
                }
                return false;
            }
        });


        getData(category, brand);

    }

    private void getData(String cat, String merk) {
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.ppob(cat, merk).enqueue(new Callback<GetProdukPponResponse>() {
            @Override
            public void onResponse(Call<GetProdukPponResponse> call, Response<GetProdukPponResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getProductPpobList().isEmpty()){
                            lempty.setVisibility(View.VISIBLE);
                        }else {
                            adapter = new ProductPpobAdapter(context, response.body().getProductPpobList(), TransPpobActivity.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<GetProdukPponResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void showDetailProduct(ProductPpob ppob){
        if(nomor.getText().toString().isEmpty()){
            nomor.setError("Masukkan nomor");
            nomor.requestFocus();
            return;
        }
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        @SuppressLint("InflateParams") final View mDialog = getLayoutInflater().inflate(R.layout.bottom_sheet_ppob, null);
        TextView textNomor = mDialog.findViewById(R.id.text_nomor);
        TextView textKategori = mDialog.findViewById(R.id.text_kategori);
        TextView textBrand = mDialog.findViewById(R.id.text_brand);
        TextView textSaldo = mDialog.findViewById(R.id.text_saldo);
        TextView textHarga = mDialog.findViewById(R.id.text_harga);
        TextView textBiaya = mDialog.findViewById(R.id.text_biaya);
        TextView textTotal = mDialog.findViewById(R.id.text_total);
        Button ubah = mDialog.findViewById(R.id.btn_submit);
        Button bayar = mDialog.findViewById(R.id.btn_submit2);

        int harga = 0;
        int biaya = 0;
        int total = 0;

        harga = ppob.getPricePostku();
        total = harga + biaya;
        textNomor.setText(nomor.getText().toString());
        textKategori.setText(ppob.getCategory());
        textBrand.setText(ppob.getBrand());
        textHarga.setText(DHelper.toformatRupiah(String.valueOf(ppob.getPricePostku())));
        textBiaya.setText(DHelper.toformatRupiah(String.valueOf(biaya)));
        textTotal.setText(DHelper.toformatRupiah(String.valueOf(total)));

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

            }
        });

        mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.setContentView(mDialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(mBottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

    private void startTrans(){

    }
}