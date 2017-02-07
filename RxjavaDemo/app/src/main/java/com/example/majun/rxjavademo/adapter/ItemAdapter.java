package com.example.majun.rxjavademo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.majun.rxjavademo.R;
import com.example.majun.rxjavademo.model.Item;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by majun on 17/2/3.
 */
public class ItemAdapter extends RecyclerView.Adapter {
    List<Item> mList;
    Context mContext;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        Item item = mList.get(position);
        itemHolder.description.setText(item.description);
        Glide.with(mContext).load(item.image_url).into(itemHolder.imageView);
    }

    public void setmList(List<Item> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        } else {
            return mList.size();
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_image)
        ImageView imageView;
        @Bind(R.id.item_description)
        TextView description;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
