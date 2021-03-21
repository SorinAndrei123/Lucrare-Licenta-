package com.example.aplicatielicenta.profesor;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import utilitare.general.RecyclerItemClickListener;
import utilitare.general.Sarcina;
import utilitare.profesor.AdaptorAcordareNoteStudenti;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class VizualizareSarciniFragment extends Fragment {
static final String CHEIE="CHEIE";
TextView descriereTask,termenLimita,tipFisier;
String idSarcina;
FirebaseFirestore firebaseFirestore;
RecyclerView recyclerViewStudenti;
List<String>numeStudenti=new ArrayList<>();
AdaptorAcordareNoteStudenti adaptorAcordareNoteStudenti;
StorageReference storageReference;
Sarcina sarcina;


    public VizualizareSarciniFragment() {

    }


    public static VizualizareSarciniFragment newInstance(String idSarcina) {
        VizualizareSarciniFragment fragment = new VizualizareSarciniFragment();
        Bundle args = new Bundle();
        args.putString(CHEIE,idSarcina);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_vizualizare_sarcini,container,false);
       initComponents(view);
       return  view;
    }

    private void initComponents(View view) {
        descriereTask=view.findViewById(R.id.textViewDescriereTaskDetalii);
        termenLimita=view.findViewById(R.id.textViewTermenLimita);
        tipFisier=view.findViewById(R.id.textViewTipFisierDetalii);
         idSarcina=getArguments().getString(CHEIE);
        Toast.makeText(getContext().getApplicationContext(), idSarcina, Toast.LENGTH_SHORT).show();
         firebaseFirestore=FirebaseFirestore.getInstance();
         firebaseFirestore.collection("Sarcini").document(idSarcina).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if(task.isSuccessful()){
                     DocumentSnapshot documentSnapshot=task.getResult();
                      sarcina=documentSnapshot.toObject(Sarcina.class);
                     descriereTask.setText("Descriere sarcina: "+sarcina.getDescriere());
                     termenLimita.setText("Termen limita: ziua  "+ sarcina.getDataDeadline()+", ora "+sarcina.getOraDeadline());
                     tipFisier.setText("Tip fisier dorit : " +sarcina.getTipFisierDorit());

                 }
             }
         });

firebaseFirestore.collection("Fisiere").whereEqualTo("idSarcina",idSarcina).whereEqualTo("notaAcordata",0).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if(task.isSuccessful()){
            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                String numeStudent=queryDocumentSnapshot.get("numeStudent").toString();
                numeStudenti.add(numeStudent);


            }
            recyclerViewStudenti=view.findViewById(R.id.recyclerViewSarciniDePunctat);
            recyclerViewStudenti.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
            adaptorAcordareNoteStudenti=new AdaptorAcordareNoteStudenti(numeStudenti,sarcina,getContext().getApplicationContext(),idSarcina,storageReference);
            recyclerViewStudenti.setAdapter(adaptorAcordareNoteStudenti);
            adaptorAcordareNoteStudenti.notifyDataSetChanged();

        }

    }
});


    }


}