package com.jinkan.www.lsbdemo.view;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jinkan.www.lsbdemo.R;
import com.jinkan.www.lsbdemo.model.repository.http.bean.PetsBean;

import kotlin.jvm.functions.Function0;

/**
 * Created by Sampson on 2019-05-07.
 * FastAndroid
 */
public class PetsAdapter extends BasePagedListAdapter<PetsBean> {
    //    private List<PetsBean> list;
    private OnItemClick<PetsBean> onItemClick;

    public PetsAdapter(Function0 retryCallback) {
        super(DIFF_CALLBACK, retryCallback);
    }


    public void setOnItemClick(OnItemClick<PetsBean> onItemClick) {
        this.onItemClick = onItemClick;
    }


    @NonNull
    @Override
    protected RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        return PetsViewHolder.create(parent);
    }


    @Override
    protected void viewHolderBind(RecyclerView.ViewHolder holder, int position) {
        ((PetsViewHolder) holder).bind(getItem(position), onItemClick);
    }


    @Override
    protected int giveItemViewType(int position) {
        return R.layout.item_pets;
    }

    /**
     * 后台线程DiffUtil类回调： 计算新的List和原来的List的差距
     */
    private static final DiffUtil.ItemCallback<PetsBean> DIFF_CALLBACK = new DiffUtil.ItemCallback<PetsBean>() {
        @Override
        public boolean areItemsTheSame(@NonNull PetsBean oldItem, @NonNull PetsBean newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull PetsBean oldItem, @NonNull PetsBean newItem) {
            return oldItem.getId() == newItem.getId()
                    && oldItem.getName().equals(newItem.getName());

        }
    };
}
