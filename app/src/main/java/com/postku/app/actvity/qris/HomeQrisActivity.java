package com.postku.app.actvity.qris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.postku.app.R;
import com.postku.app.actvity.SelectOutletActivity;
import com.postku.app.adapter.HistoryTransAdapter;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.ClaimRequestJson;
import com.postku.app.json.ClaimResponseJson;
import com.postku.app.json.GetHistoryTransResponse;
import com.postku.app.models.Transaction;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class HomeQrisActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private LinearLayout lsaldo, lriwayat, lclaim;
    private RecyclerView recyclerView;
    private HistoryTransAdapter adapter;
    private List<Integer> idclaim = new ArrayList<>();
    private int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_qris);
        context = this;
        sessionManager = new SessionManager(context);
        lsaldo = findViewById(R.id.lsaldo);
        lclaim = findViewById(R.id.lclaim);
        lriwayat = findViewById(R.id.lriwayat);
        recyclerView = findViewById(R.id.rec_riwayat);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        lclaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmClaim();
            }
        });

        lriwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HistoryClaimActivity.class);
                startActivity(intent);
            }
        });

        getData();

    }

    private void confirmClaim(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_exit_qris, null);
        builder.setView(dialogView);

        final TextView title = dialogView.findViewById(R.id.title);
        final TextView message = dialogView.findViewById(R.id.title2);
        final Button submit = dialogView.findViewById(R.id.btn_submit2);
        final Button dismiss = dialogView.findViewById(R.id.btn_submit);

        title.setText(R.string.title_confirm_claim);
        message.setText(R.string.message_claim_saldo);
        submit.setText("Klaim Saldo");
        dismiss.setText("Batal");


        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                alertDialog.dismiss();
                Log.e(TAG, "total:" + total + " idlist:" + new List[]{idclaim});
                klaim();
                alertDialog.dismiss();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    private void getData(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.settlement(sessionManager.getIdToko()).enqueue(new Callback<GetHistoryTransResponse>() {
            @Override
            public void onResponse(Call<GetHistoryTransResponse> call, Response<GetHistoryTransResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getTransactionList().isEmpty()){
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new HistoryTransAdapter(context, response.body().getTransactionList());
                        recyclerView.setAdapter(adapter);
                        List<Transaction> transactions = response.body().getTransactionList();
                        for(int i=0;i < transactions.size();i++){
                            idclaim.add(transactions.get(i).getId());
                        }
                        total = response.body().getTotal();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetHistoryTransResponse> call, Throwable t) {

            }
        });
    }

    private void klaim(){
        ClaimRequestJson requestJson = new ClaimRequestJson();
        requestJson.setData(idclaim);
        requestJson.setToko(Integer.parseInt(sessionManager.getIdToko()));
        requestJson.setTotal(total);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.claim(requestJson).enqueue(new Callback<ClaimResponseJson>() {
            @Override
            public void onResponse(Call<ClaimResponseJson> call, Response<ClaimResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() ==201){
                        dialogSuccess(response.body().getMessage(), true);
                    }else {
                        dialogSuccess(response.body().getMessage(), false);
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<ClaimResponseJson> call, Throwable t) {

            }
        });

    }

    private void dialogSuccess(String msg, boolean isSuccess){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(true);

        final TextView textView = dialog.findViewById(R.id.text_message);
        final TextView title = dialog.findViewById(R.id.text_message2);
        final ImageView img = dialog.findViewById(R.id.imageView10);

        if(isSuccess){
            textView.setText("Success");
            title.setText("Silahkan lihat status claim saldo di menu riwayat");
            img.setImageDrawable(context.getDrawable(R.drawable.img_success));
        }else {
            textView.setText("Gagal");
            title.setText(msg);
            img.setImageDrawable(context.getDrawable(R.drawable.img_success));
        }



        dialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // Close dialog after 1000ms
                dialog.cancel();

            }
        }, 1000);
    }
}