package com.postku.app.fragment.pos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.adapter.ItemCartAdapter;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.DetailCartResponse;
import com.postku.app.models.Cart;
import com.postku.app.models.User;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class DetailOrderActivity extends AppCompatActivity {
    private Context context;
    private User user;
    private SessionManager sessionManager;
    private TextView totalItems,subTotal, discount, customer, meja, tipeOrder, labelOrder,
    paymentMethod, grandTotal, deleteCart;
    private RelativeLayout rlDiskon, rlCustomer, rlTable, rlTipe, rlLabel, rlPajak, rlService;
    private RecyclerView recyclerView;
    private Button simpan, bayar;
    private ItemCartAdapter adapter;
    private ImageView backButton;
    private TextView caption;
    double totalTagihan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        context = this;
        user = BaseApp.getInstance(context).getLoginUser();
        sessionManager = new SessionManager(context);
        totalItems = findViewById(R.id.text_total_item);
        recyclerView = findViewById(R.id.rec_item_cart);
        rlDiskon = findViewById(R.id.rldiscount);
        rlCustomer = findViewById(R.id.rlpelanggan);
        rlTable = findViewById(R.id.rlmeja);
        rlTipe = findViewById(R.id.rltipe);
        rlLabel = findViewById(R.id.rllabel);
        rlPajak = findViewById(R.id.rlpajak);
        rlService = findViewById(R.id.rlservice);
        subTotal = findViewById(R.id.text_subtotal);
        discount = findViewById(R.id.text_discount);
        customer = findViewById(R.id.text_customer);
        meja = findViewById(R.id.text_meja);
        tipeOrder = findViewById(R.id.text_tipe_order);
        labelOrder = findViewById(R.id.text_label_order);
        paymentMethod = findViewById(R.id.text_payment);
        grandTotal = findViewById(R.id.text_total);
        deleteCart = findViewById(R.id.text_delete);
        simpan = findViewById(R.id.btn_print3);
        bayar = findViewById(R.id.btn_print2);
        caption = findViewById(R.id.text_caption);
        backButton = findViewById(R.id.back_button);

        caption.setText("Detail Order");

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaymentActivity.class);
                intent.putExtra(Constants.ID, getIntent().getIntExtra(Constants.ID, 0));
                intent.putExtra(Constants.ADD, totalTagihan);
                startActivity(intent);
            }
        });

        detail(getIntent().getIntExtra(Constants.ID, 0));
    }

    private void detail(int id){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailCart(String.valueOf(id)).enqueue(new Callback<DetailCartResponse>() {
            @Override
            public void onResponse(Call<DetailCartResponse> call, Response<DetailCartResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        totalItems.setText(response.body().getJmlItem() + " Items");
                        Cart cart = new Cart();
                        cart = response.body().getCart();
                        subTotal.setText(DHelper.formatRupiah(cart.getTotalPrice()));
                        grandTotal.setText(DHelper.formatRupiah(cart.getGrandTotal()));
                        totalTagihan = Math.round(cart.getGrandTotal());

                        if(response.body().getItemCartList().size() > 0){
                            adapter = new ItemCartAdapter(context, response.body().getItemCartList());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }else {
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailCartResponse> call, Throwable t) {

            }
        });
    }
}