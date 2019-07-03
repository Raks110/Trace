package com.rakshitgl.trace.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakshitgl.trace.MainActivity;
import com.rakshitgl.trace.R;
import com.rakshitgl.trace.activities.WalletActivity;
import com.rakshitgl.trace.models.MonetaryUnit;
import com.rakshitgl.trace.models.TextMessageModel;
import com.rakshitgl.trace.models.User;
import com.rakshitgl.trace.models.UserListMonetary;

import java.util.ArrayList;
import java.util.List;

public class WalletFragment extends Fragment {

    private View view;
    private int index;

    public WalletFragment() {
        // Required empty public constructor
    }

    public static WalletFragment newInstance() {

        return new WalletFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wallet, container, false);

        view.findViewById(R.id.walletShow).setVisibility(View.GONE);
        view.findViewById(R.id.emptyWallet).setVisibility(View.GONE);
        view.findViewById(R.id.loadingwalletFragment).setVisibility(View.VISIBLE);

        getWalletList();
        return view;
    }

    private void getWalletList(){

        final String oUser = MainActivity.user.getEmail().substring(0, MainActivity.user.getEmail().lastIndexOf("@gmail.com"));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(oUser);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChildren()){

                    view.findViewById(R.id.loadingwalletFragment).setVisibility(View.GONE);
                    view.findViewById(R.id.emptyWallet).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.walletShow).setVisibility(View.GONE);
                }
                else {

                    view.findViewById(R.id.emptyWallet).setVisibility(View.GONE);

                    final List<Integer> money = new ArrayList<>();
                    final List<String> currencies = new ArrayList<>();

                    final List<UserListMonetary> list = new ArrayList<>();
                    index = 0;

                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

                        ref.child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot5) {

                                User userInner = dataSnapshot5.getValue(User.class);

                                UserListMonetary user = new UserListMonetary();
                                user.setDisplayName(userInner.getDisplayName());
                                user.setEmail(userInner.getEmail());
                                user.setPhotoUrl(userInner.getPhotoURL());
                                user.setMaxMoney(0);
                                user.setList(new ArrayList<MonetaryUnit>());


                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                    TextMessageModel model = snapshot1.getValue(TextMessageModel.class);
                                    if(model.getMoneyStatus()){

                                        boolean notFoundFlag = true;

                                        for(int i=0; i<user.getList().size(); i++){
                                            if(user.getList().get(i).getCurrency().equals(model.getCurrency())){
                                                notFoundFlag = false;

                                                if(model.getSenderEmail().equals(oUser))
                                                    user.getList().get(i).setMoney( user.getList().get(i).getMoney() + model.getMoney() );
                                                else
                                                    user.getList().get(i).setMoney( user.getList().get(i).getMoney() - model.getMoney());
                                            }
                                        }

                                        if(notFoundFlag){
                                            MonetaryUnit unit = new MonetaryUnit();

                                            if(model.getSenderEmail().equals(oUser))
                                                unit.setMoney(model.getMoney());
                                            else
                                                unit.setMoney(-model.getMoney());

                                            unit.setCurrency(model.getCurrency());

                                            user.getList().add(unit);
                                        }

                                        if(model.getSenderEmail().equals(oUser)) {
                                            money.add(model.getMoney());
                                            currencies.add(model.getCurrency());
                                        }
                                        else {
                                            money.add(-model.getMoney());
                                            currencies.add(model.getCurrency());
                                        }
                                    }
                                }

                                long maxMoney = 0;

                                user.setMaxMoney(maxMoney);
                                list.add(user);

                                if(index == dataSnapshot.getChildrenCount()-1) {

                                    int moneyFin = 0;
                                    boolean sameCurr = true;
                                    String currency = currencies.get(0);

                                    for (int i = 0; i < money.size(); i++) {
                                        moneyFin += money.get(i);
                                        if (!currency.equals(currencies.get(i))) {
                                            sameCurr = false;
                                        }
                                    }

                                    if (moneyFin < 0) {

                                        ((TextView) view.findViewById(R.id.walletShowMsgHeader)).setText(R.string.wallet_text_own_header);
                                        ((TextView) view.findViewById(R.id.walletShowMsgFooter)).setText(R.string.wallet_text_own_footer);
                                        ((TextView) view.findViewById(R.id.walletShowMsgFooterUniversal)).setText(R.string.wallet_footer);
                                        ((TextView) view.findViewById(R.id.walletShowMsgMidNum)).setText(String.format(getResources().getString(R.string.wallet_text_mid_num), -moneyFin));
                                        if (sameCurr) {

                                            ((TextView) view.findViewById(R.id.walletShowMsgMidCurr)).setText(String.format(getResources().getString(R.string.wallet_text_mid_curr), currency));
                                        } else {

                                            ((TextView) view.findViewById(R.id.walletShowMsgMidCurr)).setText(String.format(getResources().getString(R.string.wallet_text_mid_curr), "bucks"));
                                        }
                                    } else {

                                        ((TextView) view.findViewById(R.id.walletShowMsgHeader)).setText(R.string.wallet_text_others_header);
                                        ((TextView) view.findViewById(R.id.walletShowMsgFooter)).setText(R.string.wallet_text_others_footer);
                                        ((TextView) view.findViewById(R.id.walletShowMsgFooterUniversal)).setText(R.string.wallet_footer);
                                        ((TextView) view.findViewById(R.id.walletShowMsgMidNum)).setText(String.format(getResources().getString(R.string.wallet_text_mid_num), moneyFin));
                                        if (sameCurr) {

                                            ((TextView) view.findViewById(R.id.walletShowMsgMidCurr)).setText(String.format(getResources().getString(R.string.wallet_text_mid_curr), currency));
                                        } else {

                                            ((TextView) view.findViewById(R.id.walletShowMsgMidCurr)).setText(String.format(getResources().getString(R.string.wallet_text_mid_curr), "bucks"));
                                        }
                                    }

                                    view.findViewById(R.id.loadingwalletFragment).setVisibility(View.GONE);
                                    view.findViewById(R.id.walletShow).setVisibility(View.VISIBLE);

                                    view.findViewById(R.id.walletShow).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Intent intent = new Intent(getContext(), WalletActivity.class);
                                            WalletActivity.list = list;
                                            startActivity(intent);
                                        }
                                    });
                                }

                                index ++;

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                view.findViewById(R.id.loadingwalletFragment).setVisibility(View.GONE);
                                view.findViewById(R.id.emptyWallet).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.walletShow).setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                view.findViewById(R.id.loadingwalletFragment).setVisibility(View.GONE);
                view.findViewById(R.id.emptyWallet).setVisibility(View.VISIBLE);
                view.findViewById(R.id.walletShow).setVisibility(View.GONE);
            }
        });
    }
}