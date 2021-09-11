package com.postku.app.fragment.pos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.adapter.MenuAdapter;
import com.postku.app.adapter.ProdukAdapter;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.helpers.OnItemClickListener;
import com.postku.app.json.CreateCartResponse;
import com.postku.app.json.DetailCartResponse;
import com.postku.app.json.GetCartResponse;
import com.postku.app.json.GetKategoriResponseJson;
import com.postku.app.json.GetMenuResponseJson;
import com.postku.app.json.InsertItemResponse;
import com.postku.app.models.Cart;
import com.postku.app.models.ItemCart;
import com.postku.app.models.Kategori;
import com.postku.app.models.User;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class ListProdukFragment extends Fragment implements OnItemClickListener {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private int idCat;
    private TextView textView;
    private ProdukAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout lempty;
    private LinearLayout boxCart;
    private TextView qty, total;
    private ImageView imgTable, imgList;
    int idCart = 0;
    private List<ItemCart> itemCartList = new ArrayList<>();
    public ListProdukFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_produk, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        textView = view.findViewById(R.id.textView24);
        idCat = getArguments().getInt(Constants.ID_KATEGORI,0);
        textView.setText(getArguments().getString(Constants.NAMA) + "-" + idCat);
        recyclerView = view.findViewById(R.id.recProduk);
        lempty = view.findViewById(R.id.layout_empty);
        boxCart = view.findViewById(R.id.container);
        qty = view.findViewById(R.id.text_qty);
        total = view.findViewById(R.id.text_total);
        imgTable = view.findViewById(R.id.img_meja);
        imgList = view.findViewById(R.id.img_save);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailOrderActivity.class);
                intent.putExtra(Constants.ID, idCart);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(idCat);
        checkCart();
    }

    private void getData(int id){

        lempty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        if(id > 0){
            service.getMenuByKategori(sessionManager.getIdToko(), String.valueOf(id)).enqueue(new Callback<GetMenuResponseJson>() {
                @Override
                public void onResponse(Call<GetMenuResponseJson> call, Response<GetMenuResponseJson> response) {

                    if(response.isSuccessful()){
                        if(response.body().getStatusCode() == 200){
                            if(response.body().getMenusList().isEmpty()){
                                lempty.setVisibility(View.VISIBLE);
                            }else {
                                adapter = new ProdukAdapter(context, response.body().getMenusList(), ListProdukFragment.this::onItemClick);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(adapter);
                            }
                        }else {
                            lempty.setVisibility(View.VISIBLE);
                            DHelper.pesan(context, response.body().getMsg());
                        }
                    }else {
                        DHelper.pesan(context, context.getString(R.string.error_server));
                    }
                }

                @Override
                public void onFailure(Call<GetMenuResponseJson> call, Throwable t) {

                    t.printStackTrace();
                    Log.e(Constants.TAG, t.getMessage());
                }
            });
        }else {
            service.getMenu(sessionManager.getIdToko()).enqueue(new Callback<GetMenuResponseJson>() {
                @Override
                public void onResponse(Call<GetMenuResponseJson> call, Response<GetMenuResponseJson> response) {

                    if(response.isSuccessful()){
                        if(response.body().getStatusCode() == 200){
                            if(response.body().getMenusList().isEmpty()){
                                lempty.setVisibility(View.VISIBLE);
                            }else {
                                adapter = new ProdukAdapter(context, response.body().getMenusList(), ListProdukFragment.this::onItemClick);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(adapter);
                            }
                        }else {
                            lempty.setVisibility(View.VISIBLE);
                            DHelper.pesan(context, response.body().getMsg());
                        }
                    }else {
                        DHelper.pesan(context, context.getString(R.string.error_server));
                    }
                }

                @Override
                public void onFailure(Call<GetMenuResponseJson> call, Throwable t) {

                    t.printStackTrace();
                    Log.e(Constants.TAG, t.getMessage());
                }
            });
        }
    }

    @Override
    public void onItemClick(int id) {
        addItem(id);
    }


    private void checkCart(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getCart(sessionManager.getIdToko()).enqueue(new Callback<GetCartResponse>() {
            @Override
            public void onResponse(Call<GetCartResponse> call, Response<GetCartResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getCartList().size() > 0){
                            boxCart.setVisibility(View.VISIBLE);
                            Cart cart = response.body().getCartList().get(0);
                            idCart = cart.getId();
                            sessionManager.createCart(String.valueOf(idCart));
                            qty.setText(cart.getTotalItem() + " item");
                            double grandTotal = Math.round(cart.getGrandTotal());
                            total.setText("Total= Rp" + DHelper.toformatRupiah(String.valueOf(grandTotal)));
                        }else {
                            boxCart.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCartResponse> call, Throwable t) {

            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    public void addItem(int id){
        Log.e(TAG, id + "-------------");
        if(idCart > 0){
            detailCart(idCart, id);
        }else {
            createCart(id);
        }

    }


    private void createCart(int id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("user", createPartFromString(user.getId()));
        map.put("toko", createPartFromString(sessionManager.getIdToko()));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.createCart(map).enqueue(new Callback<CreateCartResponse>() {
            @Override
            public void onResponse(Call<CreateCartResponse> call, Response<CreateCartResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 201){
                        Cart cart = response.body().getCart();
                        idCart = cart.getId();
                        addMenuItem(id);
                        checkCart();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateCartResponse> call, Throwable t) {

            }
        });
    }

    private void addMenuItem(int id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("cart", createPartFromString(String.valueOf(idCart)));
        map.put("menu", createPartFromString(String.valueOf(id)));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.addItem(map).enqueue(new Callback<InsertItemResponse>() {
            @Override
            public void onResponse(Call<InsertItemResponse> call, Response<InsertItemResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 201){
                        DHelper.pesan(context, response.body().getMessage());
                        checkCart();
                    }
                }
            }

            @Override
            public void onFailure(Call<InsertItemResponse> call, Throwable t) {

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
                        checkCart();
                    }
                }
            }

            @Override
            public void onFailure(Call<InsertItemResponse> call, Throwable t) {

            }
        });
    }

    private void detailCart(int id, int idItem){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailCart(String.valueOf(id)).enqueue(new Callback<DetailCartResponse>() {
            @Override
            public void onResponse(Call<DetailCartResponse> call, Response<DetailCartResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getItemCartList().size() > 0){
                            itemCartList = response.body().getItemCartList();
                            for(int i=0;i < itemCartList.size();i++){
                                if(itemCartList.get(i).getMenu() == idItem){
                                    Log.e(TAG, "upd idmenu: " + itemCartList.get(i).getMenu() + " iditem:" + idItem);
                                    updMenuItem(itemCartList.get(i).getId(), itemCartList.get(i).getQty() + 1);
                                }else{
                                    Log.e(TAG, "add idmenu: " + itemCartList.get(i).getMenu() + " iditem:" + idItem);
                                    addMenuItem(idItem);
                                }
                            }
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