package com.postku.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postku.app.R;
import com.postku.app.helpers.DHelper;
import com.postku.app.models.HistoryWallet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryWalletAdapter extends RecyclerView.Adapter<HistoryWalletAdapter.VH> {
    private Context context;
    private List<HistoryWallet> historyWalletList;

    public HistoryWalletAdapter(Context context, List<HistoryWallet> historyWalletList){
        this.context = context;
        this.historyWalletList = historyWalletList;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_pay, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final HistoryWallet wallet = historyWalletList.get(position);
        if(wallet.getType() == 1){
            holder.tipe.setText("Topup");
            holder.nominal.setText("+ Rp" + DHelper.toformatRupiah(String.valueOf(wallet.getBalance())));
        }else if(wallet.getType() == 2){
            holder.tipe.setText("Pembayaran");
            holder.nominal.setText("- Rp" + DHelper.toformatRupiah(String.valueOf(wallet.getBalance())));
        }else {
            holder.tipe.setText("Refund");
            holder.nominal.setText("+ Rp" + DHelper.toformatRupiah(String.valueOf(wallet.getBalance())));
        }
        holder.tanggal.setText(DHelper.strTodatetime(wallet.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return historyWalletList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView tipe, nominal, tanggal;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            tipe = view.findViewById(R.id.text_invoice);
            nominal = view.findViewById(R.id.text_total);
            tanggal = view.findViewById(R.id.text_tanggal);
        }
    }
}
