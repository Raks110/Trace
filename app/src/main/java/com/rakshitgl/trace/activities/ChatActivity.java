package com.rakshitgl.trace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakshitgl.trace.MainActivity;
import com.rakshitgl.trace.R;
import com.rakshitgl.trace.adapters.ChatAdapter;
import com.rakshitgl.trace.models.TextMessageModel;
import com.rakshitgl.trace.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.rakshitgl.trace.MainActivity.preferences;

public class ChatActivity extends AppCompatActivity {

    public static User user;
    public static boolean loadedAlready;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(loadedAlready)
            finish();

        setContentView(R.layout.activity_chat);

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);

        if(preferences.getBoolean("SHOULD_SHOW_MESSAGE_AGAIN",true)){
            findViewById(R.id.chatsActivityNote).setVisibility(View.VISIBLE);
        }
        else{
            findViewById(R.id.chatsActivityNote).setVisibility(View.GONE);
        }

        onClickDescription();

        userName = user.getEmail().substring(0, user.getEmail().lastIndexOf("@gmail.com"));

        Log.e("Username is", userName);
        ((TextView) findViewById(R.id.userTextEmail)).setText(user.getEmail());
        ((TextView) findViewById(R.id.userTextName)).setText(user.getDisplayName());

        ((TextView) findViewById(R.id.emptyMessagesMsgPersonal)).setText(String.format(getResources().getString(R.string.no_personal_messages), user.getDisplayName()));

        checkIfChatExists();

        findViewById(R.id.sendMessageAnimation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((EditText) findViewById(R.id.messageUser)).getText().toString().length() > 0){

                    ((LottieAnimationView) v).playAnimation();
                    ((LottieAnimationView) v).setSpeed(3.0f);

                    sendMessage(((EditText) findViewById(R.id.messageUser)).getText().toString());

                    ((EditText) findViewById(R.id.messageUser)).getText().clear();
                }
            }
        });

        Picasso.with(getApplicationContext()).load(user.getPhotoURL()).into((ImageView) findViewById(R.id.userTextPhoto));
    }

    private void checkIfChatExists(){

        findViewById(R.id.loadingMessagesPersonal).setVisibility(View.VISIBLE);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(MainActivity.user.getEmail().substring(0, MainActivity.user.getEmail().lastIndexOf("@gmail.com"))).child(userName);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){

                    LottieAnimationView animationView = findViewById(R.id.emptyMessagesAnimationPersonal);

                    findViewById(R.id.emptyMessagesPersonal).setVisibility(View.VISIBLE);

                    animationView.playAnimation();
                    animationView.setSpeed(0.8f);
                    animationView.setMaxProgress(0.7f);
                }
                else{

                    findViewById(R.id.chatsPersonalRecycler).setVisibility(View.VISIBLE);
                    findViewById(R.id.emptyMessagesPersonal).setVisibility(View.GONE);

                    List<TextMessageModel> list = new ArrayList<>();

                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        list.add(dataSnapshot1.getValue(TextMessageModel.class));
                    }

                    Collections.reverse(list);

                    RecyclerView recyclerView = findViewById(R.id.chatsPersonalRecycler);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setReverseLayout(true);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    MaterialCardView mcv = findViewById(R.id.mcv_chat_header);
                    recyclerView.setPadding(0, 200 + mcv.getHeight() + 16, 0, 0);

                    ChatAdapter adapter = new ChatAdapter(list);
                    recyclerView.setAdapter(adapter);

                }
                findViewById(R.id.loadingMessagesPersonal).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String text){

        int oweOthers = 0, oweOwn = 0;
        String oweOthersCurrency = "", oweOwnCurrency = "", currencyStr = "";
        boolean moneyStatus = false;
        int moneyInt = 0;

        String textLower = text.toLowerCase();
        int index = textLower.indexOf(getResources().getString(R.string.search_tag_others));

        try {

            while (index >= 0) {
                if (Character.isLetter(textLower.charAt(index + 11)) && Character.isLetter(textLower.charAt(index + 12)) && Character.isLetter(textLower.charAt(index + 13)) && textLower.charAt(index + 14) == ' ') {

                    String currency = textLower.substring(index + 11, index + 14).toUpperCase();
                    String money = "";

                    int i = 0;

                    try {
                        while (Character.isDigit(textLower.charAt(index + 15 + i))) {
                            money = money.concat(textLower.substring(index + 15 + i, index + 16 + i));
                            i++;
                        }
                    }
                    catch(Exception e){}

                    if(!money.equals("")) {
                        oweOthers = Integer.parseInt(money);
                        oweOthersCurrency = currency;
                    }

                    break;
                } else {
                    index = textLower.indexOf(getResources().getString(R.string.search_tag_others), index + 1);
                }
            }
        }
        catch(Exception e){}

        index = textLower.indexOf(getResources().getString(R.string.search_tag_own));

        try {

            while (index >= 0) {

                if (Character.isLetter(textLower.charAt(index + 10)) && Character.isLetter(textLower.charAt(index + 11)) && Character.isLetter(textLower.charAt(index + 12)) && textLower.charAt(index + 13) == ' ') {

                    String currency = textLower.substring(index + 10, index + 13).toUpperCase();
                    String money = "";

                    int i = 0;

                    try {
                        while (Character.isDigit(textLower.charAt(index + 14 + i))) {
                            money = money.concat(textLower.substring(index + 14 + i, index + 15 + i));
                            i++;
                        }
                    }
                    catch(Exception e){}

                    if(!money.equals("")) {
                        oweOwn = Integer.parseInt(money);
                        oweOwnCurrency = currency;
                    }

                    break;
                } else {
                    index = textLower.indexOf(getResources().getString(R.string.search_tag_own), index + 1);
                }
            }
        }
        catch(Exception e){}

        if(oweOthersCurrency.equals("")){
            if(!oweOwnCurrency.equals("")){
                moneyStatus = true;
                moneyInt = moneyInt - oweOwn;
                currencyStr = oweOwnCurrency;
            }
        }
        else{
            if(oweOwnCurrency.equals("")){
                moneyStatus = true;
                moneyInt = oweOthers;
                currencyStr = oweOthersCurrency;
            }
            else{
                if(oweOthersCurrency.equals(oweOwnCurrency)) {
                    moneyStatus = true;
                    moneyInt = oweOthers - oweOwn;
                    currencyStr = oweOthersCurrency;
                }
            }
        }

        String oUser = MainActivity.user.getEmail().substring(0, MainActivity.user.getEmail().lastIndexOf("@gmail.com"));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(oUser).child(userName);
        DatabaseReference databaseReference1 = firebaseDatabase.getReference(userName).child(oUser);

        TextMessageModel message = new TextMessageModel();
        message.setText(text);
        message.setSenderEmail(oUser);
        message.setTimestamp(new Date());
        message.setCurrency(currencyStr);
        message.setMoney(moneyInt);
        message.setMoneyStatus(moneyStatus);

        String uKey = databaseReference.push().getKey();

        assert uKey != null;

        databaseReference.child(uKey).setValue(message);
        databaseReference1.child(uKey).setValue(message);

    }

    private void onClickDescription(){

        findViewById(R.id.chatsActivityNoteTv).setLongClickable(true);
        findViewById(R.id.chatsActivityNoteTv).setClickable(true);

        findViewById(R.id.chatsActivityNoteTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("SHOULD_SHOW_MESSAGE_AGAIN",false);
                editor.apply();

                findViewById(R.id.chatsActivityNote).setVisibility(View.GONE);
            }
        });

        findViewById(R.id.chatsActivityNoteTv).setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("SHOULD_SHOW_MESSAGE_AGAIN",true);
                editor.apply();

                findViewById(R.id.chatsActivityNote).setVisibility(View.GONE);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed(){

        String oUser = MainActivity.user.getEmail().substring(0, MainActivity.user.getEmail().lastIndexOf("@gmail.com"));

        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(oUser + "." + userName, new Date().getTime());
        Log.e("Pref Key Activity", oUser + "." + userName);
        editor.apply();

        finish();
        loadedAlready = true;
    }
}
