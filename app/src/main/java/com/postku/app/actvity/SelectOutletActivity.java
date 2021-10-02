package com.postku.app.actvity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.adapter.OutletAdapter;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.GetOutletResponseJson;
import com.postku.app.models.User;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectOutletActivity extends AppCompatActivity {
    private Context context;
    private OutletAdapter adapter;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private Button selectOutlet;
    private User user;
    private TextView caption;
    private ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_outlet);
        context = this;
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        recyclerView = findViewById(R.id.rec_outlet);
        selectOutlet = findViewById(R.id.button2);
        caption = findViewById(R.id.text_caption);
        imgBack = findViewById(R.id.back_button);

        caption.setText("Pilih Toko");

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        getToko();
        selectOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getSelectedItem() > 0){
                    sessionManager.setIdToko(String.valueOf(adapter.getSelectedItem()));
                    sessionManager.setNamaToko(adapter.getNamaToko());
                    sessionManager.setAlamatToko(adapter.getAlamatToko());
                    sessionManager.setLogoToko(adapter.getLogoToko());
                    sessionManager.setKategoriToko(adapter.getKategoriToko());
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void getToko(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getToko(user.getId()).enqueue(new Callback<GetOutletResponseJson>() {
            @Override
            public void onResponse(Call<GetOutletResponseJson> call, Response<GetOutletResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode().equalsIgnoreCase("200")){
                        if(response.body().getTokoList().isEmpty()){
                            recyclerView.setVisibility(View.GONE);
                            selectOutlet.setVisibility(View.GONE);
                        }else {
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter = new OutletAdapter(context, response.body().getTokoList());
                            recyclerView.setAdapter(adapter);
                            selectOutlet.setVisibility(View.VISIBLE);
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMsg());
                        selectOutlet.setVisibility(View.GONE);
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                    selectOutlet.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetOutletResponseJson> call, Throwable t) {
                t.printStackTrace();
                Log.e(Constants.TAG, t.getMessage());
                selectOutlet.setVisibility(View.GONE);
            }
        });
    }
}