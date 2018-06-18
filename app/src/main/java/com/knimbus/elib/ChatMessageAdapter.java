package com.knimbus.elib;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder> {

    private List<ChatMessage> messageList;
    private DateFormat df = new SimpleDateFormat("EEE dd/MM hh:mm", Locale.getDefault());
    private String TAG = "Adapter";

    ChatMessageAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_user_message, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_other_message, parent, false);
            return new MyViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_user_message, parent, false);
            return new MyViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatMessage msg = messageList.get(position);
        holder.tvText.setText(msg.getText());
        Log.d(TAG, "onBindViewHolder: text = " + msg.getText());
        holder.tvTime.setText(df.format(new Date(msg.getTimestamp())));
        Log.d(TAG, "onBindViewHolder: date = " + msg.getTimestamp());
        holder.tvName.setText(msg.getName());

    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvText, tvName, tvTime;

        MyViewHolder(View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_message_text);
            tvName = itemView.findViewById(R.id.tv_message_name);
            tvTime = itemView.findViewById(R.id.tv_message_time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getTypeId();
    }
}
