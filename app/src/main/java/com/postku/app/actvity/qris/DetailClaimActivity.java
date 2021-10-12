package com.postku.app.actvity.qris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postku.app.R;
import com.postku.app.adapter.HistoryTransAdapter;
import com.postku.app.fragment.ImageViewerFragment;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.ClaimResponseJson;
import com.postku.app.json.GetHistoryTransResponse;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.SessionManager;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class DetailClaimActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private TextView reffCode, status, total, caption;
    private RelativeLayout rlcekbukti;
    private RecyclerView recyclerView;
    private ImageView backButton;
    private HistoryTransAdapter adapter;
    private String imgUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_claim);
        context = this;
        sessionManager = new SessionManager(context);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        reffCode = findViewById(R.id.text_reff_code);
        status = findViewById(R.id.text_status);
        total = findViewById(R.id.text_total);
        rlcekbukti = findViewById(R.id.rlcekbukti);
        recyclerView = findViewById(R.id.rec_history);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        final ImageViewerFragment dialogFragment = new ImageViewerFragment();
        rlcekbukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_DATA, imgUrl);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, TAG);
            }
        });

        getData(String.valueOf(getIntent().getIntExtra(Constants.ID, 0)));
    }

    private void getData(String id){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailSettlement(id).enqueue(new Callback<ClaimResponseJson>() {
            @Override
            public void onResponse(Call<ClaimResponseJson> call, Response<ClaimResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        reffCode.setText(response.body().getData().getName());
                        total.setText(DHelper.toformatRupiah(String.valueOf(response.body().getData().getTotal())));
                        imgUrl = response.body().getData().getImage();
                        if(response.body().getData().getTransactionList().isEmpty()){
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter = new HistoryTransAdapter(context, response.body().getData().getTransactionList());
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ClaimResponseJson> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}