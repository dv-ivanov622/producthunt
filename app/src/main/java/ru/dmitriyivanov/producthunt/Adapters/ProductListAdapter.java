package ru.dmitriyivanov.producthunt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.dmitriyivanov.producthunt.ProductHuntApi.Models.Post;
import ru.dmitriyivanov.producthunt.R;

/**
 * Developer - WALKER
 * Date - 09.04.2017
 * Project - ProductHunt
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private Context mContext;
    private static onProductItemClicked mOnClickListener;
    private List<Post> mPostList;

    public ProductListAdapter(Context context, List<Post> postList, ProductListAdapter.onProductItemClicked onClickListener) {
        mContext = context;
        mPostList = postList;
        mOnClickListener = onClickListener;
    }

    public interface onProductItemClicked {
        void onClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext).load(mPostList.get(position).thumb.image).into(holder.productImg);
        holder.productTitle.setText(mPostList.get(position).name);
        holder.productDescription.setText(mPostList.get(position).description);
        holder.productVote.setText(String.valueOf(mPostList.get(position).votes));
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView productTitle, productDescription, productVote;
        public ImageView productImg;
        public ViewHolder(View v) {
            super(v);
            productTitle = (TextView) v.findViewById(R.id.productTitle);
            productDescription = (TextView) v.findViewById(R.id.productDescription);
            productVote = (TextView) v.findViewById(R.id.productVote);
            productImg = (ImageView) v.findViewById(R.id.productImg);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onClick(getLayoutPosition());
        }
    }
}