package com.postku.app.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.postku.app.R;
import com.postku.app.adapter.ReferenceAdapter;
import com.postku.app.helpers.ClickInterface;
import com.postku.app.helpers.Constants;
import com.postku.app.json.GetDesaResponse;
import com.postku.app.json.GetKecamatanResponse;
import com.postku.app.json.GetKotaResponse;
import com.postku.app.json.GetProvinsiResponse;
import com.postku.app.models.location.Kecamatan;
import com.postku.app.models.location.Kelurahan;
import com.postku.app.models.location.Kota;
import com.postku.app.models.location.Provinsi;
import com.postku.app.models.location.Reference;
import com.postku.app.services.ApiLocationService;
import com.postku.app.services.api.ReferenceService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class ReferenceFragment extends DialogFragment {
    private Context context;
    private SessionManager sessionManager;
    private List<Reference> referenceList = new ArrayList<>();
    private ReferenceAdapter adapter;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EditText search;
    private int id;
    private String metode;
    private ClickInterface clickInterface;
    private ProgressBar progressBar;
    public ReferenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reference, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        toolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recycler);
        search = view.findViewById(R.id.edt_search);
        progressBar = view.findViewById(R.id.progressBar4);

        id = getArguments().getInt(Constants.ID, 0);
        metode = getArguments().getString(Constants.METHOD);

        clickInterface = new ClickInterface() {
            @Override
            public void onItemSelected(String id, String nama) {
                UpdateText updateText = (UpdateText)getActivity();
                updateText.updateResult(id, nama, metode);
                dismiss();
            }

        };

        adapter = new ReferenceAdapter(context, referenceList, clickInterface);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if(metode.equalsIgnoreCase(Constants.PROVINSI)){
            toolbar.setTitle("Cari Provinsi");
            getProvinsi();
        }else if(metode.equalsIgnoreCase(Constants.KABKOTA)){
            toolbar.setTitle("Cari Kabupaten/Kota");
            getKotaKabupaten(String.valueOf(id));
        }else if(metode.equalsIgnoreCase(Constants.KECAMATAN)){
            toolbar.setTitle("Cari Kecamatan");
            getKecamatan(String.valueOf(id));
        }else if(metode.equalsIgnoreCase(Constants.KELURAHAN)){
            toolbar.setTitle("Cari Desa/Kelurahan");
            getKelurahan(String.valueOf(id));
        }

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(adapter !=null){
                    if(s.length() == 0){
                        adapter.getFilter().filter("");
                    }else {
                        adapter.getFilter().filter(s.toString());
                    }
                }
            }
        });


        return view;
    }

    private void getProvinsi(){
        referenceList.clear();
        progressBar.setVisibility(View.VISIBLE);
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.provinsi().enqueue(new Callback<GetProvinsiResponse>() {
            @Override
            public void onResponse(Call<GetProvinsiResponse> call, Response<GetProvinsiResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getProvinsiList().isEmpty()){
                        Log.e(TAG, "data provinsi not found");
                    }else {
                        List<Provinsi> provinsiList = response.body().getProvinsiList();
                        for(int i =0; i < provinsiList.size();i++){
                            Reference reference = new Reference();
                            reference.setId(String.valueOf(provinsiList.get(i).getId()));
                            reference.setNama(provinsiList.get(i).getNama());
                            referenceList.add(reference);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProvinsiResponse> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getKotaKabupaten(String id){
        referenceList.clear();
        progressBar.setVisibility(View.VISIBLE);
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.kota(id).enqueue(new Callback<GetKotaResponse>() {
            @Override
            public void onResponse(Call<GetKotaResponse> call, Response<GetKotaResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getKotaList().size() > 0){
                        List<Kota> kotaKabList = response.body().getKotaList();
                        for(int i =0; i < kotaKabList.size();i++){
                            Reference reference = new Reference();
                            reference.setId(String.valueOf(kotaKabList.get(i).getId()));
                            reference.setNama(kotaKabList.get(i).getNama());
                            referenceList.add(reference);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetKotaResponse> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getKecamatan(String id){
        referenceList.clear();
        progressBar.setVisibility(View.VISIBLE);
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.kecamatan(id).enqueue(new Callback<GetKecamatanResponse>() {
            @Override
            public void onResponse(Call<GetKecamatanResponse> call, Response<GetKecamatanResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getKecamatanList().size() > 0){
                        List<Kecamatan> kecamatanList = response.body().getKecamatanList();
                        for(int i =0; i < kecamatanList.size();i++){
                            Reference reference = new Reference();
                            reference.setId(String.valueOf(kecamatanList.get(i).getId()));
                            reference.setNama(kecamatanList.get(i).getNama());
                            referenceList.add(reference);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetKecamatanResponse> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void getKelurahan(String id){
        referenceList.clear();
        progressBar.setVisibility(View.VISIBLE);
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.desa(id).enqueue(new Callback<GetDesaResponse>() {
            @Override
            public void onResponse(Call<GetDesaResponse> call, Response<GetDesaResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getKelurahanList().size() > 0){
                        List<Kelurahan> desaList = response.body().getKelurahanList();
                        for(int i =0; i < desaList.size();i++){
                            Reference reference = new Reference();
                            reference.setId(desaList.get(i).getId());
                            reference.setNama(desaList.get(i).getNama());
                            referenceList.add(reference);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDesaResponse> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public interface UpdateText{
        void updateResult(String id, String nama, String metode);
    }
}