package com.rakshitgl.trace.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.rakshitgl.trace.MainActivity;
import com.rakshitgl.trace.R;
import com.rakshitgl.trace.adapters.chatholders.OthersMessageHolder;
import com.rakshitgl.trace.adapters.chatholders.OthersMessageMoneyHolder;
import com.rakshitgl.trace.adapters.chatholders.OwnMessageHolder;
import com.rakshitgl.trace.adapters.chatholders.OwnMessageMoneyHolder;
import com.rakshitgl.trace.models.TextMessageModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TextMessageModel> list;
    private static final int TYPE_OWN = 0;
    private static final int TYPE_OTHER = 1;
    private static final int TYPE_OWN_MONETARY = 2;
    private static final int TYPE_OTHER_MONETARY = 3;

    public ChatAdapter(List<TextMessageModel> list){

        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == TYPE_OWN){

            LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_own_message, parent, false);

            return new OwnMessageHolder(ll);
        }
        else if(viewType == TYPE_OWN_MONETARY){

            LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_own_message_monetary, parent, false);

            return new OwnMessageMoneyHolder(ll);
        }
        else if(viewType == TYPE_OTHER_MONETARY){

            LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_others_message_monetary, parent, false);

            return new OthersMessageMoneyHolder(ll);
        }
        else{

            LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_others_message, parent, false);

            return new OthersMessageHolder(ll);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        TextMessageModel text = list.get(position);

        if(holder instanceof OwnMessageHolder){

            ((OwnMessageHolder) holder).message.setText(text.getText());

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM hh:mm");
            String dateShow = formatter.format(text.getTimestamp());
            ((OwnMessageHolder) holder).timestamp.setText(dateShow);
        }
        else if(holder instanceof OwnMessageMoneyHolder){

            if(text.getMoney() >= 0){

                ((OwnMessageMoneyHolder) holder).belongs.setText("YOU OWE ME");
                ((OwnMessageMoneyHolder) holder).money.setText(Integer.toString(text.getMoney()));
            }
            else{

                ((OwnMessageMoneyHolder) holder).belongs.setText("I OWE YOU");
                ((OwnMessageMoneyHolder) holder).money.setText(Integer.toString(0 - text.getMoney()));
            }

            ((OwnMessageMoneyHolder) holder).currency.setText(text.getCurrency());
            ((OwnMessageMoneyHolder) holder).message.setText(text.getText());

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM hh:mm");
            String dateShow = formatter.format(text.getTimestamp());
            ((OwnMessageMoneyHolder) holder).timestamp.setText(dateShow);

        }
        else if(holder instanceof OthersMessageMoneyHolder){

            if(text.getMoney() >= 0){

                ((OthersMessageMoneyHolder) holder).belongs.setText("YOU OWE ME");
                ((OthersMessageMoneyHolder) holder).money.setText(Integer.toString(text.getMoney()));
            }
            else{

                ((OthersMessageMoneyHolder) holder).belongs.setText("I OWE YOU");
                ((OthersMessageMoneyHolder) holder).money.setText(Integer.toString(0 - text.getMoney()));
            }

            ((OthersMessageMoneyHolder) holder).currency.setText(text.getCurrency());
            ((OthersMessageMoneyHolder) holder).message.setText(text.getText());

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM hh:mm");
            String dateShow = formatter.format(text.getTimestamp());
            ((OthersMessageMoneyHolder) holder).timestamp.setText(dateShow);

        }
        else{

            ((OthersMessageHolder) holder).message.setText(text.getText());

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM hh:mm");
            String dateShow = formatter.format(text.getTimestamp());
            ((OthersMessageHolder) holder).timestamp.setText(dateShow);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position){

        if(list.get(position).getSenderEmail().concat("@gmail.com").equals(MainActivity.user.getEmail())){
            if(list.get(position).getMoneyStatus())
                return TYPE_OWN_MONETARY;
            else
                return TYPE_OWN;
        }
        else{
            if(list.get(position).getMoneyStatus())
                return TYPE_OTHER_MONETARY;
            else
                return TYPE_OTHER;
        }
    }
}
