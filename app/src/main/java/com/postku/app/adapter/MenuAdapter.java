package com.postku.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.postku.app.R;
import com.postku.app.fragment.product.menu.ManageMenuActivity;
import com.postku.app.helpers.Constants;
import com.postku.app.helpers.DHelper;
import com.postku.app.models.Menus;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.VH> implements Filterable {
    private Context context;
    private List<Menus> menusList;
    private List<Menus> filteredList;

    public MenuAdapter(Context context, List<Menus> menusList){
        this.context = context;
        this.menusList = menusList;
        this.filteredList = menusList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final Menus menus = filteredList.get(position);
        Glide.with(context)
                .load(menus.getImage())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);
        holder.nama.setText(menus.getNama());
        holder.harga.setText("Rp" + DHelper.toformatRupiah(String.valueOf(menus.getHarga())));
        if(menus.getKategori() != null){
            holder.kategori.setText(menus.getKategori());
        }else {
            holder.kategori.setText("-");
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ManageMenuActivity.class);
                intent.putExtra(Constants.ID, menus.getId());
                intent.putExtra(Constants.ID_KATEGORI, menus.getKategori());
                intent.putExtra(Constants.METHOD, Constants.EDIT);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = menusList;
                }else {
                    List<Menus> list = new ArrayList<>();
                    for(Menus row : menusList){
                        if(row.getNama().toLowerCase().contains(nama.toLowerCase())){
                            list.add(row);
                        }
                    }
                    filteredList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Menus>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView nama, harga, kategori;
        private ImageView imageView;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            nama = view.findViewById(R.id.text_nama_produk);
            harga = view.findViewById(R.id.text_harga);
            kategori = view.findViewById(R.id.text_kategori);
            imageView = view.findViewById(R.id.img_produk);
        }
    }
}
