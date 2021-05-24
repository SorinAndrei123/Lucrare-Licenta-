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
    RecyclerView saptamana1, saptamana2,saptamana3,saptamana4,saptamana5,saptamana6,saptamana7,saptamana8,saptamana9,saptamana10,saptamana11,
            saptamana12,saptamana13,saptamana14;
    List<Sarcina> listaSarciniSapt1 = new ArrayList<>();
    List<Sarcina> listaSarciniSapt2 = new ArrayList<>();
    List<Sarcina> listaSarciniSapt3 = new ArrayList<>();
    List<Sarcina> listaSarciniSapt4 = new ArrayList<>();
    List<Sarcina> listaSarciniSapt5 = new ArrayList<>();
    List<Sarcina> listaSarciniSapt6 = new ArrayList<>();
    List<Sarcina> listaSarciniSapt7 = new ArrayList<>();
    List<Sarcina> listaSarciniSapt8 = new ArrayList<>();
    List<Sarcina> listaSarciniSapt9 = new ArrayList<>();
    List<Sarcina> listaSarciniSapt10 = new ArrayList<>();
    List<Sarcina> listaSarciniSapt11= new ArrayList<>();
    List<Sarcina> listaSarciniSapt12= new ArrayList<>();
    List<Sarcina> listaSarciniSapt13= new ArrayList<>();
    List<Sarcina> listaSarciniSapt14 = new ArrayList<>();

    AdaptorTask adaptorTaskSaptamana1,adaptorTaskSaptamana2,adaptorTaskSaptamana3,adaptorTaskSaptamana4,adaptorTaskSaptamana5,adaptorTaskSaptamana6,
    adaptorTaskSaptamana7,adaptorTaskSaptamana8,adaptorTaskSaptamana9,adaptorTaskSaptamana10,adaptorTaskSaptamana11,
            adaptorTaskSaptamana12,adaptorTaskSaptamana13,adaptorTaskSaptamana14;
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
        saptamana3 = view.findViewById(R.id.recyclerViewSapt3);
        saptamana3.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        saptamana4 = view.findViewById(R.id.recyclerViewSapt4);
        saptamana4.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        saptamana5 = view.findViewById(R.id.recyclerViewSapt5);
        saptamana5.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        saptamana6 = view.findViewById(R.id.recyclerViewSapt6);
        saptamana6.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        saptamana7 = view.findViewById(R.id.recyclerViewSapt7);
        saptamana7.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        saptamana8 = view.findViewById(R.id.recyclerViewSapt8);
        saptamana8.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        saptamana9 = view.findViewById(R.id.recyclerViewSapt9);
        saptamana9.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        saptamana10 = view.findViewById(R.id.recyclerViewSapt10);
        saptamana10.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        saptamana11 = view.findViewById(R.id.recyclerViewSapt1);
        saptamana11.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        saptamana12 = view.findViewById(R.id.recyclerViewSapt12);
        saptamana12.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        saptamana13 = view.findViewById(R.id.recyclerViewSapt13);
        saptamana13.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        saptamana14 = view.findViewById(R.id.recyclerViewSapt14);
        saptamana14.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        adaugaTaskNou = view.findViewById(R.id.buttonaAdaugareTaskNou);
        firebaseFirestore = FirebaseFirestore.getInstance();
        idMaterie = getArguments().getString(cheie);
        adaptorTaskSaptamana1=new AdaptorTask(listaSarciniSapt1);
       adaptorTaskSaptamana2=new AdaptorTask(listaSarciniSapt2);
        adaptorTaskSaptamana3=new AdaptorTask(listaSarciniSapt3);
        adaptorTaskSaptamana4=new AdaptorTask(listaSarciniSapt4);
        adaptorTaskSaptamana5=new AdaptorTask(listaSarciniSapt5);
        adaptorTaskSaptamana6=new AdaptorTask(listaSarciniSapt6);
        adaptorTaskSaptamana7=new AdaptorTask(listaSarciniSapt7);
        adaptorTaskSaptamana8=new AdaptorTask(listaSarciniSapt8);
        adaptorTaskSaptamana9=new AdaptorTask(listaSarciniSapt9);
        adaptorTaskSaptamana10=new AdaptorTask(listaSarciniSapt10);
        adaptorTaskSaptamana11=new AdaptorTask(listaSarciniSapt11);
        adaptorTaskSaptamana12=new AdaptorTask(listaSarciniSapt12);
        adaptorTaskSaptamana13=new AdaptorTask(listaSarciniSapt13);
        adaptorTaskSaptamana14=new AdaptorTask(listaSarciniSapt14);
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
        afisareSarcini();


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

        saptamana3.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana3, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("3",position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        saptamana4.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana4, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("4",position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        saptamana5.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana5, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("5",position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        saptamana6.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana6, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("6",position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        saptamana7.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana7, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("7",position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        saptamana8.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana8, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("8",position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        saptamana9.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana9, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("9",position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        saptamana10.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana10, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("10",position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        saptamana11.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana11, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("11",position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        saptamana12.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana12, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("12",position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        saptamana13.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana13, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("13",position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        saptamana14.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext().getApplicationContext(), saptamana14, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchSarcini("14",position);
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

afisareSarcini();
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

    void afisareSarcini(){
        firebaseFirestore.collection("Sarcini").whereEqualTo("idMaterie",idMaterie).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                        Sarcina sarcina=queryDocumentSnapshot.toObject(Sarcina.class);
                        if(sarcina.getNrSaptDeadline().equals("1")){
                            listaSarciniSapt1.clear();
                            listaSarciniSapt1.add(sarcina);
                            saptamana1.setAdapter(adaptorTaskSaptamana1);
                            adaptorTaskSaptamana1.notifyDataSetChanged();

                        }
                        else if(sarcina.getNrSaptDeadline().equals("2")){
                            listaSarciniSapt2.clear();
                            listaSarciniSapt2.add(sarcina);
                            saptamana2.setAdapter(adaptorTaskSaptamana2);
                            adaptorTaskSaptamana2.notifyDataSetChanged();
                        }
                        else if(sarcina.getNrSaptDeadline().equals("3")){
                            listaSarciniSapt3.clear();
                            listaSarciniSapt3.add(sarcina);
                            saptamana3.setAdapter(adaptorTaskSaptamana3);
                            adaptorTaskSaptamana3.notifyDataSetChanged();
                        }
                        else if(sarcina.getNrSaptDeadline().equals("4")){
                            listaSarciniSapt4.clear();
                            listaSarciniSapt4.add(sarcina);
                            saptamana4.setAdapter(adaptorTaskSaptamana4);
                            adaptorTaskSaptamana4.notifyDataSetChanged();
                        }
                        else if(sarcina.getNrSaptDeadline().equals("5")){
                            listaSarciniSapt5.clear();
                            listaSarciniSapt5.add(sarcina);
                            saptamana5.setAdapter(adaptorTaskSaptamana5);
                            adaptorTaskSaptamana5.notifyDataSetChanged();
                        }
                        else if(sarcina.getNrSaptDeadline().equals("6")){
                            listaSarciniSapt6.clear();
                            listaSarciniSapt6.add(sarcina);
                            saptamana6.setAdapter(adaptorTaskSaptamana6);
                            adaptorTaskSaptamana6.notifyDataSetChanged();
                        }
                        else if(sarcina.getNrSaptDeadline().equals("7")){
                            listaSarciniSapt7.clear();
                            listaSarciniSapt7.add(sarcina);
                            saptamana7.setAdapter(adaptorTaskSaptamana7);
                            adaptorTaskSaptamana7.notifyDataSetChanged();
                        }
                        else if(sarcina.getNrSaptDeadline().equals("8")){
                            listaSarciniSapt8.clear();
                            listaSarciniSapt8.add(sarcina);
                            saptamana8.setAdapter(adaptorTaskSaptamana8);
                            adaptorTaskSaptamana8.notifyDataSetChanged();
                        }
                        else if(sarcina.getNrSaptDeadline().equals("9")){
                            listaSarciniSapt9.clear();
                            listaSarciniSapt9.add(sarcina);
                            saptamana9.setAdapter(adaptorTaskSaptamana9);
                            adaptorTaskSaptamana9.notifyDataSetChanged();
                        }
                        else if(sarcina.getNrSaptDeadline().equals("10")){
                            listaSarciniSapt10.clear();
                            listaSarciniSapt10.add(sarcina);
                            saptamana10.setAdapter(adaptorTaskSaptamana10);
                            adaptorTaskSaptamana10.notifyDataSetChanged();
                        }
                        else if(sarcina.getNrSaptDeadline().equals("11")){
                            listaSarciniSapt11.clear();
                            listaSarciniSapt11.add(sarcina);
                            saptamana11.setAdapter(adaptorTaskSaptamana11);
                            adaptorTaskSaptamana11.notifyDataSetChanged();
                        }
                        else if(sarcina.getNrSaptDeadline().equals("12")){
                            listaSarciniSapt12.clear();
                            listaSarciniSapt12.add(sarcina);
                            saptamana12.setAdapter(adaptorTaskSaptamana12);
                            adaptorTaskSaptamana12.notifyDataSetChanged();
                        }
                        else if(sarcina.getNrSaptDeadline().equals("13")){
                            listaSarciniSapt13.clear();
                            listaSarciniSapt13.add(sarcina);
                            saptamana13.setAdapter(adaptorTaskSaptamana13);
                            adaptorTaskSaptamana13.notifyDataSetChanged();
                        }
                        else if(sarcina.getNrSaptDeadline().equals("14")){
                            listaSarciniSapt14.clear();
                            listaSarciniSapt14.add(sarcina);
                            saptamana14.setAdapter(adaptorTaskSaptamana14);
                            adaptorTaskSaptamana14.notifyDataSetChanged();
                        }
                    }
                }
            }
        });



    }

}