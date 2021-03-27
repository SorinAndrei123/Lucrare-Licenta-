package utilitare.general;

import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptorPreviewConversatie extends RecyclerView.Adapter<AdaptorPreviewConversatie.ViewHolder> {
    public List<String>numeGrupuri;
    public List<ChatMessage>messages;
    public StorageReference storageReference;
    public AdaptorPreviewConversatie(List<String>numeGrupuri,List<ChatMessage>messages,StorageReference storageReference){
        this.numeGrupuri=numeGrupuri;
        this.messages=messages;
        this.storageReference=storageReference;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_alegere_conversatie,parent,false);
       return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.numePersoana.setText(this.numeGrupuri.get(position));
        holder.ultimulMesaj.setText(this.messages.get(position).getMessageText());
        holder.ora.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                this.messages.get(position).getMessageTime()));
        storageReference.child(this.numeGrupuri.get(position)+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(holder.imagineProfil);
            }
        });
      //  Picasso.get().load(this.poze.get(position)).fit().centerCrop().into(holder.imagineProfil);

    }

    @Override
    public int getItemCount() {
        return this.numeGrupuri.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ultimulMesaj,ora,numePersoana;
        ImageView imagineProfil;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ultimulMesaj=itemView.findViewById(R.id.textViewUltimulMesaj);
            ora=itemView.findViewById(R.id.textViewOraMesaj);
            numePersoana=itemView.findViewById(R.id.textViewNumeConversatie);
            imagineProfil=itemView.findViewById(R.id.imagineProfil);
        }
    }

}
