package com.postku.app.fragment.pos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.adapter.ItemCartAdapter;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.helpers.OnCartItemClickListener;
import com.postku.app.json.CreateCartResponse;
import com.postku.app.json.DetailCartResponse;
import com.postku.app.json.InsertItemResponse;
import com.postku.app.models.Cart;
import com.postku.app.models.User;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class DetailOrderActivity extends AppCompatActivity implements OnCartItemClickListener {
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
    private LinearLayout main;
    private ProgressBar progressBar;
    double totalTagihan;
    public int quantity=0;
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
        progressBar = findViewById(R.id.progressBar);
        main = findViewById(R.id.main);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        caption.setText("Detail Order");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaymentActivity.class);
                intent.putExtra(Constants.ID, getIntent().getIntExtra(Constants.ID, 0));
                intent.putExtra(Constants.ADD, totalTagihan);
                startActivity(intent);
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmSave(getIntent().getIntExtra(Constants.ID, 0));
            }
        });

        detail(getIntent().getIntExtra(Constants.ID, 0));
    }

    private void simpanCart(int id, String label) {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_cart", createPartFromString(String.valueOf(id)));
        map.put("nama_cart", createPartFromString(label));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.simpanCart(map).enqueue(new Callback<CreateCartResponse>() {
            @Override
            public void onResponse(Call<CreateCartResponse> call, Response<CreateCartResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        DHelper.pesan(context, response.body().getMessage());
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateCartResponse> call, Throwable t) {

            }
        });
    }

    private void showConfirmSave(int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_kategori, null);
        builder.setView(dialogView);

        final TextView title = dialogView.findViewById(R.id.title);
        final TextView delete = dialogView.findViewById(R.id.delete);
        final EditText editText = dialogView.findViewById(R.id.edittext);
        final Button submit = dialogView.findViewById(R.id.btn_submit);

        title.setText("Konfirmasi Simpan Pesanan");
        editText.setHint("nama pesanan");
        delete.setVisibility(View.GONE);

        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty()){
                    editText.setError(context.getString(R.string.error_empty));
                    editText.requestFocus();
                    return;
                }

                simpanCart(id, editText.getText().toString());
                alertDialog.dismiss();
            }
        });
    }

    private void showEditDialog(int id, int qty, String nama, double harga){
        quantity = qty;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_edit_item, null);
        builder.setView(dialogView);

        final TextView textProduk = dialogView.findViewById(R.id.text_produk);
        final TextView textPrice = dialogView.findViewById(R.id.text_harga);
        final TextView textQty = dialogView.findViewById(R.id.text_qty);
        final Button min = dialogView.findViewById(R.id.btn_min);
        final Button plus = dialogView.findViewById(R.id.btn_plus);
        final Button simpan = dialogView.findViewById(R.id.btn_submit);
        final RelativeLayout rldiskon = dialogView.findViewById(R.id.rldiscount);
        final ImageView imgDelete = dialogView.findViewById(R.id.img_delete);

        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        textProduk.setText(nama);
        textPrice.setText(DHelper.toformatRupiah(String.valueOf(harga)));
        textQty.setText(qty + "");
        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity - 1 >=0){
                    quantity--;
                    textQty.setText(quantity + "");
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                textQty.setText(quantity + "");
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updMenuItem(id, quantity);
                alertDialog.dismiss();
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMenuItem(id);
                alertDialog.dismiss();
            }
        });


    }

    private void detail(int id){
        progressBar.setVisibility(View.VISIBLE);
        main.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailCart(String.valueOf(id)).enqueue(new Callback<DetailCartResponse>() {
            @Override
            public void onResponse(Call<DetailCartResponse> call, Response<DetailCartResponse> response) {
                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        main.setVisibility(View.VISIBLE);
                        totalItems.setText(response.body().getJmlItem() + " Items");
                        Cart cart = new Cart();
                        cart = response.body().getCart();
                        subTotal.setText(DHelper.formatRupiah(cart.getTotalPrice()));
                        grandTotal.setText(DHelper.formatRupiah(cart.getGrandTotal()));
                        totalTagihan = Math.round(cart.getGrandTotal());

                        if(response.body().getItemCartList().size() > 0){
                            adapter = new ItemCartAdapter(context, response.body().getItemCartList(), DetailOrderActivity.this::onItemClick);
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
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void updMenuItem(int id, int qty){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_cart_item", createPartFromString(String.valueOf(id)));
        map.put("qty", createPartFromString(String.valueOf(qty)));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.updateItem(map).enqueue(new Callback<InsertItemResponse>() {
            @Override
            public void onResponse(Call<InsertItemResponse> call, Response<InsertItemResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        DHelper.pesan(context, response.body().getMessage());
                        detail(getIntent().getIntExtra(Constants.ID, 0));
                    }
                }
            }

            @Override
            public void onFailure(Call<InsertItemResponse> call, Throwable t) {

            }
        });
    }

    private void deleteMenuItem(int id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_cart_item", createPartFromString(String.valueOf(id)));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.deleteItem(map).enqueue(new Callback<InsertItemResponse>() {
            @Override
            public void onResponse(Call<InsertItemResponse> call, Response<InsertItemResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        DHelper.pesan(context, response.body().getMessage());
                        detail(getIntent().getIntExtra(Constants.ID, 0));
                    }
                }
            }

            @Override
            public void onFailure(Call<InsertItemResponse> call, Throwable t) {

            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    @Override
    public void onItemClick(int id, int qty, String menuName, int price, int method) {
        if(method == 1){
            showEditDialog(id, qty, menuName, price);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("konfirmasi");
            builder.setMessage("Apakah yakin hapus item ini?");
            builder.setPositiveButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteMenuItem(id);
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
}