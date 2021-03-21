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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_titlu_task,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titlu.setText(this.listaSarcini.get(position).getTitlu());



    }

    @Override
    public int getItemCount() {
        return this.listaSarcini.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView titlu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titlu=itemView.findViewById(R.id.textViewTitluTask);


        }
    }
    public AdaptorTask(List<Sarcina>listaSarcini){
        this.listaSarcini=listaSarcini;
    }
}
