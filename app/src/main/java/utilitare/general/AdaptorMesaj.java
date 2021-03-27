package utilitare.general;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;

import java.util.List;

public class AdaptorMesaj extends RecyclerView.Adapter<AdaptorMesaj.ViewHolder> {
    private List<ChatMessage>listaMesaje;
    @NonNull
    @Override
    public AdaptorMesaj.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mesaj,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptorMesaj.ViewHolder holder, int position) {
        holder.mesaj.setText(this.listaMesaje.get(position).getMessageText());
        holder.numePersoana.setText(this.listaMesaje.get(position).getMessageUser());
        holder.oraTrimiterii.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                this.listaMesaje.get(position).getMessageTime()));

    }

    @Override
    public int getItemCount() {
        return this.listaMesaje.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView numePersoana,oraTrimiterii,mesaj;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numePersoana=itemView.findViewById(R.id.textViewNumePersoanaMesaj);
            oraTrimiterii=itemView.findViewById(R.id.textViewDataMesaj);
            mesaj=itemView.findViewById(R.id.textViewMesaj);
        }
    }

    public AdaptorMesaj(List<ChatMessage>listaMesaje){
        this.listaMesaje=listaMesaje;
    }
}
