 package com.example.aplicatielicenta.student;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import utilitare.general.Sarcina;
import utilitare.general.UploadFisier;
import utilitare.general.User;

import static android.app.Activity.RESULT_OK;


public class IncarcareMaterialeSarcinaFragment extends Fragment {
String idSarcina;
static final String cheie="CHEIE_STRING";
static final String cheieUser="CHEIE_USER";
TextView descriereTask,termenLimita,tipFisier,notaPrimita;
FirebaseFirestore firebaseFirestore;
Button alegereFisier;
static final int COD_REQUEST_FISIER=24;
private Uri uri;
private StorageReference storageReference;
//private DatabaseReference databaseReference;
private ProgressBar progressBar;
Sarcina sarcina;
User user;
String idFisier;

    public IncarcareMaterialeSarcinaFragment() {
    }


    public static IncarcareMaterialeSarcinaFragment newInstance(String idSarcina,User user) {
        IncarcareMaterialeSarcinaFragment fragment = new IncarcareMaterialeSarcinaFragment();
        Bundle args = new Bundle();
        args.putString(cheie,idSarcina);
        args.putSerializable(cheieUser,user);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_incarcare_materiale_sarcina, container, false);
        initComponent(view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==COD_REQUEST_FISIER && resultCode==RESULT_OK && data !=null && data.getData()!=null){
            uri=data.getData();
            uploadFile();

        }
    }

    private void initComponent(View view) {
        descriereTask=view.findViewById(R.id.textViewDescriereTaskDetaliiStudent);
        termenLimita=view.findViewById(R.id.textViewTermenLimitaStudent);
        tipFisier=view.findViewById(R.id.textViewTipFisierDetaliiStudent);
        progressBar=view.findViewById(R.id.progressBarUploadFisier);
        storageReference= FirebaseStorage.getInstance().getReference("fisiere");
        notaPrimita=view.findViewById(R.id.textViewNotaPrimita);
        alegereFisier=view.findViewById(R.id.buttonChooseFile);
        firebaseFirestore=FirebaseFirestore.getInstance();
        idSarcina=getArguments().getString(cheie);
        user= (User) getArguments().getSerializable(cheieUser);

        firebaseFirestore.collection("Sarcini").document(idSarcina).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                     sarcina=documentSnapshot.toObject(Sarcina.class);
                    descriereTask.setText("Descriere task: "+sarcina.getDescriere());
                    termenLimita.setText("Data deadline: "+sarcina.getDataDeadline()+", ora: "+sarcina.getOraDeadline());
                    tipFisier.setText("Tip fisier acceptat: "+sarcina.getTipFisierDorit());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        firebaseFirestore.collection("Fisiere").whereEqualTo("idSarcina",idSarcina).whereEqualTo("numeStudent",user.getNume()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getDocuments().size()>0){
                        idFisier=task.getResult().getDocuments().get(0).getId();
                        firebaseFirestore.collection("Fisiere").document(idFisier).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot=task.getResult();
                                    UploadFisier uploadFisier=documentSnapshot.toObject(UploadFisier.class);
                                    if(uploadFisier.getNotaAcordata()!=0){
                                        notaPrimita.setText("Nota primita este :"+String.valueOf(uploadFisier.getNotaAcordata()));
                                        notaPrimita.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        alegereFisier.setVisibility(View.GONE);


                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }


                }
            }
        });

        alegereFisier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                switch (sarcina.getTipFisierDorit()){
                    case "pdf":
                        intent.setType("application/pdf");
                        break;
                    case "word":
                        intent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                        break;

                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,COD_REQUEST_FISIER);
            }
        });


    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
    private void uploadFile(){
        if(uri!=null){
            StorageReference fileReference=storageReference.child(user.getNume()+idSarcina+"."+getFileExtension(uri));
            fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },5000);
                    Toast.makeText(getContext().getApplicationContext(), "Upload succesful", Toast.LENGTH_SHORT).show();
                  UploadFisier uploadFisier=new UploadFisier(taskSnapshot.getUploadSessionUri().toString(),idSarcina,user.getNume());
                    firebaseFirestore.collection("Fisiere").add(uploadFisier);
                   // String uploadId=databaseReference.push().getKey();
                    //databaseReference.child(uploadId).setValue(uploadFisier);



                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progess=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progess);

                        }
                    });
            
        }
        else{
            Toast.makeText(getContext().getApplicationContext(), "Nu a fost selecat ", Toast.LENGTH_SHORT).show();
        }

    }
}