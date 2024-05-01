package com.example.payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactionList;
    private Context context;

    public TransactionAdapter(List<Transaction> transactionList, Context context) {
        this.transactionList = transactionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        // Bind transaction data to views in the ViewHolder
        holder.amountTextView.setText("₹" + transaction.getAmount());
       // holder.timestemp.setText((int) transaction.getTimestamp());
        Glide.with(context)
                .load(transaction.getReceiverProfileImg())
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(holder.user_profile);
        if(transaction.getFieldName() == "senderUid") {
            holder.receiverUidTextView.setText(transaction.getSenderUid());
            Glide.with(context)
                    .load(transaction.getReceiverProfileImg())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(holder.user_profile);
        }
        else {
            holder.receiverUidTextView.setText(transaction.getReceiverUid());
            Glide.with(context)
                    .load(transaction.getSenderProfileImg())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(holder.user_profile);
        }

        // You can add more views and bind other transaction data here if needed

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amountTextView;
        TextView receiverUidTextView,timestemp;
        ImageView user_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            receiverUidTextView = itemView.findViewById(R.id.receiverUidTextView);
           /// timestemp = itemView.findViewById(R.id.timestemp);
            user_profile = itemView.findViewById(R.id.userPhoto);
        }
    }

}
