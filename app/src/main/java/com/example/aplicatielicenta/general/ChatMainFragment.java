package com.example.aplicatielicenta.general;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import utilitare.general.AdaptorMesaj;
import utilitare.general.ChatMessage;
import utilitare.general.User;


public class ChatMainFragment extends Fragment {
    FloatingActionButton trimiteMesaj;
    RecyclerView recyclerViewListaMesaje;
    EditText mesajDeTrimis;
    AdaptorMesaj adaptorMesaj;
  DatabaseReference databaseReference;
    User user;
    String numeGrup;
    static final String KEY="CHEIE";
    static final String KEY_NUME_GRUP="NMGR";
    List<ChatMessage>listaMesaje=new ArrayList<>();



    public ChatMainFragment() {

    }



    public static ChatMainFragment newInstance(User user,String numeGrup) {
        ChatMainFragment fragment = new ChatMainFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY,user);
        args.putSerializable(KEY_NUME_GRUP,numeGrup);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat_main, container, false);
        initComponents(view);
        return view;

    }

    private void initComponents(View view) {
        trimiteMesaj=view.findViewById(R.id.floatingActionButtonSendMessageGrup);
        mesajDeTrimis=view.findViewById(R.id.editTextMesajDeTrimisGrup);
        recyclerViewListaMesaje =view.findViewById(R.id.recyclerViewMesajeGrup);
        recyclerViewListaMesaje.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        user= (User) getArguments().getSerializable(KEY);
        numeGrup= (String) getArguments().getSerializable(KEY_NUME_GRUP);
       // databaseReference=FirebaseDatabase.getInstance().getReference("Mesaje").child(String.valueOf(user.getGrupa()));
        databaseReference=FirebaseDatabase.getInstance().getReference("Mesaje").child(numeGrup);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String mesaj=dataSnapshot.child("messageText").getValue().toString();
                    String messageUser=dataSnapshot.child("messageUser").getValue().toString();
                    String messageTime= dataSnapshot.child("messageTime").getValue().toString();
                    ChatMessage chatMessage=new ChatMessage();
                    chatMessage.setMessageText(mesaj);
                    chatMessage.setMessageTime(Long.valueOf(messageTime));
                    chatMessage.setMessageUser(messageUser);
                    listaMesaje.add(chatMessage);

                }
                adaptorMesaj=new AdaptorMesaj(listaMesaje);
                recyclerViewListaMesaje.setAdapter(adaptorMesaj);
                adaptorMesaj.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        trimiteMesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaMesaje.clear();
               //
                // FirebaseDatabase.getInstance().getReference("Mesaje").child(String.valueOf(user.getGrupa())).push().setValue(new ChatMessage(mesajDeTrimis.getText().toString(),user.getNume()));
                FirebaseDatabase.getInstance().getReference("Mesaje").child(numeGrup).push().setValue(new ChatMessage(mesajDeTrimis.getText().toString(),user.getNume()));
                mesajDeTrimis.setText("");
            }
        });
    }

}