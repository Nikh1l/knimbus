package com.knimbus.elib;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {

    private List<HashMap<String ,String>> searchResultList;

    SearchResultAdapter(List<HashMap<String, String >> searchResultList){
        this.searchResultList = searchResultList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final HashMap<String, String> res = searchResultList.get(position);

        holder.tvResultTitle.setText(res.get("title"));
        holder.tvResultAuthor.setText(res.get("author"));

    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvResultTitle, tvResultAuthor;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvResultAuthor = itemView.findViewById(R.id.tv_result_author);
            tvResultTitle = itemView.findViewById(R.id.tv_result_title);
        }
    }
}
