package com.postku.app.fragment.product.stok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.postku.app.R;
import com.postku.app.adapter.HistoryStockAdapter;
import com.postku.app.adapter.StockAdapter;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.StockResponseJson;
import com.postku.app.json.StockTrxResponse;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class DetailStockActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private TextView caption, nama, currentStock;
    private ImageView backButton;
    private RecyclerView recyclerView;
    private HistoryStockAdapter adapter;
    private LinearLayout ladd;
    private int id;
    String[] listCategory;
    int typeAdjust = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stock);
        context = this;
        sessionManager = new SessionManager(context);
        caption = findViewById(R.id.text_caption);
        nama = findViewById(R.id.text_nama_menu);
        currentStock = findViewById(R.id.text_cur_stock);
        backButton = findViewById(R.id.back_button);
        recyclerView = findViewById(R.id.rec_history);
        ladd = findViewById(R.id.ladd);

        caption.setText("Detail Stock");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        id = getIntent().getIntExtra(Constants.ID, 0);
        nama.setText(getIntent().getStringExtra(Constants.NAMA));
        currentStock.setText("" + getIntent().getIntExtra(Constants.METHOD, 0));
        getData(id);

        ladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd();
            }
        });

    }

    private void getData(int idMenu){
        recyclerView.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.historyStock(String.valueOf(idMenu)).enqueue(new Callback<StockTrxResponse>() {
            @Override
            public void onResponse(Call<StockTrxResponse> call, Response<StockTrxResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getHistoryStockList().size() > 0){
                            adapter = new HistoryStockAdapter(context, response.body().getHistoryStockList());
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<StockTrxResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void showDialogAdd(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_stock, null);
        builder.setView(dialogView);

        final TextView title = dialogView.findViewById(R.id.title);
        final AutoCompleteTextView selectKategori = dialogView.findViewById(R.id.kategori);
        final EditText edtStock = dialogView.findViewById(R.id.edittext);
        final EditText edtNote = dialogView.findViewById(R.id.edittext2);
        final Button submit = dialogView.findViewById(R.id.btn_submit);

        listCategory = getResources().getStringArray(R.array.trx_stock);
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listCategory);
        selectKategori.setAdapter(catAdapter);
        selectKategori.setSelection(0);
        selectKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectKategori.showDropDown();
            }
        });

        selectKategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdjust = position;
            }
        });


        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "pos:" + typeAdjust);
                alertDialog.dismiss();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}