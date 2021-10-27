package com.postku.app.actvity.wallet.topup;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.postku.app.R;
import com.postku.app.actvity.wallet.RiwayatTopupActivity;
import com.postku.app.adapter.HistoryTopupAdapter;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.GetHistoryTopupResponse;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class TopupSuksesFragment extends Fragment {
    private Context context;
    private SessionManager sessionManager;
    private LinearLayout lempty;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private HistoryTopupAdapter adapter;
    private int idwallet;
    private SwipeRefreshLayout swipe;
    public TopupSuksesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topup_sukses, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        lempty = view.findViewById(R.id.lempty);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.rec_history);
        swipe = view.findViewById(R.id.swipe);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        Log.e(TAG, "walletid:" + ((RiwayatTopupActivity)context).walletId);
        idwallet = ((RiwayatTopupActivity)context).walletId;
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipe.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        lempty.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.historyTopup(String.valueOf(idwallet), "2").enqueue(new Callback<GetHistoryTopupResponse>() {
            @Override
            public void onResponse(Call<GetHistoryTopupResponse> call, Response<GetHistoryTopupResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getDataList().isEmpty()){
                            lempty.setVisibility(View.VISIBLE);
                        }else {
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter = new HistoryTopupAdapter(context, response.body().getDataList());
                            recyclerView.setAdapter(adapter);
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<GetHistoryTopupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }
}