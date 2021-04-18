package com.example.aplicatielicenta.student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import utilitare.general.AdaptorTask;
import utilitare.general.RecyclerItemClickListener;
import utilitare.general.Sarcina;
import utilitare.general.User;

public class InformatiiAditionaleStudentFragment extends Fragment {
    String idMaterieSelectata;
    static final String cod="cod";
    static final String codUser="COD_USER";
    TextView conectareZoom;
    FirebaseFirestore firebaseFirestore;
    String url;
    RecyclerView taskuriDeFacut;
    List<Sarcina>listaSarcini=new ArrayList<>();
    AdaptorTask adaptorTask;
    User user;
    TextView quiz;
    String numeMaterie;


    public InformatiiAditionaleStudentFragment() {
        // Required empty public constructor
    }

    public static InformatiiAditionaleStudentFragment newInstance(String codMaterie, User user) {
        InformatiiAditionaleStudentFragment fragment = new InformatiiAditionaleStudentFragment();
        Bundle args = new Bundle();
        args.putString(cod,codMaterie);
        args.putSerializable(codUser,user);
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
        user= (User) getArguments().getSerializable(codUser);
        firebaseFirestore=FirebaseFirestore.getInstance();
        quiz=view.findViewById(R.id.textViewQuiz);
        quiz.setVisibility(View.GONE);
        taskuriDeFacut=view.findViewById(R.id.recyclerViewStudentTaskuri);
        taskuriDeFacut.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        adaptorTask=new AdaptorTask(listaSarcini);
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

firebaseFirestore.collection("Sarcini").whereEqualTo("idMaterie",idMaterieSelectata).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if(task.isSuccessful()){
            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                Sarcina sarcina=queryDocumentSnapshot.toObject(Sarcina.class);
                listaSarcini.add(sarcina);
                taskuriDeFacut.setAdapter(adaptorTask);
                adaptorTask.notifyDataSetChanged();

            }
        }

    }
});
taskuriDeFacut.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), taskuriDeFacut, new RecyclerItemClickListener.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {
        firebaseFirestore.collection("Sarcini").whereEqualTo("idMaterie",idMaterieSelectata).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String idSarcina=task.getResult().getDocuments().get(position).getId();
                    Fragment fragment=IncarcareMaterialeSarcinaFragment.newInstance(idSarcina,user);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();


                }

            }
        });
    }

    @Override
    public void onLongItemClick(View view, int position) {

    }
}));

firebaseFirestore.collection("Materii").document(idMaterieSelectata).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if(task.isSuccessful()){
            DocumentSnapshot documentSnapshot=task.getResult();
             numeMaterie=documentSnapshot.get("nume").toString();
        }

        firebaseFirestore.collection("Intrebari").whereEqualTo("materie",numeMaterie).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().size()>0){
                    quiz.setText("Quiz de rezolvat");
                    quiz.setVisibility(View.VISIBLE);


                }
            }
        });

    }
});
quiz.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Fragment fragment=StartQuizFragment.newInstance(numeMaterie,user.getNume());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

    }
});


    }
}