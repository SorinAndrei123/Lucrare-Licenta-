package com.example.aplicatielicenta.profesor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import utilitare.general.AdaptorPrivateContactSelection;
import utilitare.general.Materie;
import utilitare.general.RecyclerItemClickListener;
import utilitare.general.User;

public class AlegereContactActivityProfesor extends AppCompatActivity {
    RecyclerView recyclerViewContacte;
    AdaptorPrivateContactSelection adaptorPrivateContactSelection;
    FirebaseFirestore firebaseFirestore;
    List<User> listaUtilizatori=new ArrayList<>();
    StorageReference storageReference;
    User user;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alegere_contact);
        firebaseFirestore=FirebaseFirestore.getInstance();
        intent=getIntent();
        user= (User)intent.getSerializableExtra("user");
        firebaseFirestore.collection("Studenti").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                        User user=queryDocumentSnapshot.toObject(User.class);
                        listaUtilizatori.add(user);
                    }
                }

                firebaseFirestore.collection("Profesori").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                                User user=queryDocumentSnapshot.toObject(User.class);
                                listaUtilizatori.add(user);
                            }

                            for(int i=0;i<listaUtilizatori.size();i++){
                                if(listaUtilizatori.get(i).getNume().equals(user.getNume())){
                                    listaUtilizatori.remove(i);
                                }
                            }

                        }
                        recyclerViewContacte=findViewById(R.id.recyclerViewAlegereContact);
                        recyclerViewContacte.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        storageReference= FirebaseStorage.getInstance().getReference("pozeProfil");
                        adaptorPrivateContactSelection=new AdaptorPrivateContactSelection(listaUtilizatori,storageReference);
                        recyclerViewContacte.setAdapter(adaptorPrivateContactSelection);
                        adaptorPrivateContactSelection.notifyDataSetChanged();
                        recyclerViewContacte.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerViewContacte, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intentIntoarcere=new Intent();
                                intentIntoarcere.putExtra("destinatar",listaUtilizatori.get(position).getNume());
                                setResult(RESULT_OK,intentIntoarcere);
                                finish();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        }));

                    }
                });
            }
        });




    }
}
