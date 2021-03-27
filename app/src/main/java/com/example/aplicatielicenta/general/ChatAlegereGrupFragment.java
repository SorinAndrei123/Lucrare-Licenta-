package com.example.aplicatielicenta.general;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aplicatielicenta.R;
import com.example.aplicatielicenta.student.InformatiiAditionaleStudentFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import utilitare.general.AdaptorPreviewConversatie;
import utilitare.general.ChatMessage;
import utilitare.general.RecyclerItemClickListener;
import utilitare.general.User;

public class ChatAlegereGrupFragment extends Fragment {
User user;
RecyclerView grupuri;
static final String KEY="CHEIE";
DatabaseReference databaseReference;
List<String>listaNumeGrupuri=new ArrayList<>();
List<ChatMessage>listaUltimulMesaj=new ArrayList<>();
List<Uri>listaUri=new ArrayList<>();
StorageReference storageReference;
AdaptorPreviewConversatie adaptorPreviewConversatie;

    public ChatAlegereGrupFragment() {

    }


    public static ChatAlegereGrupFragment newInstance(User user) {
        ChatAlegereGrupFragment fragment = new ChatAlegereGrupFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY,user);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_chat_alegere_grup, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        user= (User) getArguments().getSerializable(KEY);
        listaNumeGrupuri.add(String.valueOf(user.getGrupa()));
        listaNumeGrupuri.add(user.getSerie());
        grupuri=view.findViewById(R.id.recyclerViewAlegereGrup);
        grupuri.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        storageReference= FirebaseStorage.getInstance().getReference("pozeChatProfil");
        databaseReference=FirebaseDatabase.getInstance().getReference("Mesaje").child(String.valueOf(user.getGrupa()));
        Query lastQueryGrupa=databaseReference.orderByKey().limitToLast(1);
        lastQueryGrupa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String mesajText=dataSnapshot.child("messageText").getValue().toString();
                    String user=dataSnapshot.child("messageUser").getValue().toString();
                    String time=dataSnapshot.child("messageTime").getValue().toString();
                    ChatMessage chatMessage=new ChatMessage();
                    chatMessage.setMessageUser(user);
                    chatMessage.setMessageTime(Long.valueOf(time));
                    chatMessage.setMessageText(mesajText);
                    listaUltimulMesaj.add(chatMessage);

                }
                databaseReference=FirebaseDatabase.getInstance().getReference("Mesaje").child(user.getSerie());
                Query lastQuerySerie=databaseReference.orderByKey().limitToLast(1);
                lastQuerySerie.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String mesajText=dataSnapshot.child("messageText").getValue().toString();
                            String user=dataSnapshot.child("messageUser").getValue().toString();
                            String time=dataSnapshot.child("messageTime").getValue().toString();
                            ChatMessage chatMessage=new ChatMessage();
                            chatMessage.setMessageUser(user);
                            chatMessage.setMessageTime(Long.valueOf(time));
                            chatMessage.setMessageText(mesajText);
                            listaUltimulMesaj.add(chatMessage);
                        }
                        adaptorPreviewConversatie=new AdaptorPreviewConversatie(listaNumeGrupuri,listaUltimulMesaj,storageReference);
                        grupuri.setAdapter(adaptorPreviewConversatie);
                        Toast.makeText(getContext().getApplicationContext(), String.valueOf(listaUltimulMesaj.size()), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
              //  adaptorPreviewConversatie.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        grupuri.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), grupuri, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Fragment fragment=ChatMainFragment.newInstance(user,listaNumeGrupuri.get(position));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();



            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }
}