package com.rakshitgl.trace.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.rakshitgl.trace.activities.ChatActivity;
import com.rakshitgl.trace.adapters.ChatsListAdapter;
import com.rakshitgl.trace.models.ChatsListModel;
import com.rakshitgl.trace.models.TextMessageModel;
import com.rakshitgl.trace.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatFragment extends Fragment {

    private View view;
    private int positionClick;
    private ChatsListAdapter adapter;
    private List<ChatsListModel> list;

    public interface ClickListener{
        void onClick(View view,int position);
        void onLongClick(View view,int position);
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance() {

        return new ChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        positionClick = -1;

        assert getActivity() != null;
        getActivity().findViewById(R.id.loadingMessagesList).setVisibility(View.VISIBLE);

        checkIfChatsListExists();
        return view;
    }

    private void checkIfChatsListExists(){

        assert getActivity() != null;

        getActivity().findViewById(R.id.loadingMessagesList).setVisibility(View.VISIBLE);
        view.findViewById(R.id.chatsListRecycler).setVisibility(View.GONE);

        list = new ArrayList<>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(MainActivity.user.getEmail().substring(0, MainActivity.user.getEmail().lastIndexOf("@gmail.com")));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){

                    view.findViewById(R.id.emptyMessages).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.chatsListRecycler).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.loadingMessagesList).setVisibility(View.GONE);
                }
                else{

                    final long length = dataSnapshot.getChildrenCount();

                    view.findViewById(R.id.chatsListRecycler).setVisibility(View.VISIBLE);

                    list.clear();

                    for(final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        final String email = dataSnapshot1.getKey();

                        try {

                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users");
                            databaseReference1.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User userRecord = dataSnapshot.getValue(User.class);

                                    ChatsListModel model = new ChatsListModel();
                                    model.setDisplayName(userRecord.getDisplayName());
                                    model.setEmail(userRecord.getEmail());
                                    model.setPhotoURL(userRecord.getPhotoURL());

                                    String oUser = MainActivity.user.getEmail().substring(0, MainActivity.user.getEmail().lastIndexOf("@gmail.com"));

                                    model.setLastSeen(MainActivity.preferences.getLong(oUser + "." + email, 0));


                                    Log.e("Pref Key", oUser + "." + email);

                                    for(DataSnapshot s: dataSnapshot1.getChildren()){

                                        TextMessageModel messageModel = s.getValue(TextMessageModel.class);
                                        model.setLastMessageReceived(messageModel.getTimestamp());
                                        model.setLastMessage(messageModel.getText());
                                    }

                                    list.add(model);

                                    if(list.size() >= length){

                                        getActivity().findViewById(R.id.loadingMessagesList).setVisibility(View.GONE);
                                        view.findViewById(R.id.emptyMessages).setVisibility(View.GONE);

                                        RecyclerView recyclerView = view.findViewById(R.id.chatsListRecycler);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

                                        recyclerView.setPadding(0,getActivity().findViewById(R.id.mcv_header).getHeight() + 24,0,0);
                                        recyclerView.setLayoutManager(linearLayoutManager);

                                        adapter = new ChatsListAdapter(list);
                                        recyclerView.setAdapter(adapter);

                                        listenToTouchRecycler(recyclerView);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        catch (Exception e){
                            Log.v("Trace", "nothing added.");
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void listenToTouchRecycler(RecyclerView recyclerView){

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ChatActivity.user = new User();
                ChatActivity.user.setEmail(((TextView) view.findViewById(R.id.hiddenEmailChats)).getText().toString());
                ChatActivity.user.setDisplayName(((TextView) view.findViewById(R.id.userNameChats)).getText().toString());
                ChatActivity.user.setPhotoURL(((TextView) view.findViewById(R.id.hiddenPhotoUrlChats)).getText().toString());

                positionClick = position;

                ChatActivity.loadedAlready = false;

                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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

    @Override
    public void onResume(){
        super.onResume();

        if(positionClick >= 0){

            ChatsListModel model = list.get(positionClick);
            model.setLastSeen(new Date().getTime());

            adapter.notifyItemChanged(positionClick);
        }
    }
}
