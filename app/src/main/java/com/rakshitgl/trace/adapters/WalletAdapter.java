package com.rakshitgl.trace.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.rakshitgl.trace.R;
import com.rakshitgl.trace.activities.ChatActivity;
import com.rakshitgl.trace.models.MonetaryUnit;
import com.rakshitgl.trace.models.User;
import com.rakshitgl.trace.models.UserListMonetary;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder>{

    List<UserListMonetary> list;

    public WalletAdapter(List<UserListMonetary> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MaterialCardView ll = (MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_wallet, parent, false);
        return new ViewHolder(ll);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.displayname.setText(list.get(position).getDisplayName());
        holder.email.setText(list.get(position).getEmail());

        Picasso.with(holder.itemView.getContext()).load(list.get(position).getPhotoUrl()).into(holder.userImage);

        holder.photoUrl.setText(list.get(position).getPhotoUrl());

        for(int i=0;i<list.get(position).getList().size(); i++){

            LinearLayout cardView = (LinearLayout) LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.rv_wallet_currency, (ViewGroup) holder.itemView, false);

            MonetaryUnit unit = list.get(position).getList().get(i);

            ((TextView) cardView.findViewById(R.id.currencyReport)).setText(unit.getCurrency());

            if(unit.getMoney() < 0){

                ((TextView) cardView.findViewById(R.id.oweReport)).setText("You should pay ");
                ((TextView) cardView.findViewById(R.id.moneyReport)).setText(Long.toString(-unit.getMoney()));
                ((TextView) cardView.findViewById(R.id.moneyReport)).setTextColor(Color.parseColor("#b30000"));
                ((TextView) cardView.findViewById(R.id.currencyReport)).setTextColor(Color.parseColor("#b30000"));
            }
            else{

                ((TextView) cardView.findViewById(R.id.oweReport)).setText("You should receive ");
                ((TextView) cardView.findViewById(R.id.moneyReport)).setText(Long.toString(unit.getMoney()));
                ((TextView) cardView.findViewById(R.id.moneyReport)).setTextColor(Color.parseColor("#006600"));
                ((TextView) cardView.findViewById(R.id.currencyReport)).setTextColor(Color.parseColor("#006600"));
            }

            holder.recyclerView.addView(cardView);
        }

        holder.mcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User();
                user.setDisplayName(list.get(position).getDisplayName());
                user.setEmail(list.get(position).getEmail());
                user.setPhotoURL(list.get(position).getPhotoUrl());

                ChatActivity.user = user;
                ChatActivity.loadedAlready = false;

                Intent intent = new Intent(holder.itemView.getContext(), ChatActivity.class);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView displayname;
        private TextView email;
        private ImageView userImage;
        private TextView photoUrl;
        private LinearLayout recyclerView;
        private MaterialCardView mcv;

        public ViewHolder(@NonNull MaterialCardView ll) {
            super(ll);

            mcv = ll;
            displayname = ll.findViewById(R.id.userNameWalletRv);
            email = ll.findViewById(R.id.userEmailWalletRv);
            userImage = ll.findViewById(R.id.userPhotoWalletRv);
            photoUrl = ll.findViewById(R.id.hiddenPhotoUrlWalletRv);
            recyclerView = ll.findViewById(R.id.walletCurrencyRecycler);
        }
    }
}
