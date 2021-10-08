package com.postku.app.fragment.pos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.postku.app.R;
import com.postku.app.adapter.MejaAdapter;
import com.postku.app.adapter.TableSelectAdapter;
import com.postku.app.fragment.ReferenceFragment;
import com.postku.app.fragment.meja.TableFragment;
import com.postku.app.helpers.ClickInterface;
import com.postku.app.helpers.DHelper;
import com.postku.app.json.GetTableResponse;
import com.postku.app.models.Meja;
import com.postku.app.services.ServiceGenerator;
import com.postku.app.services.api.UserService;
import com.postku.app.utils.Log;
import com.postku.app.utils.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postku.app.helpers.Constants.TAG;

public class SelectTable extends DialogFragment {
    private Context context;
    private SessionManager sessionManager;
    private TableSelectAdapter adapter;
    private RecyclerView recyclerView;
    private ClickInterface clickInterface;
    private ImageView backButton;
    private TextView caption;
    private Button select;
    private List<Meja> mejaList = new ArrayList<>();
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_select_table, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        recyclerView = view.findViewById(R.id.rec_table);
        backButton = view.findViewById(R.id.back_button);
        caption = view.findViewById(R.id.text_caption);
        select = view.findViewById(R.id.button6);

        caption.setText("Daftar meja");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter != null){
                    if(adapter.isBooked()){
                        DHelper.pesan(context, "Meja sudah dibooking");
                        return;
                    }else {
                        UpdateText updateText = (UpdateText) getParentFragment();
                        updateText.updateResult(String.valueOf(adapter.getSelectedItem()), adapter.getName());
                        dismiss();
                    }
                }
            }
        });

        return view;
    }

    private void getData() {
//        progressBar.setVisibility(View.VISIBLE);
//        lempty.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getTable(sessionManager.getIdToko()).enqueue(new Callback<GetTableResponse>() {
            @Override
            public void onResponse(Call<GetTableResponse> call, Response<GetTableResponse> response) {
//                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        if(response.body().getMejaList().size() > 0){
                            adapter = new TableSelectAdapter(context, response.body().getMejaList(), clickInterface);
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
            public void onFailure(Call<GetTableResponse> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public interface UpdateText{
        void updateResult(String id, String nama);
    }
}
