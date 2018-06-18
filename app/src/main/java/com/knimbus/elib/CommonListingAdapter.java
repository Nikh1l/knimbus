package com.knimbus.elib;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CommonListingAdapter extends RecyclerView.Adapter<CommonListingAdapter.MyViewHolder> {

    private List<CommonListing> commonListings;

    CommonListingAdapter(List<CommonListing> commonListings){
        this.commonListings = commonListings;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_common_list, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTitle.setText(commonListings.get(position).getTitle());
        holder.tvAuthor.setText(commonListings.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return commonListings.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle, tvAuthor;
        private CardView cvCommonDoc;
        Context mContext;

        MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_common_title);
            tvAuthor = itemView.findViewById(R.id.tv_common_author);
            cvCommonDoc = itemView.findViewById(R.id.cv_common_card);
            mContext = itemView.getContext();

            cvCommonDoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,DetailsActivity.class);
                    intent.putExtra("title",commonListings.get(getAdapterPosition()).getTitle());
                    intent.putExtra("author",commonListings.get(getAdapterPosition()).getAuthor());
                    intent.putExtra("type",commonListings.get(getAdapterPosition()).getType());
                    intent.putExtra("url",commonListings.get(getAdapterPosition()).getUrl());
                    intent.putExtra("size",commonListings.get(getAdapterPosition()).getSize());
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
