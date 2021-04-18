package utilitare.general;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;
import com.example.aplicatielicenta.profesor.CreareQuizActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AdaptorRecyclerIntrebare extends RecyclerView.Adapter<AdaptorRecyclerIntrebare.ViewHolder> {
List<Intrebare>listaIntrebari;
AdaptorRecyclerIntrebare adaptorRecyclerIntrebare;
String numeMaterie;
Context context;

    public AdaptorRecyclerIntrebare(List<Intrebare> listaIntrebari,String numeMaterie,Context context) {
        this.listaIntrebari = listaIntrebari;
        this.numeMaterie=numeMaterie;
        this.context=context;
    }

    public void setAdaptorRecyclerIntrebare(AdaptorRecyclerIntrebare adaptorRecyclerIntrebare) {
        this.adaptorRecyclerIntrebare = adaptorRecyclerIntrebare;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_question_recycler_view,parent,false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.intrebare.setText(listaIntrebari.get(position).getCerinta());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Intrebari").whereEqualTo("materie",numeMaterie).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            String id=task.getResult().getDocuments().get(position).getId();
                            FirebaseFirestore.getInstance().collection("Intrebari").document(id).delete();
                            listaIntrebari.remove(position);
                            adaptorRecyclerIntrebare.notifyDataSetChanged();
                        }
                    }
                });

            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CreareQuizActivity.class);
                intent.putExtra("intrebareDeModificat",listaIntrebari.get(position));
                intent.putExtra("pozitieDeModificat",position);
                intent.putExtra("numeMaterie",numeMaterie);
                intent.putExtra("cheita","cheita");
               // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // context.startActivity(intent);
                ((Activity) context).startActivityForResult(intent,69);

            }
        });


    }

    @Override
    public int getItemCount() {
        return listaIntrebari.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView intrebare;
        ImageView edit,delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            intrebare=itemView.findViewById(R.id.textViewNumeIntrebareRecycler);
            edit=itemView.findViewById(R.id.imageViewEditQuestion);
            delete=itemView.findViewById(R.id.imageViewDeleteQuestion);



        }
    }
}
