package com.rakshitgl.trace.adapters.chatholders;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rakshitgl.trace.R;

public class OwnMessageHolder extends RecyclerView.ViewHolder {

    public TextView message;
    public TextView timestamp;

    public OwnMessageHolder(@NonNull LinearLayout ll) {
        super(ll);

        message = ll.findViewById(R.id.ownMessage);
        timestamp = ll.findViewById(R.id.ownMessageTimestamp);
    }
}
