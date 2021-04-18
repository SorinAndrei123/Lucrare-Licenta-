package utilitare.general;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;

import java.util.List;
import java.util.Map;

public class AdaptorCardView extends RecyclerView.Adapter<AdaptorCardView.ViewHolder> {
    private Context context;
    private List<String>numeMaterie;

    public AdaptorCardView( Context context,List<String>numeMaterie) {
        this.context = context;
        this.numeMaterie=numeMaterie;
    }

    @NonNull
    @Override
    public AdaptorCardView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptorCardView.ViewHolder holder, int position) {
         holder.textView.setText(numeMaterie.get(position));


    }

    @Override
    public int getItemCount() {
        return numeMaterie.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textViewNumeMaterieCardView);
        }
    }
}
