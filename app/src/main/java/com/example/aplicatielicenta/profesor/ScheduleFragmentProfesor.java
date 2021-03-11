package com.example.aplicatielicenta.profesor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import utilitare.profesor.AdaptorScheduleProfesor;
import utilitare.general.Materie;
import utilitare.general.RecyclerItemClickListener;
import utilitare.general.User;


public class ScheduleFragmentProfesor extends Fragment {
    private final static String cheie="OBITNERE_OBIECT";
User userPrimit;
RecyclerView recyclerView;
AdaptorScheduleProfesor adaptorScheduleProfesor;
List<Materie>listaMaterii;
Fragment currentFragment;
FirebaseFirestore firebaseFirestore;





    public ScheduleFragmentProfesor() {
        // Required empty public constructor
    }



    public static ScheduleFragmentProfesor newInstance(User user) {
        ScheduleFragmentProfesor fragment = new ScheduleFragmentProfesor();
        Bundle args = new Bundle();
        args.putSerializable(cheie,user);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view=inflater.inflate(R.layout.fragment_schedule_profesor,container,false);
       initComponent(view);
       return  view;

    }

    private void initComponent(View view) {
        listaMaterii=new ArrayList<>();
        userPrimit= (User) getArguments().getSerializable(cheie);
        firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference reference=firebaseFirestore.collection("Materii");
        reference.whereEqualTo("profesor",userPrimit.getNume()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                      Materie materie=queryDocumentSnapshot.toObject(Materie.class);
                        listaMaterii.add(materie);
                    }
                }
                recyclerView=view.findViewById(R.id.recyclerViewScheduleProf);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext() ));
                adaptorScheduleProfesor=new AdaptorScheduleProfesor(listaMaterii);
                recyclerView.setAdapter(adaptorScheduleProfesor);
                adaptorScheduleProfesor.notifyDataSetChanged();
                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String idMaterie=   task.getResult().getDocuments().get(position).getId();
                        currentFragment=AddTaskuriInfoProfFragment.newInstance(idMaterie);

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_profesor,currentFragment).commit();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

            }
        });

    }
}