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

public class AdaptorMesaj extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    class ViewHolder1 extends  RecyclerView.ViewHolder{
        public TextView numePersoana,oraTrimiterii,mesaj;
        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            numePersoana=itemView.findViewById(R.id.textViewNumePersoanaMesaj);
            oraTrimiterii=itemView.findViewById(R.id.textViewDataMesaj);
            mesaj=itemView.findViewById(R.id.textViewMesaj);
        }
    }

    class ViewHolder2 extends  RecyclerView.ViewHolder {
        public TextView numePersoana,oraTrimiterii,mesaj;
        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            numePersoana=itemView.findViewById(R.id.textViewNumePersoanaMesajOther);
            oraTrimiterii=itemView.findViewById(R.id.textViewDataMesajOther);
            mesaj=itemView.findViewById(R.id.textViewMesajOther);
        }
    }
    private List<ChatMessage>listaMesaje;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mesaj_me,parent,false);
        View view2=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mesaj_other,parent,false);
      if(viewType==1){
          return new ViewHolder1(view1);
      }
      else{
          return  new ViewHolder2(view2);
      }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 1:
                ViewHolder1 viewHolder1=(ViewHolder1)holder;
                viewHolder1.mesaj.setText(this.listaMesaje.get(position).getMessageText());
                viewHolder1.numePersoana.setText(this.listaMesaje.get(position).getMessageUser());
                viewHolder1.oraTrimiterii.setText(DateFormat.format(" (HH:mm:ss)",
                        this.listaMesaje.get(position).getMessageTime()));
                break;
            case 2:
                ViewHolder2 viewHolder2= (ViewHolder2) holder;
                viewHolder2.mesaj.setText(this.listaMesaje.get(position).getMessageText());
                viewHolder2.numePersoana.setText(this.listaMesaje.get(position).getMessageUser());
                viewHolder2.oraTrimiterii.setText(DateFormat.format(" (HH:mm:ss)", this.listaMesaje.get(position).getMessageTime()));
                break;
        }




    }


    @Override
    public int getItemViewType(int position) {
        if(listaMesaje.get(position).isSentByMe()){
            return 1;
        }
        else{
            return 2;
        }
    }


    @Override
    public int getItemCount() {
        return this.listaMesaje.size();
    }

    public AdaptorMesaj(List<ChatMessage>chatMessages){
        this.listaMesaje=chatMessages;
    }
}





