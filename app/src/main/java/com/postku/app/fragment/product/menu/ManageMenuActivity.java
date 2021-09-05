package com.postku.app.fragment.product.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.postku.app.BaseApp;
import com.postku.app.R;
import com.postku.app.helpers.Constants;
import com.postku.app.models.User;
import com.postku.app.utils.SessionManager;

public class ManageMenuActivity extends AppCompatActivity {
    private Context context;
    private User user;
    private SessionManager sessionManager;
    private TextView caption;
    private ImageView back;
    private RelativeLayout rlimage;
    private EditText edtNama, edtDeskripsi, edtHarga, edtModal, edtKategori;
    private Switch aSwitch;
    private Button submit;
    private int id= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);
        context = this;
        user = BaseApp.getInstance(context).getLoginUser();
        sessionManager = new SessionManager(context);
        caption = findViewById(R.id.text_caption);
        back = findViewById(R.id.back_button);
        rlimage = findViewById(R.id.rlimage);
        edtNama = findViewById(R.id.edt_nama_produk);
        edtDeskripsi = findViewById(R.id.edt_deskripsi);
        edtHarga = findViewById(R.id.edt_harga);
        edtModal = findViewById(R.id.edt_harga_modal);
        edtKategori = findViewById(R.id.edt_kategori);
        aSwitch = findViewById(R.id.switch1);
        submit = findViewById(R.id.btn_submit);

        if(getIntent().getStringExtra(Constants.METHOD).equalsIgnoreCase(Constants.ADD)){
            caption.setText("Tambah Menu");
        }else {
            caption.setText("Edit Menu");
            id = getIntent().getIntExtra(Constants.ID, 0);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}