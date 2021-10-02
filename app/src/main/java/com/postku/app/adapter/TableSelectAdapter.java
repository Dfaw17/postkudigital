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
import com.postku.app.helpers.ClickInterface;
import com.postku.app.models.Meja;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TableSelectAdapter extends RecyclerView.Adapter<TableSelectAdapter.VH> {
    private Context context;
    private List<Meja> mejaList;
    private ClickInterface clickInterface;

    public TableSelectAdapter(Context context, List<Meja> mejaList, ClickInterface clickInterface){
        this.context = context;
        this.mejaList = mejaList;
        this.clickInterface = clickInterface;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_booking, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final Meja meja = mejaList.get(position);
        holder.nama.setText(meja.getNama());
        if(meja.isBooked()){
            holder.item.setBackground(context.getResources().getDrawable(R.drawable.bg_outline_red));
        }else {
            holder.item.setBackground(context.getResources().getDrawable(R.drawable.bg_outline_green));
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterface.onItemSelected(String.valueOf(meja.getId()), meja.getNama());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mejaList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView nama;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            nama = view.findViewById(R.id.text_meja);
        }
    }
}
