package com.example.aplicatielicenta.profesor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import utilitare.general.AdaptorRecyclerIntrebare;
import utilitare.general.Intrebare;

public class DashboardQuizActivity extends AppCompatActivity {
List<Intrebare>listaIntrebari=new ArrayList<>();
RecyclerView recyclerViewIntrebari;
AdaptorRecyclerIntrebare adaptorRecyclerIntrebare;
ImageButton addQuestion;
Button finisQuestions;
Intent intent;
FirebaseFirestore firebaseFirestore;
String numeMaterieQuizActivity;
String numeMaterieAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_quiz);
        intent=getIntent();
        numeMaterieQuizActivity=intent.getStringExtra("numeMaterieQuizActivity");
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerViewIntrebari=findViewById(R.id.recyclerViewAfisareIntrebari);
        recyclerViewIntrebari.setLayoutManager(new LinearLayoutManager(this));


        firebaseFirestore.collection("Intrebari").whereEqualTo("materie",numeMaterieQuizActivity).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                        Intrebare intrebare=queryDocumentSnapshot.toObject(Intrebare.class);
                        listaIntrebari.add(intrebare);

                    }
                    adaptorRecyclerIntrebare=new AdaptorRecyclerIntrebare(listaIntrebari,numeMaterieQuizActivity,DashboardQuizActivity.this);
                    adaptorRecyclerIntrebare.setAdaptorRecyclerIntrebare(adaptorRecyclerIntrebare);
                    recyclerViewIntrebari.setAdapter(adaptorRecyclerIntrebare);
                    adaptorRecyclerIntrebare.notifyDataSetChanged();
                }

            }
        });


        addQuestion=findViewById(R.id.imageButtonAddQuestion);
        finisQuestions=findViewById(R.id.buttonFinishQuestions);
        finisQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ContPersonalProfesorActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.removeExtra("cheita");
                intent.removeExtra("intrebareDeModificat");
                setResult(RESULT_OK,intent);
                finish();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==69 && resultCode==RESULT_OK && data!=null){
            listaIntrebari.clear();
            firebaseFirestore.collection("Intrebari").whereEqualTo("materie",numeMaterieQuizActivity).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                            Intrebare intrebare=queryDocumentSnapshot.toObject(Intrebare.class);
                            listaIntrebari.add(intrebare);

                        }
                        adaptorRecyclerIntrebare=new AdaptorRecyclerIntrebare(listaIntrebari,numeMaterieQuizActivity,DashboardQuizActivity.this);
                        adaptorRecyclerIntrebare.setAdaptorRecyclerIntrebare(adaptorRecyclerIntrebare);
                        recyclerViewIntrebari.setAdapter(adaptorRecyclerIntrebare);
                        adaptorRecyclerIntrebare.notifyDataSetChanged();
                    }

                }
            });
        }
    }
}