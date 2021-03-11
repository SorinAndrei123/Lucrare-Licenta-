package com.example.aplicatielicenta.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import utilitare.general.RecyclerItemClickListener;
import utilitare.student.AdaptorRecyclerSchedule;
import utilitare.general.Materie;
import utilitare.general.User;

import static android.content.ContentValues.TAG;


public class ScheduleFragment extends Fragment {
private final static String cheie="OBITNERE_OBIECT";
User userPrimit;
RecyclerView recyclerView;
AdaptorRecyclerSchedule adaptorRecyclerSchedule;
List<Materie>listaMaterii;
FirebaseFirestore firebaseFirestore;
Fragment fragmentCurent;




    public ScheduleFragment() {

    }


    public static ScheduleFragment newInstance(User user) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(cheie,user);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_schedule,container,false);
        initComponent(view);
        return  view;
    }

    private void initComponent(View view) {
        listaMaterii=new ArrayList<>();
        userPrimit= (User) getArguments().getSerializable(cheie);
        recyclerView=view.findViewById(R.id.recyclerViewSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        adaptorRecyclerSchedule=new AdaptorRecyclerSchedule(listaMaterii);
        firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference reference=firebaseFirestore.collection("Materii");
        Task task1=reference.whereEqualTo("serie",userPrimit.getSerie()).get();
        Task task2=reference.whereEqualTo("grupa",String.valueOf(userPrimit.getGrupa())).get();
        Task<List<QuerySnapshot>> allTasks= Tasks.whenAllSuccess(task1,task2);
        allTasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                for(QuerySnapshot queryDocumentSnapshots:querySnapshots){
                    for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                        Materie materie=documentSnapshot.toObject(Materie.class);
                        listaMaterii.add(materie);
                    }
                }

            int dimTaskuri=allTasks.getResult().get(0).size()+allTasks.getResult().get(1).size();
                int dimTaskuri1=allTasks.getResult().get(0).size();
                Toast.makeText(getContext().getApplicationContext(), "Dimensiune taskuri: "+String.valueOf(dimTaskuri), Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(adaptorRecyclerSchedule);
                adaptorRecyclerSchedule.notifyDataSetChanged();
                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                       if(listaMaterii.get(position).getSerie()!=null){
                            Toast.makeText(getContext().getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                           String idMaterie=   allTasks.getResult().get(0).getDocuments().get(position).getId();
                           fragmentCurent=InformatiiAditionaleStudentFragment.newInstance(idMaterie);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentCurent).commit();
                        }
                       else if(listaMaterii.get(position).getGrupa()!=null){
                           String idMaterie=allTasks.getResult().get(1).getDocuments().get(position-dimTaskuri1).getId();
                           fragmentCurent=InformatiiAditionaleStudentFragment.newInstance(idMaterie);
                           getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentCurent).commit();

                       }


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
            }


        });


    }







}