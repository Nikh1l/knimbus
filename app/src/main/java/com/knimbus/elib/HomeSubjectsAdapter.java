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


public class HomeSubjectsAdapter extends RecyclerView.Adapter<HomeSubjectsAdapter.MyViewHolder>{

    List<HomeSubjects> subjects;

    HomeSubjectsAdapter(List<HomeSubjects> subjects){
        this.subjects = subjects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_subjects,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvSubInside.setText(subjects.get(position).getSubject());
        holder.tvSubOutside.setText(subjects.get(position).getSubject());
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvSubInside, tvSubOutside;
        private CardView subCard;
        Context mContext;

        MyViewHolder(View itemView) {
            super(itemView);

            tvSubInside = itemView.findViewById(R.id.tv_sub_inside);
            tvSubOutside = itemView.findViewById(R.id.tv_sub_outside);
            subCard = itemView.findViewById(R.id.cv_subjects_card);
            mContext = itemView.getContext();

            subCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,SubjectActivity.class);
                    intent.putExtra("subName", subjects.get(getAdapterPosition()).getSubject());
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
