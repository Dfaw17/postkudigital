package com.postku.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postku.app.R;
import com.postku.app.helpers.DHelper;
import com.postku.app.models.ItemCart;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemCartAdapter extends RecyclerView.Adapter<ItemCartAdapter.VH> {
    private Context context;
    private List<ItemCart> itemCartList;

    public ItemCartAdapter(Context context, List<ItemCart> itemCartList){
        this.context = context;
        this.itemCartList = itemCartList;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final ItemCart itemCart = itemCartList.get(position);
        holder.nama.setText(itemCart.getMenuName().getNama());
        holder.harga.setText("Rp" + DHelper.toformatRupiah(String.valueOf(itemCart.getMenuName().getHarga())) + " x " + itemCart.getQty());
        double grandTotal = Math.round(itemCart.getGrandTotalPrice());
        holder.total.setText("Rp" + DHelper.toformatRupiah(String.valueOf(grandTotal)));

    }

    @Override
    public int getItemCount() {
        return itemCartList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView nama, harga, total;
        private ImageView edit, delete;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            nama = view.findViewById(R.id.text_nama);
            harga = view.findViewById(R.id.text_price);
            total = view.findViewById(R.id.text_total);
            edit = view.findViewById(R.id.img_edit);
            delete = view.findViewById(R.id.img_delete);
        }
    }
}
