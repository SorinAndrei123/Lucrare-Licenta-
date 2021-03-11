package utilitare.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;

import java.util.List;

import utilitare.general.Materie;

public class AdaptorRecyclerSchedule extends RecyclerView.Adapter<AdaptorRecyclerSchedule.ViewHolder> {

    private List<Materie> listaMaterii;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView numeMaterie,tip,zi,ora,profesor;

        public ViewHolder(View view) {
            super(view);
            numeMaterie=view.findViewById(R.id.textViewNumeMaterie);
            tip=view.findViewById(R.id.textViewTip);
            zi=view.findViewById(R.id.textViewZiuaMateriei);
            ora=view.findViewById(R.id.textViewOraMateriei);
            profesor=view.findViewById(R.id.textViewProfesor);

        }


    }

    public AdaptorRecyclerSchedule(List<Materie> dataSet) {
        listaMaterii = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_orar_fragment, viewGroup, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.numeMaterie.setText("Nume materie: "+listaMaterii.get(position).getNume());
        viewHolder.ora.setText("Ora: "+listaMaterii.get(position).getOra());
        viewHolder.tip.setText("Tip: "+listaMaterii.get(position).getTip());
        viewHolder.zi.setText("Zi: "+listaMaterii.get(position).getZi());
        viewHolder.profesor.setText("Profesor: "+listaMaterii.get(position).getProfesor());
    }

    @Override
    public int getItemCount() {
        return listaMaterii.size();
    }
}
