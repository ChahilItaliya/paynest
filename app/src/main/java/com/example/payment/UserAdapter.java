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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> userList;
    private OnUserClickListener listener;
    private Context context;


    public UserAdapter(List<User> userList, OnUserClickListener listener,Context context) {
        this.userList = userList;
        this.listener = listener;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userNameTextView.setText(user.getName());
        holder.userEmailTextView.setText(user.getEmail());
        String  url = user.getPhotoUrl();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.profile) // Placeholder image while loading
                .error(R.drawable.profile)       // Error image if loading fails
                .into(holder.userPhotoImageView);


        // Load user photo here (you can use a library like Picasso or Glide)
        // Example with Picasso:
        // Picasso.get().load(user.getPhotoUrl()).into(holder.userPhotoImageView);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user.getUid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTextView;
        public TextView userEmailTextView;
        public ImageView userPhotoImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userName);
            userEmailTextView = itemView.findViewById(R.id.userEmail);
            userPhotoImageView = itemView.findViewById(R.id.userPhoto);
        }
    }

    public interface OnUserClickListener {
        void onUserClick(String uid);
    }
}
