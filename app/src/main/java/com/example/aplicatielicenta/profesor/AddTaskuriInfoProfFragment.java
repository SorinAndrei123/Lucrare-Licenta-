package com.example.aplicatielicenta.profesor;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicatielicenta.R;
import com.example.aplicatielicenta.logare.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import utilitare.general.AdaptorTask;
import utilitare.general.RecyclerItemClickListener;
import utilitare.general.Sarcina;

import static android.app.Activity.RESULT_OK;


public class AddTaskuriInfoProfFragment extends Fragment {
    String idMaterie;
    private final static String cheie = "cheie";
    EditText linkConectare;
    FloatingActionButton adaugareInfo;
    FirebaseFirestore firebaseFirestore;
    Button adaugaTaskNou;
    static final int REQUEST_CODE = 100;
    RecyclerView saptamana1, saptamana2;
    List<Sarcina> listaSarciniSapt1 = new ArrayList<>();
    List<Sarcina> listaSarciniSapt2 = new ArrayList<>();
    AdaptorTask adaptorTaskSaptamana1,adaptorTaskSaptamana2;
    String idSarcina;


    public AddTaskuriInfoProfFragment() {

    }

    public static AddTaskuriInfoProfFragment newInstance(String idMaterie) {
        AddTaskuriInfoProfFragment fragment = new AddTaskuriInfoProfFragment();
        Bundle args = new Bundle();
        args.putString(cheie, idMaterie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_taskuri_info_prof, container, false);
        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        saptamana1 = view.findViewById(R.id.recyclerViewSapt1);
        saptamana1.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        saptamana2 = view.findViewById(R.id.recyclerViewSapt2);
        saptamana2.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        adaugaTaskNou = view.findViewById(R.id.buttonaAdaugareTaskNou);
        firebaseFirestore = FirebaseFirestore.getInstance();
        idMaterie = getArguments().getString(cheie);
        adaptorTaskSaptamana1=new AdaptorTask(listaSarciniSapt1);
       adaptorTaskSaptamana2=new AdaptorTask(listaSarciniSapt2);
        linkConectare = view.findViewById(R.id.profLinkConectare);
        adaugareInfo = view.findViewById(R.id.floatingActionButtonAdaugareInformatii);
        firebaseFirestore.collection("Materii").document(idMaterie).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    linkConectare.setText(documentSnapshot.get("link conectare").toString());
                }

            }
        });

        adaugareInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Materii").document(idMaterie).update("link conectare", linkConectare.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "A fost adaugat cu succes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        adaugaTaskNou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), CreareTaskProfesorActivity.class);
                intent.putExtra("cod materie", idMaterie);
                  startActivityForResult(intent,REQUEST_CODE);
                //startActivity(intent);
            }
        });
        firebaseFirestore.collection("Sarcini").whereEqualTo("idMaterie",idMaterie).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                        Sarcina sarcina=queryDocumentSnapshot.toObject(Sarcina.class);
                        if(sarcina.getNrSaptDeadline().equals("1")){
                            listaSarciniSapt1.add(sarcina);
                            saptamana1.setAdapter(adaptorTaskSaptamana1);
                            adaptorTaskSaptamana1.notifyDataSetChanged();

                        }
                        else if(sarcina.getNrSaptDeadline().equals("2")){
                            listaSarciniSapt2.add(sarcina);
                            saptamana2.setAdapter(adaptorTaskSaptamana2);
                            adaptorTaskSaptamana2.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        saptamana1.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana1, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("1",position);



            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        saptamana2.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana2, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("2",position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        for(Fragment fragment:getChildFragmentManager().getFragments()){
            fragment.onActivityResult(requestCode,resultCode,data);
        }
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            String codTask=data.getStringExtra("codTask");
            if(codTask!=null){
                Toast.makeText(getContext().getApplicationContext(), "Codul este "+codTask, Toast.LENGTH_SHORT).show();
                firebaseFirestore.collection("Sarcini").document(codTask).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            Sarcina sarcina=documentSnapshot.toObject(Sarcina.class);
                            String saptamana=sarcina.getNrSaptDeadline();
                            if(saptamana.equals("1")){
                                listaSarciniSapt1.add(sarcina);
                                adaptorTaskSaptamana1.notifyDataSetChanged();
                            }
                            else if(saptamana.equals("2")){
                                listaSarciniSapt2.add(sarcina);
                                adaptorTaskSaptamana2.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        }


    }
    void searchSarcini(String saptamana,int pozitie){

        firebaseFirestore.collection("Sarcini").whereEqualTo("nrSaptDeadline",saptamana).whereEqualTo("idMaterie",idMaterie).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                  idSarcina=task.getResult().getDocuments().get(pozitie).getId();
                    Fragment fragment=VizualizareSarciniFragment.newInstance(idSarcina);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_profesor,fragment).commit();

                }
            }
        });


    }
}