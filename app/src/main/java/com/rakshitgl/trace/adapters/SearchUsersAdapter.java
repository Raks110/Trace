package com.rakshitgl.trace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rakshitgl.trace.R;
import com.rakshitgl.trace.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchUsersAdapter extends RecyclerView.Adapter<SearchUsersAdapter.ViewHolder>{

    private List<User> users;

    public SearchUsersAdapter(List<User> users){
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LinearLayout cl = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_search_users, parent, false);

        return new SearchUsersAdapter.ViewHolder(cl);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.displayName.setText(users.get(position).getDisplayName());
        holder.email.setText(users.get(position).getEmail());
        holder.photoUrl.setText(users.get(position).getPhotoURL());

        Picasso.with(holder.itemView.getContext()).load(users.get(position).getPhotoURL()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView displayName;
        private TextView email;
        private ImageView image;
        private TextView photoUrl;

        private ViewHolder(@NonNull LinearLayout ll) {
            super(ll);

            displayName = ll.findViewById(R.id.userName);
            email = ll.findViewById(R.id.userEmail);
            image = ll.findViewById(R.id.userPhoto);
            photoUrl = ll.findViewById(R.id.hiddenPhotoUrl);
        }
    }
}
