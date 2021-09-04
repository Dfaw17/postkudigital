package com.postku.app.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.postku.app.R;
import com.postku.app.adapter.BannerAdapter;
import com.postku.app.adapter.NewsAdapter;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.HomeResponseJson;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private Context context;
    private TextView headerText, saldoWallet, saldoQris, totalTrx, totalEarning, totalTrxCash,
    totalTrxQris, totalSold, menuFavorit;
    private ViewPager pager;
    private CircleIndicator circleIndicator;
    private BannerAdapter bannerAdapter;
    private LinearLayout lslider;
    Timer timer;
    final long DELAY_MS = 2000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 4000; // time in milliseconds between successive task executions.
    int currentPage = 0;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private TextView notFoundNews, readMore;
    private SessionManager sessionManager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        lslider = view.findViewById(R.id.lbanner);
        pager = view.findViewById(R.id.pager);
        circleIndicator = view.findViewById(R.id.circle_indicator);
        saldoWallet = view.findViewById(R.id.text_saldo);
        saldoQris = view.findViewById(R.id.text_qris);
        totalTrx = view.findViewById(R.id.text_total_transaksi);
        totalEarning = view.findViewById(R.id.text_pendapatan);
        totalTrxCash = view.findViewById(R.id.text_trx_tunai);
        totalTrxQris = view.findViewById(R.id.text_trx_qris);
        totalSold = view.findViewById(R.id.text_menu_terjual);
        menuFavorit = view.findViewById(R.id.text_menu_terlaris);
        notFoundNews = view.findViewById(R.id.text_not_found);
        readMore = view.findViewById(R.id.text_read_more);
        recyclerView = view.findViewById(R.id.rec_artikel);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getHome(sessionManager.getIdToko());
    }

    private void getHome(String idtoko){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.home(idtoko).enqueue(new Callback<HomeResponseJson>() {
            @Override
            public void onResponse(Call<HomeResponseJson> call, Response<HomeResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){

                        if(response.body().getWallet().equalsIgnoreCase("Wallet Belum Aktif")){
                            saldoWallet.setText("Aktifkan Wallet");
                        }else {
                            saldoWallet.setText(DHelper.toformatRupiah(response.body().getWallet()));
                        }

                        if(response.body().getQris() == null){
                            saldoQris.setText("Aktifkan Qris");
                        }else {
                            saldoQris.setText(DHelper.toformatRupiah(response.body().getWallet()));
                        }
                        totalTrx.setText(String.valueOf(response.body().getTotaltrx()));
                        totalEarning.setText(DHelper.toformatRupiah(response.body().getPendapatan()));
                        totalTrxCash.setText(DHelper.toformatRupiah(response.body().getTrxTunai()));
                        totalTrxQris.setText(DHelper.toformatRupiah(response.body().getTrxQris()));
                        totalSold.setText(DHelper.toformatRupiah(response.body().getMenuTerjual()));
                        menuFavorit.setText(response.body().getMenuTerlaris());

                        if(response.body().getBannerList().isEmpty()){
                            Log.e(Constants.TAG, "sini");
                            lslider.setVisibility(View.GONE);
                        }else {
                            Log.e(Constants.TAG, "sinidong");
                            lslider.setVisibility(View.VISIBLE);
                            final int NUM_PAGES = response.body().getBannerList().size();
                            bannerAdapter = new BannerAdapter(context, response.body().getBannerList());
                            pager.setAdapter(bannerAdapter);
                            circleIndicator.setViewPager(pager);
                            pager.setPadding(0,0,0,0);

                            final Handler handler = new Handler();
                            final  Runnable update = new Runnable() {
                                @Override
                                public void run() {
                                    if(currentPage == NUM_PAGES){
                                        currentPage = 0;
                                    }
                                    pager.setCurrentItem(currentPage++, true);
                                }
                            };
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(update);
                                }
                            }, DELAY_MS, PERIOD_MS);

                        }

                        if(response.body().getArtikelList().isEmpty()){
                            notFoundNews.setVisibility(View.VISIBLE);
                            readMore.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            notFoundNews.setVisibility(View.GONE);
                            readMore.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter = new NewsAdapter(context, response.body().getArtikelList());
                            recyclerView.setAdapter(adapter);

                        }

                    }else{
                        DHelper.pesan(context, response.body().getMsg());
                    }

                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<HomeResponseJson> call, Throwable t) {
                t.printStackTrace();
                Log.e(Constants.TAG, t.getMessage());
            }
        });
    }
}