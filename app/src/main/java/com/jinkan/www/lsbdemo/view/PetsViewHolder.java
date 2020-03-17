package com.jinkan.www.lsbdemo.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jinkan.www.lsbdemo.R;
import com.jinkan.www.lsbdemo.model.repository.http.bean.PetsBean;

/**
 * Created by Sampson on 2019/3/11.
 * FastAndroid
 */
public class PetsViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView name;

    private PetsViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.icon_pets);
        name = itemView.findViewById(R.id.pets_name);

    }

    public static PetsViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pets, parent, false);
        return new PetsViewHolder(view);
    }

    void bind(PetsBean petsBean, OnItemClick<PetsBean> onSelectClick) {
        name.setText(petsBean.getName());
        Glide.with(imageView).load(petsBean.getImage()).into(imageView);
        itemView.setOnClickListener(v -> {
            if (onSelectClick != null) {
                onSelectClick.onClick(v, petsBean, getLayoutPosition());
            }
        });
    }

}
