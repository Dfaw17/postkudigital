package com.postku.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postku.app.R;
import com.postku.app.helpers.DHelper;
import com.postku.app.models.HistoryStock;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryStockAdapter extends RecyclerView.Adapter<HistoryStockAdapter.VH> {
    private Context context;
    private List<HistoryStock> historyStockList;

    public HistoryStockAdapter(Context context, List<HistoryStock> historyStockList){
        this.context = context;
        this.historyStockList = historyStockList;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_history, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final HistoryStock hs = historyStockList.get(position);
        holder.label.setText(DHelper.strToSimpleDate(hs.getCreatedAt()));
        if(hs.getType() == 1){
            holder.tipe.setText("Penambahan");
            holder.tipe.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_green));
        }else if(hs.getType() == 2){
            holder.tipe.setText("Pengurangan");
            holder.tipe.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_red));
        }else if(hs.getType() == 3){
            holder.tipe.setText("Reset Stock");
            holder.tipe.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_yellow));
        }else if(hs.getType() == 4){
            holder.tipe.setText("Penjualan");
            holder.tipe.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_blue));
        }
        holder.qty.setText("" + hs.getAdjustment());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return historyStockList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private LinearLayout item;
        private TextView label,tipe, qty;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            label = view.findViewById(R.id.text_nama);
            tipe = view.findViewById(R.id.text_tipe);
            qty = view.findViewById(R.id.text_quantity);
        }
    }
}
