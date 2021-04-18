package com.example.aplicatielicenta.student;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class StartQuizFragment extends Fragment {
String numeMaterie,numeStudent;
private static final String KEY="cheie";
Button buttonStart;
TextView highscore;
FirebaseFirestore firebaseFirestore;
   
 

    public StartQuizFragment() {
        
    }

    public static StartQuizFragment newInstance(String numeMaterie,String numeStudent) {
        StartQuizFragment fragment = new StartQuizFragment();
        Bundle args = new Bundle();
        args.putString(KEY,numeMaterie);
        args.putString("numeStudent",numeStudent);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     
        View view= inflater.inflate(R.layout.fragment_start_quiz, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        numeMaterie=getArguments().getString(KEY);
        numeStudent=getArguments().getString("numeStudent");
        buttonStart=view.findViewById(R.id.buttonStartQuiz);
        highscore=view.findViewById(R.id.textViewHighScore);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Scor").whereEqualTo("numeMaterie",numeMaterie).whereEqualTo("numeStudent",numeStudent).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().size()>0){
                        for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                            int scor=Integer.valueOf(queryDocumentSnapshot.get("highscore").toString());
                            highscore.setText("Highscore: "+String.valueOf(scor));
                        }
                    }

                }
            }
        });
       buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=QuizStudentFragment.newInstance(numeMaterie,numeStudent);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
    }
}