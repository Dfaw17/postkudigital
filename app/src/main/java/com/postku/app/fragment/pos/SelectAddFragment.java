package com.postku.app.fragment.pos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.postku.app.R;
import com.postku.app.adapter.CustomerAdapter;
import com.postku.app.adapter.PromoAdapter;
import com.postku.app.adapter.SelectAdapter;
import com.postku.app.adapter.ServiceFeeAdapter;
import com.postku.app.adapter.TableSelectAdapter;
import com.postku.app.adapter.TaxAdapter;
import com.postku.app.fragment.customer.CustomerFragment;
import com.postku.app.fragment.diskon.fee.ServiceFeeFragment;
import com.postku.app.fragment.diskon.promo.PromoFragment;
import com.postku.app.fragment.diskon.tax.TaxFragment;
import com.postku.app.helpers.ClickInterface;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.GetCustomerResponseJson;
import com.postku.app.json.GetPromoResponseJson;
import com.postku.app.json.GetServiceAddResponse;
import com.postku.app.json.GetServiceResponseJson;
import com.postku.app.json.GetTaxResponseJson;
import com.postku.app.json.GetTipeOrderResponse;
import com.postku.app.models.ServiceAdd;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class SelectAddFragment extends DialogFragment {
    private Context context;
    private SessionManager sessionManager;
    private SelectAdapter adapter;
    private RecyclerView recyclerView;
    private ClickInterface clickInterface;
    private ImageView backButton;
    private TextView caption;
    private Button btnDelete, btnSelect;
    private String title;
    private List<ServiceAdd> dataList = new ArrayList<>();
    private Realm realm;
    public SelectAddFragment() {
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
        View view = inflater.inflate(R.layout.fragment_select_add, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);

        recyclerView = view.findViewById(R.id.rec_table);
        backButton = view.findViewById(R.id.back_button);
        caption = view.findViewById(R.id.text_caption);
        btnDelete = view.findViewById(R.id.button6);
        btnSelect = view.findViewById(R.id.button8);

        title = getArguments().getString(Constants.NAMA);
        caption.setText(title);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.DISKON)){
            dataDiskon();
        }else if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.CUSTOMER)) {
            dataCustomer();
        }else if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.TIPE_ORDER)) {
            dataTipeOrder();
        }else if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.LABEL_ORDER)) {
            dataLabelOrder();
        }else if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.PAJAK)) {
            dataPajak();
        }else if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.SERVICE_CHARGE)){
            dataService();
        }

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.SERVICE_CHARGE)){
                    if(adapter != null){
                        List<Integer> serviceFee = adapter.getSelectedItems();
                        sessionManager.saveServiceFee(serviceFee, "servicefee");
                        dismiss();
                    }
                }else {
                    if(adapter != null){
                        UpdateText updateText = (UpdateText) getActivity();
                        updateText.updateResult(getArguments().getString(Constants.METHOD),
                                adapter.getSelectedItem(), adapter.getSelectedName());
                        dismiss();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void dataDiskon(){
        dataList.clear();
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getPromo(sessionManager.getIdToko()).enqueue(new Callback<GetPromoResponseJson>() {
            @Override
            public void onResponse(Call<GetPromoResponseJson> call, Response<GetPromoResponseJson> response) {

                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        if(response.body().getPromoList().size() > 0){
                            for(int i=0;i < response.body().getPromoList().size();i++){
                                final ServiceAdd serviceAdd = new ServiceAdd();
                                serviceAdd.setId(response.body().getPromoList().get(i).getId());
                                serviceAdd.setNama(response.body().getPromoList().get(i).getNama());
                                dataList.add(serviceAdd);
                            }
                            adapter = new SelectAdapter(context, dataList,R.layout.item_select,
                                    clickInterface, false);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }else{

                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetPromoResponseJson> call, Throwable t) {

                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void dataCustomer(){
        dataList.clear();
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getCustomer(sessionManager.getIdToko()).enqueue(new Callback<GetCustomerResponseJson>() {
            @Override
            public void onResponse(Call<GetCustomerResponseJson> call, Response<GetCustomerResponseJson> response) {
//                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        if(response.body().getCustomerList().size() > 0){
                            for(int i=0;i < response.body().getCustomerList().size();i++){
                                final ServiceAdd serviceAdd = new ServiceAdd();
                                serviceAdd.setId(response.body().getCustomerList().get(i).getId());
                                serviceAdd.setNama(response.body().getCustomerList().get(i).getNama());
                                dataList.add(serviceAdd);
                            }
                            adapter = new SelectAdapter(context, dataList,R.layout.item_select,
                                    clickInterface, false);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }else{
//                        lempty.setVisibility(View.VISIBLE);
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetCustomerResponseJson> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void dataTipeOrder(){
        dataList.clear();
        recyclerView.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.tipeorder().enqueue(new Callback<GetTipeOrderResponse>() {
            @Override
            public void onResponse(Call<GetTipeOrderResponse> call, Response<GetTipeOrderResponse> response) {
//                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getDataList().size() > 0){
                            for(int i=0;i < response.body().getDataList().size();i++){
                                final ServiceAdd serviceAdd = new ServiceAdd();
                                serviceAdd.setId(response.body().getDataList().get(i).getId());
                                serviceAdd.setNama(response.body().getDataList().get(i).getNama());
                                dataList.add(serviceAdd);
                            }
                            adapter = new SelectAdapter(context, dataList,R.layout.item_select,
                                    clickInterface, false);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }else{
//                        lempty.setVisibility(View.VISIBLE);
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetTipeOrderResponse> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void dataLabelOrder(){
        dataList.clear();
        recyclerView.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.labelorder().enqueue(new Callback<GetServiceAddResponse>() {
            @Override
            public void onResponse(Call<GetServiceAddResponse> call, Response<GetServiceAddResponse> response) {
//                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getDataList().size() > 0){
                            for(int i=0;i < response.body().getDataList().size();i++){
                                final ServiceAdd serviceAdd = new ServiceAdd();
                                serviceAdd.setId(response.body().getDataList().get(i).getId());
                                serviceAdd.setNama(response.body().getDataList().get(i).getNama());
                                dataList.add(serviceAdd);
                            }
                            adapter = new SelectAdapter(context, dataList,R.layout.item_select,
                                    clickInterface, false);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }else{
//                        lempty.setVisibility(View.VISIBLE);
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetServiceAddResponse> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void dataPajak(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getPajak(sessionManager.getIdToko()).enqueue(new Callback<GetTaxResponseJson>() {
            @Override
            public void onResponse(Call<GetTaxResponseJson> call, Response<GetTaxResponseJson> response) {
//                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        if(response.body().getPajakList().size() > 0){
                            for(int i=0;i < response.body().getPajakList().size();i++){
                                final ServiceAdd serviceAdd = new ServiceAdd();
                                serviceAdd.setId(response.body().getPajakList().get(i).getId());
                                serviceAdd.setNama(response.body().getPajakList().get(i).getNama());
                                dataList.add(serviceAdd);
                            }
                            adapter = new SelectAdapter(context, dataList,R.layout.item_select,
                                    clickInterface, false);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }else {
//                            lempty.setVisibility(View.VISIBLE);
                        }
                    }else{
//                        lempty.setVisibility(View.VISIBLE);
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetTaxResponseJson> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void dataService() {
        dataList.clear();
        recyclerView.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getServiceFee(sessionManager.getIdToko()).enqueue(new Callback<GetServiceResponseJson>() {
            @Override
            public void onResponse(Call<GetServiceResponseJson> call, Response<GetServiceResponseJson> response) {
//                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        if(response.body().getServiceFees().size() > 0){
                            for(int i=0;i < response.body().getServiceFees().size();i++){
                                final ServiceAdd serviceAdd = new ServiceAdd();
                                serviceAdd.setId(response.body().getServiceFees().get(i).getId());
                                serviceAdd.setNama(response.body().getServiceFees().get(i).getNama());
                                dataList.add(serviceAdd);
                            }
                            adapter = new SelectAdapter(context, dataList,R.layout.item_select_service,
                                    clickInterface, true);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }else{
//                        lempty.setVisibility(View.VISIBLE);
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetServiceResponseJson> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public interface UpdateText{
        void updateResult(String metode, int id, String nama);
    }
}