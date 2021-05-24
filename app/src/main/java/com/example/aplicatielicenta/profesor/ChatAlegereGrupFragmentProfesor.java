package com.example.aplicatielicenta.profesor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;
import com.example.aplicatielicenta.general.ChatAlegereGrupFragment;
import com.example.aplicatielicenta.general.ChatMainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utilitare.general.AdaptorPreviewConversatie;
import utilitare.general.ChatMessage;
import utilitare.general.Materie;
import utilitare.general.RecyclerItemClickListener;
import utilitare.general.User;

public class ChatAlegereGrupFragmentProfesor extends Fragment {
    User user;
    RecyclerView grupuri;
    static final String KEY="cheita";
    DatabaseReference databaseReference;
    List<String> listaNumeGrupuri=new ArrayList<>();
    List<ChatMessage>listaUltimulMesaj=new ArrayList<>();
    StorageReference storageReference;
    AdaptorPreviewConversatie adaptorPreviewConversatie;
    FirebaseFirestore firebaseFirestore;
    List<Materie>listaMaterii=new ArrayList<>();
    Context context;
    List<String>numeGrupuriAbreviate=new ArrayList<>();


    public ChatAlegereGrupFragmentProfesor() {

    }


    public static ChatAlegereGrupFragmentProfesor newInstance(User user) {
        ChatAlegereGrupFragmentProfesor fragment = new ChatAlegereGrupFragmentProfesor();
        Bundle args = new Bundle();
        args.putSerializable(KEY,user);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_chat_alegere_grup, container, false);
        context=container.getContext().getApplicationContext();
        initView(view);
        return view;
    }

    private void initView(View view) {
        user= (User) getArguments().getSerializable(KEY);
        firebaseFirestore=FirebaseFirestore.getInstance();
        grupuri=view.findViewById(R.id.recyclerViewAlegereGrup);
        grupuri.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        storageReference= FirebaseStorage.getInstance().getReference("pozeChatProfil");
        firebaseFirestore.collection("Materii").whereIn("serie", Arrays.asList("A","B","C")).whereEqualTo("profesor",user.getNume()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                        Materie materie=queryDocumentSnapshot.toObject(Materie.class);
                        listaMaterii.add(materie);
                    }
                    for(Materie materie:listaMaterii){
                        listaNumeGrupuri.add(materie.getNume()+" seria "+materie.getSerie());
                        numeGrupuriAbreviate.add(materie.getNume()+materie.getSerie());
                        databaseReference=FirebaseDatabase.getInstance().getReference("Mesaje").child(materie.getNume()+materie.getSerie());
                        Query altQuerie=databaseReference.orderByKey().limitToLast(1);
                        altQuerie.addListenerForSingleValueEvent(new ValueEventListener() {
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
                               // Toast.makeText(context, "Dimensiune lista mesaje: "+String.valueOf(listaUltimulMesaj.size()), Toast.LENGTH_SHORT).show();
                                adaptorPreviewConversatie=new AdaptorPreviewConversatie(listaNumeGrupuri,listaUltimulMesaj,storageReference);
                                grupuri.setAdapter(adaptorPreviewConversatie);
                                grupuri.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), grupuri, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Fragment fragment= ChatMainFragment.newInstance(user,numeGrupuriAbreviate.get(position));
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_profesor,fragment).commit();



                                    }

                                    @Override
                                    public void onLongItemClick(View view, int position) {

                                    }
                                }));


                            }





                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }


                        });
                    }
                  ;

                }
                else{
                    Toast.makeText(context, "Task fara succes", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }
}
