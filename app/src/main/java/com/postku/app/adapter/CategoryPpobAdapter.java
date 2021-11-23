package com.postku.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.postku.app.R;
import com.postku.app.actvity.ppob.PpobKategoriActivity;
import com.postku.app.actvity.ppob.PpobProductActivity;
import com.postku.app.helpers.Constants;
import com.postku.app.models.Channel;
import com.postku.app.models.KategoriPpob;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryPpobAdapter extends RecyclerView.Adapter<CategoryPpobAdapter.VH> {
    private Context context;
    private List<KategoriPpob> kategoriPpobList;
    private int rowLayout;
    public CategoryPpobAdapter(Context context, List<KategoriPpob> kategoriPpobList, int rowLayout){
        this.context = context;
        this.kategoriPpobList = kategoriPpobList;
        this.rowLayout = rowLayout;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final KategoriPpob ppob = kategoriPpobList.get(position);
        Glide.with(context)
                .load(ppob.getCatImage())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.logo);
        holder.nama.setText(ppob.getCatName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PpobProductActivity.class);
                intent.putExtra(Constants.NAMA, ppob.getCatKey());
                context.startActivity(intent);
            }
        });

        if(ppob.getId() == 0){
            holder.bind(ppob);
        }
    }

    @Override
    public int getItemCount() {
        return kategoriPpobList.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        private LinearLayout item;
        private TextView nama;
        private ImageView logo;
        public VH(View itemView){
            super(itemView);
            item = itemView.findViewById(R.id.item);
            nama = itemView.findViewById(R.id.text_nama);
            logo = itemView.findViewById(R.id.img_logo);
        }

        public void bind(final KategoriPpob menu){
            if(menu.getId() == 0){
                Glide.with(context)
                        .load(menu.getCatImage())
                        .placeholder(R.drawable.img_menu)
                        .into(logo);

                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PpobKategoriActivity.class);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }
}
