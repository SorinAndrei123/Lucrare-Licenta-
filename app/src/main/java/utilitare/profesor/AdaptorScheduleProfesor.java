package utilitare.profesor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;

import java.util.List;

import utilitare.general.Materie;

public class AdaptorScheduleProfesor extends RecyclerView.Adapter<AdaptorScheduleProfesor.ViewHolder> {
private List<Materie> materieList;
    public static class ViewHolder extends RecyclerView.ViewHolder {
public TextView materie,tip,zi,ora,grupa,serie;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            materie=itemView.findViewById(R.id.textViewMaterieProf);
            tip=itemView.findViewById(R.id.textViewTipProf);
            zi=itemView.findViewById(R.id.textViewZiuaProf);
            ora=itemView.findViewById(R.id.textViewOraProf);
            grupa=itemView.findViewById(R.id.textViewGrupaProf);
            serie=itemView.findViewById(R.id.textViewSerieProf);

        }
    }
    public AdaptorScheduleProfesor(List<Materie>dataSet){
        this.materieList=dataSet;
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_orar_fragment_profesor,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.materie.setText("Materie: "+materieList.get(position).getNume());
        holder.zi.setText("Zi: "+materieList.get(position).getZi());
        holder.ora.setText("Ora: "+materieList.get(position).getOra());
        holder.tip.setText("Tip: "+materieList.get(position).getTip());
        if(materieList.get(position).getGrupa()!=null){
            holder.grupa.setText("Grupa "+materieList.get(position).getGrupa());
            holder.serie.setVisibility(View.GONE);
        }
        if(materieList.get(position).getGrupa()==null){
            holder.serie.setText("Seria "+materieList.get(position).getSerie());
            holder.grupa.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return materieList.size();
    }


}
