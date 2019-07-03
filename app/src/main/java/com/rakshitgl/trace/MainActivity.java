package com.rakshitgl.trace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rakshitgl.trace.activities.ChatActivity;
import com.rakshitgl.trace.adapters.SearchUsersAdapter;
import com.rakshitgl.trace.fragments.ChatFragment;
import com.rakshitgl.trace.fragments.SettingFragment;
import com.rakshitgl.trace.fragments.WalletFragment;
import com.rakshitgl.trace.models.User;
import com.rakshitgl.trace.utils.Utilities;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean isAddMenuOpen;
    private List<User> users;
    public static SharedPreferences preferences;

    public static User user;

    public interface ClickListener{
        void onClick(View view,int position);
        void onLongClick(View view,int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        assert firebaseUser.getPhotoUrl() != null;

        user = new User();

        user.setDisplayName(firebaseUser.getDisplayName());
        user.setEmail(firebaseUser.getEmail());
        user.setPhotoURL(firebaseUser.getPhotoUrl().toString());

        isAddMenuOpen = false;
        preferences = getPreferences(Context.MODE_PRIVATE);

        setFragments();
        users = new ArrayList<>();

        onClickTabs(R.id.chats);
        onClickTabs(R.id.wallet);
        onClickTabs(R.id.setting);
        onClickAdd();
        onClickAddPersonal();

        searchUsers();
    }

    private void setFragments(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.chatContainer, ChatFragment.newInstance()).commit();

        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.settingContainer, SettingFragment.newInstance()).commit();

        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.walletContainer, WalletFragment.newInstance()).commit();

        initializeFragment(R.id.chatContainer);
        ((AppCompatImageButton) findViewById(R.id.chats)).setColorFilter(Color.parseColor("#4285F4"));


    }

    private void initializeFragment(int fragmentContainer){

        findViewById(R.id.chatContainer).setVisibility(View.GONE);
        findViewById(R.id.settingContainer).setVisibility(View.GONE);
        findViewById(R.id.walletContainer).setVisibility(View.GONE);

        findViewById(fragmentContainer).setVisibility(View.VISIBLE);
    }

    private void onClickTabs(final int clickedElement){

        findViewById(clickedElement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int container;

                switch(clickedElement){

                    case R.id.chats:
                        container = R.id.chatContainer;
                        break;

                    case R.id.setting:
                        container = R.id.settingContainer;
                        break;

                    case R.id.wallet:
                        container = R.id.walletContainer;
                        break;

                    default:
                        container = R.id.chatContainer;

                }

                initializeFragment(container);

                ((AppCompatImageButton) findViewById(R.id.chats)).setColorFilter(Color.parseColor("#000000"));
                ((AppCompatImageButton) findViewById(R.id.wallet)).setColorFilter(Color.parseColor("#000000"));
                ((AppCompatImageButton) findViewById(R.id.setting)).setColorFilter(Color.parseColor("#000000"));

                ((AppCompatImageButton) findViewById(clickedElement)).setColorFilter(Color.parseColor("#4285F4"));
            }
        });
    }

    private void onClickAdd(){

        findViewById(R.id.addChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isAddMenuOpen) {

                    isAddMenuOpen = true;

                    findViewById(R.id.addChat).animate().rotation(45).setDuration(750).withStartAction(new Runnable() {
                        @Override
                        public void run() {
                            ((AppCompatImageButton) findViewById(R.id.addChat)).setColorFilter(Color.parseColor("#4285F4"));
                        }
                    });

                    findViewById(R.id.chats).animate().alpha(0.0f).setDuration(375);
                    findViewById(R.id.wallet).animate().alpha(0.0f).setDuration(375).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.wallet).setVisibility(View.GONE);
                        }
                    });
                    findViewById(R.id.setting).animate().alpha(0.0f).setDuration(375).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.setting).setVisibility(View.GONE);

                            findViewById(R.id.addGroupChat).setVisibility(View.INVISIBLE);
                            findViewById(R.id.addGroupChat).animate().alpha(0.0f);
                            findViewById(R.id.addGroupChat).setVisibility(View.VISIBLE);

                            findViewById(R.id.addPersonalChat).setVisibility(View.INVISIBLE);
                            findViewById(R.id.addPersonalChat).animate().alpha(0.0f);
                            findViewById(R.id.addPersonalChat).setVisibility(View.VISIBLE);

                            findViewById(R.id.addPersonalChat).animate().alpha(1.0f).setDuration(375);
                            findViewById(R.id.addGroupChat).animate().alpha(1.0f).setDuration(375);
                        }
                    });
                }
                else{

                    isAddMenuOpen = false;

                    findViewById(R.id.addChat).animate().rotation(0).setDuration(750).withStartAction(new Runnable() {
                        @Override
                        public void run() {
                            ((AppCompatImageButton) findViewById(R.id.addChat)).setColorFilter(Color.parseColor("#000000"));
                        }
                    });

                    if(findViewById(R.id.appHeader).getVisibility() == View.GONE) {

                        findViewById(R.id.searchUsers).animate().alpha(0.0f).setDuration(375).withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                findViewById(R.id.searchUsers).clearFocus();
                                Utilities.hideKeyboard(MainActivity.this);
                                findViewById(R.id.searchUsers).setVisibility(View.GONE);

                                findViewById(R.id.appHeader).setVisibility(View.VISIBLE);
                                findViewById(R.id.appHeader).animate().alpha(1.0f).setDuration(375);
                            }
                        });

                        findViewById(R.id.searchResultRecycler).setVisibility(View.GONE);
                        findViewById(R.id.searchResultsFrame).setVisibility(View.GONE);
                    }

                    findViewById(R.id.addPersonalChat).animate().alpha(0.0f).setDuration(375).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.addPersonalChat).setVisibility(View.GONE);
                        }
                    });
                    findViewById(R.id.addGroupChat).animate().alpha(0.0f).setDuration(375).withEndAction(new Runnable() {
                        @Override
                        public void run() {

                            findViewById(R.id.addGroupChat).setVisibility(View.GONE);
                            findViewById(R.id.wallet).setVisibility(View.VISIBLE);
                            findViewById(R.id.setting).setVisibility(View.VISIBLE);

                            findViewById(R.id.chats).animate().alpha(1.0f).setDuration(375);
                            findViewById(R.id.wallet).animate().alpha(1.0f).setDuration(375);
                            findViewById(R.id.setting).animate().alpha(1.0f).setDuration(375);
                        }
                    });
                }
            }
        });
    }

    private void onClickAddPersonal(){

        findViewById(R.id.addPersonalChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utilities.getAllUsers(users);

                findViewById(R.id.searchResultsFrame).setVisibility(View.VISIBLE);

                findViewById(R.id.searchUsers).animate().alpha(0.0f);
                findViewById(R.id.appHeader).animate().alpha(0.0f).setDuration(375).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.appHeader).setVisibility(View.GONE);

                        findViewById(R.id.searchUsers).setVisibility(View.VISIBLE);
                        findViewById(R.id.searchUsers).animate().alpha(1.0f).setDuration(375).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                findViewById(R.id.searchUsers).requestFocus();
                            }
                        });
                    }
                });
            }
        });
    }

    private void searchUsers(){

        EditText editSearch = findViewById(R.id.searchUsers);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable edit) {

                RecyclerView recyclerView = findViewById(R.id.searchResultRecycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                if (edit.length() != 0) {

                    List<User> innerList = new ArrayList<>();

                    for(int i=0;i<users.size();i++){
                        if(users.get(i).getEmail().contains(edit.toString())){
                            innerList.add(users.get(i));
                            if(innerList.size() > 4){
                                break;
                            }
                        }
                    }

                    recyclerView.setVisibility(View.VISIBLE);
                    SearchUsersAdapter adapter = new SearchUsersAdapter(innerList);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    SearchUsersAdapter adapter = new SearchUsersAdapter(new ArrayList<User>());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.GONE);
                }

                listenTouchRecycler(recyclerView);
            }
        });

    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        private RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private void listenTouchRecycler(RecyclerView recyclerView){

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ChatActivity.user = new User();
                ChatActivity.user.setEmail(((TextView) view.findViewById(R.id.userEmail)).getText().toString());
                ChatActivity.user.setDisplayName(((TextView) view.findViewById(R.id.userName)).getText().toString());
                ChatActivity.user.setPhotoURL(((TextView) view.findViewById(R.id.hiddenPhotoUrl)).getText().toString());

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                ChatActivity.loadedAlready = false;
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

                Toast.makeText(getApplicationContext(),String.format(getResources().getString(R.string.connect_with_user), ((TextView) view.findViewById(R.id.userName)).getText().toString()), Toast.LENGTH_LONG).show();
            }
        }));
    }

    @Override
    public void onBackPressed(){
        if(isAddMenuOpen){
            findViewById(R.id.addChat).performClick();
        }
        else{
            super.onBackPressed();
        }
    }
}
