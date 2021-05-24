package utilitare.profesor;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import utilitare.general.Sarcina;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class AdaptorAcordareNoteStudenti extends RecyclerView.Adapter<AdaptorAcordareNoteStudenti.ViewHolder> {
public List<String>listaStudentiNeevaluati;
public Sarcina sarcina;
public Context context;
public String idSarcina;
public StorageReference storageReference;
public Timer timer=new Timer();
public final long DELAY=1000;
public FirebaseFirestore firebaseFirestore;
public String idFisier;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView numeStudent;
        public EditText notaAcordata;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numeStudent=itemView.findViewById(R.id.textViewNumeStudent);
            notaAcordata=itemView.findViewById(R.id.editTextNotaDeAcordat);
        }


    }
    public AdaptorAcordareNoteStudenti(List<String> listaStudentiNeevaluati,Sarcina sarcina,Context context,String idSarcina,StorageReference storageReference){
        this.listaStudentiNeevaluati=listaStudentiNeevaluati;
        this.sarcina=sarcina;
        this.context=context;
        this.idSarcina=idSarcina;
        this.storageReference=storageReference;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_evaluare_teme_profesor,parent,false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.numeStudent.setText(this.listaStudentiNeevaluati.get(position));
        holder.numeStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numeStudent=listaStudentiNeevaluati.get(position);
                String numeFisier=numeStudent+idSarcina;
                storageReference= FirebaseStorage.getInstance().getReference("fisiere").child(numeFisier+"."+sarcina.getTipFisierDorit());
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url=uri.toString();
                        downloadFile(context,numeFisier,sarcina.getTipFisierDorit(),DIRECTORY_DOWNLOADS,url);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



            }
        });

        holder.notaAcordata.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(timer!=null){
                    timer.cancel();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            firebaseFirestore=FirebaseFirestore.getInstance();
                            firebaseFirestore.collection("Fisiere").whereEqualTo("idSarcina",idSarcina).whereEqualTo("numeStudent",listaStudentiNeevaluati.get(position)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        idFisier=task.getResult().getDocuments().get(0).getId();
                                        firebaseFirestore.collection("Fisiere").document(idFisier).update("notaAcordata",Integer.valueOf(holder.notaAcordata.getText().toString()));
                                    }

                                }
                            });



                        }
                    },DELAY);



            }
        });
    }

    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+fileExtension);
        downloadManager.enqueue(request);

    }

    @Override
    public int getItemCount() {
        return this.listaStudentiNeevaluati.size();
    }



}
