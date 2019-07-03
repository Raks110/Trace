package com.rakshitgl.trace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.rakshitgl.trace.R;
import com.rakshitgl.trace.adapters.WalletAdapter;
import com.rakshitgl.trace.models.UserListMonetary;

import java.util.List;

public class WalletActivity extends AppCompatActivity {

    public static List<UserListMonetary> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        if(list == null || list.isEmpty()){
            finish();
        }

        RecyclerView recyclerView = findViewById(R.id.walletRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        WalletAdapter adapter = new WalletAdapter(list);

        recyclerView.setAdapter(adapter);
    }
}
