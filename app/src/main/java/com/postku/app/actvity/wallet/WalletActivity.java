package com.postku.app.actvity.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.postku.app.R;
import com.postku.app.actvity.ppob.PpobKategoriActivity;
import com.postku.app.actvity.ppob.RiwayatPpobActivity;
import com.postku.app.actvity.qris.HomeQrisActivity;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.WalletResponseJson;
import com.postku.app.models.Wallet;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private LinearLayout topup, qris, ppob, pulsa, games, emoney, ewallet, pulsadata, voucher, pln, lainnya;
    private RelativeLayout rlriwayatpostku, rlriwayattopup, rlriwayatppob;
    private TextView textSaldo;
    private int walletid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        context = this;
        sessionManager = new SessionManager(context);
        textSaldo = findViewById(R.id.text_saldo);
        topup = findViewById(R.id.ltopup);
        qris = findViewById(R.id.lqris);
        ppob = findViewById(R.id.lppob);
        pulsa = findViewById(R.id.lpulsa);
        games = findViewById(R.id.lgames);
        emoney = findViewById(R.id.lemoney);
        ewallet = findViewById(R.id.lewallet);
        pulsadata = findViewById(R.id.ldata);
        voucher = findViewById(R.id.lvoucher);
        pln = findViewById(R.id.lpln);
        lainnya = findViewById(R.id.lmenulain);
        rlriwayatpostku = findViewById(R.id.rl_history_postku);
        rlriwayattopup = findViewById(R.id.rl_history_topup);
        rlriwayatppob = findViewById(R.id.rl_history_ppob);

        walletid = getIntent().getIntExtra(Constants.ID, 0);

        sessionManager.setIdWallet(String.valueOf(walletid));
        textSaldo.setText("Rp" + DHelper.toformatRupiah(String.valueOf(getIntent().getIntExtra(Constants.NOMINAL,0))));
        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatusDeposit();
            }
        });

        qris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeQrisActivity.class);
                startActivity(intent);
            }
        });

        ppob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PpobKategoriActivity.class);
                startActivity(intent);
            }
        });

        rlriwayatpostku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RiwayatPayActivity.class);
                intent.putExtra(Constants.ID, walletid);
                startActivity(intent);
            }
        });

        rlriwayattopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RiwayatTopupActivity.class);
                intent.putExtra(Constants.ID, walletid);
                startActivity(intent);
            }
        });

        rlriwayatppob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RiwayatPpobActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkStatusDeposit(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailWallet(sessionManager.getIdToko()).enqueue(new Callback<WalletResponseJson>() {
            @Override
            public void onResponse(Call<WalletResponseJson> call, Response<WalletResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        Wallet wallet = response.body().getWallet();
                        Log.e("BALANCE", String.valueOf(wallet.getBalance()));
                        textSaldo.setText("Rp" + DHelper.toformatRupiah(String.valueOf(wallet.getBalance())));
                        if(wallet.getStatusReqDepo() == 1) {
                            Intent intent = new Intent(context, KonfirmasiTopupActivity.class);
                            intent.putExtra(Constants.ID, wallet.getId());
                            intent.putExtra(Constants.NOMINAL, wallet.getBalanceReq());
                            intent.putExtra(Constants.METHOD, wallet.getStatusReqDepo());
                            startActivity(intent);

                        }else if(wallet.getStatusReqDepo() == 2){
                            Intent intent = new Intent(context, TopupPendingActivity.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(context, TopUpSaldoActivity.class);
                            intent.putExtra(Constants.ID, wallet.getId());
                            intent.putExtra(Constants.NOMINAL, wallet.getBalanceReq());
                            intent.putExtra(Constants.METHOD, wallet.getStatusReqDepo());
                            startActivity(intent);

                        }
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<WalletResponseJson> call, Throwable t) {

            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}