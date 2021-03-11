package com.example.aplicatielicenta.profesor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AddTaskuriInfoProfFragment extends Fragment {
String idMaterie;
private final static String cheie="cheie";
EditText linkConectare;
FloatingActionButton adaugareInfo;
FirebaseFirestore firebaseFirestore;
Button adaugaTaskNou;
static final int REQUEST_CODE=100;



    public AddTaskuriInfoProfFragment() {

    }

    public static AddTaskuriInfoProfFragment newInstance(String idMaterie) {
        AddTaskuriInfoProfFragment fragment = new AddTaskuriInfoProfFragment();
        Bundle args = new Bundle();;
        args.putString(cheie,idMaterie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_taskuri_info_prof,container,false);
        initComponent(view);
        return  view;
    }

    private void initComponent(View view) {
        adaugaTaskNou=view.findViewById(R.id.buttonaAdaugareTaskNou);
        firebaseFirestore=FirebaseFirestore.getInstance();
        idMaterie=getArguments().getString(cheie);
        linkConectare=view.findViewById(R.id.profLinkConectare);
        adaugareInfo=view.findViewById(R.id.floatingActionButtonAdaugareInformatii);
        firebaseFirestore.collection("Materii").document(idMaterie).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    linkConectare.setText(documentSnapshot.get("link conectare").toString());
                }

            }
        });

        adaugareInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Materii").document(idMaterie).update("link conectare",linkConectare.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "A fost adaugat cu succes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
adaugaTaskNou.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getContext().getApplicationContext(),CreareTaskProfesorActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }
});

    }
}