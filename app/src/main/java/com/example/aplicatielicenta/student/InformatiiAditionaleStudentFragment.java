package com.example.aplicatielicenta.student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InformatiiAditionaleStudentFragment extends Fragment {
    String idMaterieSelectata;
    static final String cod="cod";
    TextView conectareZoom;
    FirebaseFirestore firebaseFirestore;
    String url;

    public InformatiiAditionaleStudentFragment() {
        // Required empty public constructor
    }

    public static InformatiiAditionaleStudentFragment newInstance(String codMaterie) {
        InformatiiAditionaleStudentFragment fragment = new InformatiiAditionaleStudentFragment();
        Bundle args = new Bundle();
        args.putString(cod,codMaterie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_informatii_aditionale_student,container,false);
        initComponents(view);
        return  view;
    }

    private void initComponents(View view) {
        idMaterieSelectata=getArguments().getString(cod);
        firebaseFirestore=FirebaseFirestore.getInstance();
        conectareZoom=view.findViewById(R.id.textViewConectarePlatforma);
       firebaseFirestore.collection("Materii").document(idMaterieSelectata).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    url=documentSnapshot.get("link conectare").toString();

                }
                conectareZoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(url!=null){
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }

                    }
                });
            }
        });



    }
}