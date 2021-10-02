package com.postku.app.fragment.riwayat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.postku.app.R;
import com.postku.app.adapter.HistoryTransAdapter;
import com.postku.app.adapter.ItemCartAdapter;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.helpers.OnCartItemClickListener;
import com.postku.app.json.DetailTransactionResponse;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class DetailTransaksiActivity extends AppCompatActivity implements OnCartItemClickListener {
    private Context context;
    private SessionManager sessionManager;
    private ImageView backButton;
    private TextView caption, invoice, kasir, tanggal, subtotal, diskon, customer, meja, tipeOrder,
                     labelOrder, payment, grandTotal, qty;
    private RecyclerView recyclerView;
    private Button printStruk;
    private ProgressBar progressBar;
    private LinearLayout main;
    private String inv;
    private ItemCartAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
        context = this;
        sessionManager = new SessionManager(context);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        invoice = findViewById(R.id.text_no_transaksi);
        kasir = findViewById(R.id.text_nama_kasir);
        tanggal = findViewById(R.id.text_tanggal);
        subtotal = findViewById(R.id.text_subtotal);
        diskon = findViewById(R.id.text_discount);
        customer = findViewById(R.id.text_customer);
        meja = findViewById(R.id.text_meja);
        tipeOrder = findViewById(R.id.text_tipe_order);
        labelOrder = findViewById(R.id.text_label_order);
        payment = findViewById(R.id.text_payment);
        grandTotal = findViewById(R.id.text_total);
        qty = findViewById(R.id.text_total_item);
        recyclerView = findViewById(R.id.rec_rincian);
        printStruk = findViewById(R.id.btn_print);
        progressBar = findViewById(R.id.progressBar);
        main = findViewById(R.id.main_content);

        inv = getIntent().getStringExtra(Constants.ID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        getDetail(inv);
        caption.setText("Detail Transaksi");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void getDetail(String kode){
        progressBar.setVisibility(View.VISIBLE);
        main.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detail(kode).enqueue(new Callback<DetailTransactionResponse>() {
            @Override
            public void onResponse(Call<DetailTransactionResponse> call, Response<DetailTransactionResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        main.setVisibility(View.VISIBLE);
                        invoice.setText(kode);
                        tanggal.setText(DHelper.strTodatetime(response.body().getTransaction().getCreatedAt()));
                        kasir.setText(response.body().getUser().getNama());
                        if(response.body().getTransaction().getPaymentType() == 1){
                            payment.setText("Tunai");
                        }else {
                            payment.setText("QRIS");
                        }
                        subtotal.setText(DHelper.formatRupiah(response.body().getCart().getTotalPrice()));
                        diskon.setText(DHelper.formatRupiah(response.body().getCart().getTotalDisc()));
                        grandTotal.setText(DHelper.formatRupiah(response.body().getCart().getGrandTotal()));
                        qty.setText(response.body().getCart().getTotalItem() + " Items");
                        if(response.body().getItemCarts().size() > 0){
                            adapter = new ItemCartAdapter(context, response.body().getItemCarts(), DetailTransaksiActivity.this::onItemClick, false);
                            recyclerView.setAdapter(adapter);
                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<DetailTransactionResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemClick(int id, int qty, String nama, int harga, int method) {

    }
}