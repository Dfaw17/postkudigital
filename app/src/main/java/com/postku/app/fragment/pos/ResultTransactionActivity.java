package com.postku.app.fragment.pos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.postku.app.R;
import com.postku.app.actvity.MainActivity;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.DetailTransactionResponse;
import com.postku.app.models.Transaction;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultTransactionActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private TextView kasir, toko, payment, total, tanggal, status;
    private ImageView imgStatus;
    private Button print, backToTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_transaction);
        context = this;
        sessionManager = new SessionManager(context);
        imgStatus = findViewById(R.id.img_status);
        status = findViewById(R.id.text_status);
        tanggal = findViewById(R.id.text_tanggal);
        kasir = findViewById(R.id.text_kasir);
        toko = findViewById(R.id.text_toko);
        payment = findViewById(R.id.text_payment);
        total = findViewById(R.id.text_total);
        print = findViewById(R.id.button3);
        backToTransaction = findViewById(R.id.button4);


        getResult(getIntent().getStringExtra(Constants.ID));

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DHelper.pesan(context, "Ups sorry, this feature is not ready");
            }
        });

        backToTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(Constants.METHOD, Constants.RESET);
                startActivity(intent);
                sessionManager.deleteCart();
                finish();
            }
        });
    }


    private void getResult(String invoice){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detail(invoice).enqueue(new Callback<DetailTransactionResponse>() {
            @Override
            public void onResponse(Call<DetailTransactionResponse> call, Response<DetailTransactionResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        tanggal.setText(DHelper.strTodatetime(response.body().getTransaction().getCreatedAt()));
                        kasir.setText(response.body().getUser().getNama());
                        toko.setText(response.body().getToko().getNama());
                        if(response.body().getTransaction().getPaymentType() == 1){
                            payment.setText("Tunai");
                        }else {
                            payment.setText("QRIS");
                        }
                        total.setText(DHelper.formatRupiah(response.body().getTransaction().getGrandTotal()));
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailTransactionResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}