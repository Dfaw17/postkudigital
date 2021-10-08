package com.postku.app.actvity.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.postku.app.R;
import com.postku.app.utils.SessionManager;

public class TopupPendingActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private Button close;
    private ImageView backButton;
    private TextView caption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_pending);
        context = this;
        sessionManager = new SessionManager(context);
        close = findViewById(R.id.btn_activated);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);

        caption.setText("Topup Saldo");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}