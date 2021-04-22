package utilitare.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;
import com.example.aplicatielicenta.student.IncarcareMaterialeSarcinaFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

import utilitare.general.Materie;
import utilitare.general.Sarcina;
import utilitare.general.UploadFisier;
import utilitare.general.User;


public class AdaptorRecyclerToateSarcinile extends RecyclerView.Adapter<AdaptorRecyclerToateSarcinile.ViewHolder> {
    Map<String, Materie>mapMaterii;
    List<Sarcina>listaSarcini;
    List<String>idSarcini;
    FirebaseFirestore firebaseFirestore;
    User userPrimit;
    Context context;

    public AdaptorRecyclerToateSarcinile(Map<String, Materie> mapMaterii, List<Sarcina> listaSarcini,User user,List<String>idSarcini,Context context) {
        this.mapMaterii = mapMaterii;
        this.listaSarcini = listaSarcini;
        this.firebaseFirestore=FirebaseFirestore.getInstance();
        this.userPrimit=user;
        this.idSarcini=idSarcini;
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_toate_sarcinile,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String idMaterie=listaSarcini.get(position).getIdMaterie();
        String numeMaterie=mapMaterii.get(idMaterie).getNume();
        holder.numeMaterie.setText("Materie: "+numeMaterie);
        holder.numeSarcina.setText("Sarcina: "+listaSarcini.get(position).getTitlu());
        holder.dataDeadline.setText(listaSarcini.get(position).getDataDeadline());
        firebaseFirestore.collection("Fisiere").whereEqualTo("idSarcina",this.idSarcini.get(position)).whereEqualTo("numeStudent",userPrimit.getNume()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
             if(task.getResult().size()==0){
                 holder.buttonSubmit.setVisibility(View.VISIBLE);
             }

                holder.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Fragment fragment= IncarcareMaterialeSarcinaFragment.newInstance(idSarcini.get(position),userPrimit);
                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
     return listaSarcini.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView numeMaterie,numeSarcina,dataDeadline;
        Button buttonSubmit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numeMaterie=itemView.findViewById(R.id.textViewAdaptorToateSarcinile);
            numeSarcina=itemView.findViewById(R.id.textViewAdaptorNumeSarcina);
            dataDeadline=itemView.findViewById(R.id.textViewDataDeadline);
            buttonSubmit=itemView.findViewById(R.id.buttonToBeSubmitted);
        }
    }
}
