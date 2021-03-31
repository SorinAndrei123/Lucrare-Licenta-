package com.example.aplicatielicenta.student;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aplicatielicenta.R;
import com.example.aplicatielicenta.general.AlegereContactActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utilitare.general.AdaptorPreviewConversatie;
import utilitare.general.ChatMessage;
import utilitare.general.RecyclerItemClickListener;
import utilitare.general.User;

import static android.app.Activity.RESULT_OK;

public class PrivateChatFragment extends Fragment {
User user;
RecyclerView recyclerViewMesaje;
FloatingActionButton mesajPrivat;
private static final String cheie="KEY_USER";
private static final int cod=1000;
List<ChatMessage>listaMesaje=new ArrayList<>();
StorageReference storageReference;
AdaptorPreviewConversatie adaptorPreviewConversatie;
List<String>numeGrupuri=new ArrayList<>();
DatabaseReference databaseReference;
    public PrivateChatFragment() {
    }


    public static PrivateChatFragment newInstance(User user) {
        PrivateChatFragment fragment = new PrivateChatFragment();
        Bundle args = new Bundle();
        args.putSerializable(cheie,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_private_chat, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==cod && resultCode==RESULT_OK && data!=null){
            String destinatar=data.getStringExtra("destinatar");
            if(destinatar!=null){
                Fragment fragment= PrivateMessageStudentFragment.newInstance(user,destinatar);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        }
    }

    private void initView(View view) {
        user= (User) getArguments().getSerializable(cheie);
        recyclerViewMesaje=view.findViewById(R.id.recyclerViewPrivateMessages);
        recyclerViewMesaje.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        storageReference= FirebaseStorage.getInstance().getReference("pozeProfil");
        databaseReference= FirebaseDatabase.getInstance().getReference("Mesaje").child("Private");
        mesajPrivat=view.findViewById(R.id.floatingActionButtonSendPrivateMessage);
        mesajPrivat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext().getApplicationContext(), AlegereContactActivity.class);
                intent.putExtra("user",user);
               startActivityForResult(intent,cod);
                
            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                   String name=dataSnapshot.getKey();
                   if(name.contains(user.getNume())){
                       String numee=dataSnapshot.getKey().replace(user.getNume(),"");
                       Query query= FirebaseDatabase.getInstance().getReference("Mesaje").child("Private").child(name).orderByKey().limitToLast(1);
                       query.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                   String mesajText=dataSnapshot1.child("messageText").getValue().toString();
                                   String usernume=dataSnapshot1.child("messageUser").getValue().toString();
                                   String time=dataSnapshot1.child("messageTime").getValue().toString();
                                   ChatMessage chatMessage=new ChatMessage();
                                   chatMessage.setMessageUser(usernume);
                                   chatMessage.setMessageTime(Long.valueOf(time));
                                   chatMessage.setMessageText(mesajText);
                                   if(usernume.equals(user.getNume())){
                                       chatMessage.setSentByMe(true);
                                   }
                                   else{
                                       chatMessage.setSentByMe(false);
                                   }
                                   listaMesaje.add(chatMessage);
                               }
                               adaptorPreviewConversatie=new AdaptorPreviewConversatie(numeGrupuri,listaMesaje,storageReference);
                               recyclerViewMesaje.setAdapter(adaptorPreviewConversatie);
                               numeGrupuri.add(numee);
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });


                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


recyclerViewMesaje.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext(), recyclerViewMesaje, new RecyclerItemClickListener.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {
        Fragment fragment= PrivateMessageStudentFragment.newInstance(user,numeGrupuri.get(position));
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }

    @Override
    public void onLongItemClick(View view, int position) {

    }
}));




    }
}