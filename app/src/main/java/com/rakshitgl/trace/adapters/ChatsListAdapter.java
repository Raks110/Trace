package com.rakshitgl.trace.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rakshitgl.trace.R;
import com.rakshitgl.trace.models.ChatsListModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ViewHolder> {

    private List<ChatsListModel> list;

    public ChatsListAdapter(List<ChatsListModel> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_chats, parent, false);

        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.lastMessage.setText(list.get(position).getLastMessage());
        holder.userPhotoURL.setText(list.get(position).getPhotoURL());
        holder.userName.setText(list.get(position).getDisplayName());
        holder.email.setText(list.get(position).getEmail());

        if(list.get(position).getLastMessageReceived().getTime() > list.get(position).getLastSeen()){
            holder.llParent.setBackgroundColor(Color.parseColor("#e6f2ff"));
        }
        else{
            holder.llParent.setBackgroundColor(Color.WHITE);
        }

        Picasso.with(holder.itemView.getContext()).load(list.get(position).getPhotoURL()).into(holder.userPhoto);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView userName;
        private TextView lastMessage;
        private ImageView userPhoto;
        private TextView userPhotoURL;
        private TextView email;
        private LinearLayout llParent;

        public ViewHolder(@NonNull LinearLayout ll) {
            super(ll);

            llParent = ll;
            userName = ll.findViewById(R.id.userNameChats);
            lastMessage = ll.findViewById(R.id.userLastMessageChats);
            userPhoto = ll.findViewById(R.id.userPhotoChats);
            userPhotoURL = ll.findViewById(R.id.hiddenPhotoUrlChats);
            email = ll.findViewById(R.id.hiddenEmailChats);
        }
    }
}
