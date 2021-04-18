package com.example.aplicatielicenta.student;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import utilitare.general.AdaptorPrivateContactSelection;
import utilitare.general.RecyclerItemClickListener;
import utilitare.general.User;


public class AlegereParticipantApelFragment extends Fragment {
User user;
static final String key="key";
RecyclerView recyclerViewAlegereParticipantApel;
List<User>listaUtilizatori=new ArrayList<>();
AdaptorPrivateContactSelection adaptorPrivateContactSelection;
FirebaseFirestore firebaseFirestore;
StorageReference storageReference;
List<String>listaPermisiuni=new ArrayList<>();
static final int requestCode=1;


    public AlegereParticipantApelFragment() {

    }

    public static AlegereParticipantApelFragment newInstance(User user) {
        AlegereParticipantApelFragment fragment = new AlegereParticipantApelFragment();
        Bundle args = new Bundle();
        args.putSerializable(key,user);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_alegere_participant_apel, container, false);
        initView(view);
        return view;
    }



    private void initView(View view) {


        User user= (User) getArguments().getSerializable(key);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Studenti").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                        User user1=queryDocumentSnapshot.toObject(User.class);
                        listaUtilizatori.add(user1);
                    }
                    for(int i=0;i<listaUtilizatori.size();i++){
                        if(listaUtilizatori.get(i).getNume().equals(user.getNume())){
                            listaUtilizatori.remove(i);
                        }

                    }
                    recyclerViewAlegereParticipantApel=view.findViewById(R.id.recyclerViewAlegereParticipantApel);
                    recyclerViewAlegereParticipantApel.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
                    storageReference=FirebaseStorage.getInstance().getReference("pozeProfil");
                    adaptorPrivateContactSelection=new AdaptorPrivateContactSelection(listaUtilizatori,storageReference);
                    recyclerViewAlegereParticipantApel.setAdapter(adaptorPrivateContactSelection);
                    adaptorPrivateContactSelection.notifyDataSetChanged();
                    recyclerViewAlegereParticipantApel.addOnItemTouchListener(new RecyclerItemClickListener(getContext().getApplicationContext(), recyclerViewAlegereParticipantApel, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                           // Fragment fragment=ApelVideoFragment.newInstance(user,listaUtilizatori.get(position).getNume());
                          //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    }));
                }
            }
        });



    }
}