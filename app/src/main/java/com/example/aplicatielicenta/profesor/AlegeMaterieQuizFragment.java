package com.example.aplicatielicenta.profesor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

import utilitare.general.AdaptorCardView;
import utilitare.general.RecyclerItemClickListener;
import utilitare.general.User;


public class AlegeMaterieQuizFragment extends Fragment {

  List<String>numeMateriiNemodificate=new ArrayList<>();
  RecyclerView recyclerView;
  AdaptorCardView adaptorCardView;
  static final String KEY="key";
  User user;
  String numeProfesor;
 Task<QuerySnapshot> firebaseFirestore;
    public AlegeMaterieQuizFragment() {
        
    }

    public static AlegeMaterieQuizFragment newInstance(User user) {
        AlegeMaterieQuizFragment fragment = new AlegeMaterieQuizFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_alege_materie_quiz, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView=view.findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(),2));
        user= (User) getArguments().getSerializable(KEY);
        numeProfesor=user.getNume();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getContext().getApplicationContext(),CreareQuizActivity.class);
                intent.putExtra("numeMaterie",numeMateriiNemodificate.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        getData();

    }

    private void getData() {
        firebaseFirestore=FirebaseFirestore.getInstance().collection("Materii").whereEqualTo("profesor",numeProfesor).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                        String numeMaterie=queryDocumentSnapshot.get("nume").toString();
                        numeMateriiNemodificate.add(numeMaterie);
                    }
                }
                adaptorCardView=new AdaptorCardView(getContext().getApplicationContext(),numeMateriiNemodificate);
                recyclerView.setAdapter(adaptorCardView);


            }
        });

    }
}