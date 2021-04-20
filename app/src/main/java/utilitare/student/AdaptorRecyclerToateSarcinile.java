package utilitare.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;

import java.util.List;
import java.util.Map;

import utilitare.general.Materie;
import utilitare.general.Sarcina;


public class AdaptorRecyclerToateSarcinile extends RecyclerView.Adapter<AdaptorRecyclerToateSarcinile.ViewHolder> {
    Map<String, Materie>mapMaterii;
    List<Sarcina>listaSarcini;

    public AdaptorRecyclerToateSarcinile(Map<String, Materie> mapMaterii, List<Sarcina> listaSarcini) {
        this.mapMaterii = mapMaterii;
        this.listaSarcini = listaSarcini;
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

    /*    for(int i=0;i<listaSarcini.size();i++){
            String idMaterie=listaSarcini.get(i).getIdMaterie();
            String numeMaterie=mapMaterii.get(idMaterie).getNume();
            holder.numeMaterie.setText(numeMaterie);
            holder.numeSarcina.setText(listaSarcini.get(i).getTitlu());
        }*/

    }

    @Override
    public int getItemCount() {
     return listaSarcini.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView numeMaterie,numeSarcina;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numeMaterie=itemView.findViewById(R.id.textViewAdaptorToateSarcinile);
            numeSarcina=itemView.findViewById(R.id.textViewAdaptorNumeSarcina);
        }
    }
}
