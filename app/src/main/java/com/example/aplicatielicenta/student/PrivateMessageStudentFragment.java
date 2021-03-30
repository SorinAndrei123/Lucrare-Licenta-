package com.example.aplicatielicenta.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.aplicatielicenta.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import utilitare.general.AdaptorMesaj;
import utilitare.general.ChatMessage;
import utilitare.general.User;


public class PrivateMessageStudentFragment extends Fragment {
User user;
String destinatar;
static final String KEY_USER="key_user";
static final String KEY_DESTINATAR="key_destinatar";
FloatingActionButton trimiteMesaj;
RecyclerView recyclerViewMesaje;
EditText mesajDeTrimis;
AdaptorMesaj adaptorMesaj;
DatabaseReference databaseReference;
List<ChatMessage>listaMesaje=new ArrayList<>();
List<ChatMessage>listaMesajeDestinatar=new ArrayList<>();
List<ChatMessage>listaMesajeContCurent=new ArrayList<>();
String numeReferinta1;
String numeReferinta2;
    boolean esteRef1=false;
    boolean esteRef2=false;


    public PrivateMessageStudentFragment() {

    }


    public static PrivateMessageStudentFragment newInstance(User user,String destinatar) {
        PrivateMessageStudentFragment fragment = new PrivateMessageStudentFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER,user);
        args.putString(KEY_DESTINATAR,destinatar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_chat_main, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        user= (User) getArguments().getSerializable(KEY_USER);
        destinatar=getArguments().getString(KEY_DESTINATAR);
        trimiteMesaj=view.findViewById(R.id.floatingActionButtonSendMessageGrup);
        mesajDeTrimis=view.findViewById(R.id.editTextMesajDeTrimisGrup);
        recyclerViewMesaje =view.findViewById(R.id.recyclerViewMesajeGrup);
        recyclerViewMesaje.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        adaptorMesaj=new AdaptorMesaj(listaMesaje);
        recyclerViewMesaje.setAdapter(adaptorMesaj);
        numeReferinta1=destinatar+user.getNume();
        numeReferinta2=user.getNume()+destinatar;
        databaseReference= FirebaseDatabase.getInstance().getReference("Mesaje").child("Private");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(numeReferinta1)){
                    esteRef1=true;
                }
                if(snapshot.hasChild(numeReferinta2)){
                    esteRef2=true;
                }

                if(esteRef1==true){
                    databaseReference.child(numeReferinta1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            String mesajText=snapshot.child("messageText").getValue().toString();
                            String user=snapshot.child("messageUser").getValue().toString();
                            String time=snapshot.child("messageTime").getValue().toString();
                            ChatMessage chatMessage=new ChatMessage();
                            chatMessage.setMessageUser(user);
                            chatMessage.setMessageTime(Long.valueOf(time));
                            chatMessage.setMessageText(mesajText);
                            listaMesaje.add(chatMessage);
                            adaptorMesaj.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    databaseReference.child(numeReferinta2).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            String mesajText=snapshot.child("messageText").getValue().toString();
                            String user=snapshot.child("messageUser").getValue().toString();
                            String time=snapshot.child("messageTime").getValue().toString();
                            ChatMessage chatMessage=new ChatMessage();
                            chatMessage.setMessageUser(user);
                            chatMessage.setMessageTime(Long.valueOf(time));
                            chatMessage.setMessageText(mesajText);
                            listaMesaje.add(chatMessage);
                            adaptorMesaj.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        trimiteMesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference("Mesaje").child("Private");
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(numeReferinta1)){
                            esteRef1=true;
                        }
                        if(snapshot.hasChild(numeReferinta2)){
                            esteRef2=true;
                        }
                        if(esteRef1==true){
                            FirebaseDatabase.getInstance().getReference("Mesaje").child("Private").child(numeReferinta1).push().setValue(new ChatMessage(mesajDeTrimis.getText().toString(),user.getNume()));
                            mesajDeTrimis.setText("");
                        }
                        else{
                            FirebaseDatabase.getInstance().getReference("Mesaje").child("Private").child(numeReferinta2).push().setValue(new ChatMessage(mesajDeTrimis.getText().toString(),user.getNume()));
                            mesajDeTrimis.setText("");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });




    }
}