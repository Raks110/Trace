package com.rakshitgl.trace.adapters.chatholders;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rakshitgl.trace.R;

public class OwnMessageMoneyHolder extends RecyclerView.ViewHolder {


    public TextView message;
    public TextView timestamp;
    public TextView money;
    public TextView currency;
    public TextView belongs;

    public OwnMessageMoneyHolder(@NonNull LinearLayout ll) {
        super(ll);

        message = ll.findViewById(R.id.ownMessageMonetary);
        timestamp = ll.findViewById(R.id.ownMessageTimestampMonetary);
        money = ll.findViewById(R.id.bucksOwn);
        currency = ll.findViewById(R.id.currencyOwn);
        belongs = ll.findViewById(R.id.currencyBelongsOwn);
    }
}
