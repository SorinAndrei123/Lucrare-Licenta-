package utilitare.general;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class AdaptorPrivateContactSelection extends RecyclerView.Adapter<AdaptorPrivateContactSelection.ViewHolder> {
    public List<User>listaContacte;
    public StorageReference storageReference;
    public Context context;

    public AdaptorPrivateContactSelection(List<User>listaContacte,StorageReference storageReference){
        this.listaContacte=listaContacte;
        this.storageReference=storageReference;
    }
    @NonNull
    @Override
    public AdaptorPrivateContactSelection.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_alegere_contact,parent,false);
       context=parent.getContext();
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptorPrivateContactSelection.ViewHolder holder, int position) {
        holder.nume.setText(this.listaContacte.get(position).getNume());

        Transformation transformation=new RoundedTransformationBuilder().borderColor(Color.BLACK).borderWidthDp(3).cornerRadiusDp(30).oval(false).build();
       storageReference.child(this.listaContacte.get(position).getNume()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().transform(transformation).into(holder.pozaProfil);
            }
        }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

           }
       });

    }

    @Override
    public int getItemCount() {
        return this.listaContacte.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nume;
        public ImageView pozaProfil;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nume=itemView.findViewById(R.id.textViewNumeContact);
            pozaProfil=itemView.findViewById(R.id.imageViewImagineProfilContact);

        }
    }
}
