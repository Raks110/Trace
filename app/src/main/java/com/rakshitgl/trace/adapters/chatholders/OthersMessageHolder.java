package com.rakshitgl.trace.adapters.chatholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rakshitgl.trace.R;

public class OthersMessageHolder extends RecyclerView.ViewHolder {

    public TextView timestamp;
    public TextView message;

    public OthersMessageHolder(@NonNull LinearLayout ll) {
        super(ll);

        timestamp = ll.findViewById(R.id.othersMessageTimestamp);
        message = ll.findViewById(R.id.othersMessage);
    }
}
