package com.example.aplicatielicenta.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilitare.general.Materie;
import utilitare.general.RecyclerItemClickListener;
import utilitare.general.Sarcina;
import utilitare.general.User;
import utilitare.student.AdaptorRecyclerToateSarcinile;


public class VizualizareSarciniTotaleFragment extends Fragment {
    private static final String KEY_USER="key";
    User userPrimit;
    String serieStudent;
    Integer grupaStudent;
    FirebaseFirestore firebaseFirestore;
    List<Sarcina>listaSarcini=new ArrayList<>();
    Map<String,Materie>mapMaterii=new HashMap<>();
    List<String>listaIdSarcini=new ArrayList<>();
    RecyclerView recyclerViewToateSarcinile;
    AdaptorRecyclerToateSarcinile adaptorRecyclerToateSarcinile;

    public VizualizareSarciniTotaleFragment() {
    }



    public static VizualizareSarciniTotaleFragment newInstance(User user) {
        VizualizareSarciniTotaleFragment fragment = new VizualizareSarciniTotaleFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER,user);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_vizualizare_sarcini_totale, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        userPrimit= (User) getArguments().getSerializable(KEY_USER);
        serieStudent=userPrimit.getSerie();
        grupaStudent=userPrimit.getGrupa();
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerViewToateSarcinile=view.findViewById(R.id.recyclerViewToateSarcinile);
        recyclerViewToateSarcinile.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        CollectionReference reference=firebaseFirestore.collection("Materii");
        Task task1=reference.whereEqualTo("serie",serieStudent).get();
        Task task2=reference.whereEqualTo("grupa",String.valueOf(grupaStudent)).get();
        Task<List<QuerySnapshot>> allTasks= Tasks.whenAllSuccess(task1,task2);
        allTasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                for(QuerySnapshot querySnapshot:querySnapshots){
                    for(QueryDocumentSnapshot queryDocumentSnapshot:querySnapshot){
                   String idMaterie=queryDocumentSnapshot.getId();
                   Materie materie=queryDocumentSnapshot.toObject(Materie.class);
                   mapMaterii.put(idMaterie,materie);

                    }
                }
                firebaseFirestore.collection("Sarcini").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                                Sarcina sarcina=queryDocumentSnapshot.toObject(Sarcina.class);
                                String idSarcina=queryDocumentSnapshot.getId();
                                for(String idMaterie:mapMaterii.keySet()){
                                    if(sarcina.getIdMaterie().equals(idMaterie)){
                                        listaSarcini.add(sarcina);
                                        listaIdSarcini.add(idSarcina);


                                    }
                                }
                            }
                            adaptorRecyclerToateSarcinile=new AdaptorRecyclerToateSarcinile(mapMaterii,listaSarcini);
                            recyclerViewToateSarcinile.setAdapter(adaptorRecyclerToateSarcinile);
                            adaptorRecyclerToateSarcinile.notifyDataSetChanged();
                            recyclerViewToateSarcinile.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), recyclerViewToateSarcinile, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Fragment fragment=IncarcareMaterialeSarcinaFragment.newInstance(listaIdSarcini.get(position),userPrimit);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                                }

                                @Override
                                public void onLongItemClick(View view, int position) {

                                }
                            }));
                        }


                    }
                });



            }
        });

    }


}