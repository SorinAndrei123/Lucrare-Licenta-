package utilitare.general;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.aplicatielicenta.R;

import java.util.List;

public class AdaptorSpinnerGrupa extends BaseAdapter {
    LayoutInflater layoutInflater;
    List<String>listaGrupe;
    public AdaptorSpinnerGrupa(Context context,List<String>listaGrupe){
        this.layoutInflater=LayoutInflater.from(context);
        this.listaGrupe=listaGrupe;
    }
    @Override
    public int getCount() {
        return this.listaGrupe.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=layoutInflater.inflate(R.layout.layout_spinner_grupa,null);
        TextView grupa=convertView.findViewById(R.id.textViewGrupaAdaptor);
        grupa.setText(this.listaGrupe.get(position));
        return  convertView;
    }
}
