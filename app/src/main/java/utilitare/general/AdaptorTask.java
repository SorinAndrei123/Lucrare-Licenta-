package utilitare.general;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;

import java.util.List;

public class AdaptorTask extends RecyclerView.Adapter<AdaptorTask.ViewHolder> {
    private List<Sarcina> listaSarcini;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.laytaskafisare,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.descriereTask.setText("Descriere task: "+this.listaSarcini.get(position).getDescriere());
        holder.tipFisierSuportat.setText("Tip fisier suportat: "+this.listaSarcini.get(position).getTipFisierDorit());
        holder.deadline.setText("Data: "+this.listaSarcini.get(position).getDataDeadline()+"  ora:"+ this.listaSarcini.get(position).getOraDeadline());
        holder.notaEvaluare.setText("Nota primita: "+this.listaSarcini.get(position).getNotaAcordata());



    }

    @Override
    public int getItemCount() {
        return this.listaSarcini.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView descriereTask,deadline,tipFisierSuportat,notaEvaluare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descriereTask=itemView.findViewById(R.id.textViewDescriereTask);
            deadline=itemView.findViewById(R.id.textViewDeadline);
            tipFisierSuportat=itemView.findViewById(R.id.textViewTipFisierSuportatLayout);
            notaEvaluare=itemView.findViewById(R.id.textViewNotaEvaluare);

        }
    }
    public AdaptorTask(List<Sarcina>listaSarcini){
        this.listaSarcini=listaSarcini;
    }
}
