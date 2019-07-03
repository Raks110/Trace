package com.rakshitgl.trace.adapters.chatholders;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rakshitgl.trace.R;

public class OthersMessageMoneyHolder extends RecyclerView.ViewHolder {


    public TextView message;
    public TextView timestamp;
    public TextView money;
    public TextView currency;
    public TextView belongs;

    public OthersMessageMoneyHolder(@NonNull LinearLayout ll) {
        super(ll);

        message = ll.findViewById(R.id.othersMessageMonetary);
        timestamp = ll.findViewById(R.id.othersMessageTimestampMonetary);
        money = ll.findViewById(R.id.bucksOthers);
        currency = ll.findViewById(R.id.currencyOthers);
        belongs = ll.findViewById(R.id.currencyBelongs);
    }
}
