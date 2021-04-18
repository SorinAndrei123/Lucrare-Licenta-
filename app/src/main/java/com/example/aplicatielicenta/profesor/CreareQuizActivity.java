package com.example.aplicatielicenta.profesor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utilitare.general.Intrebare;

public class CreareQuizActivity extends AppCompatActivity {
    EditText cerinta,varianta1,varianta2,varianta3,varianta4;
    Button adaugaIntrebarile;
    List<String>variante=new ArrayList<>();
    String CERINTA,VARIANTA1,VARIANTA2,VARIANTA3,VARIANTA4;
    ToggleButton toggleButton1,toggleButton2,toggleButton3,toggleButton4;
    RadioGroup radioGroupToggleButtons;
    int RASPUNS_CORECT;
    String numeMaterie;
    FirebaseFirestore firebaseFirestore;
    Intrebare intrebareDeModificat;
    Integer pozitieInLista;
    Intent intent;
    String cheita;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creare_quiz);
        firebaseFirestore=FirebaseFirestore.getInstance();
        numeMaterie=getIntent().getStringExtra("numeMaterie");
        pozitieInLista=getIntent().getIntExtra("pozitieDeModificat",-1);
        adaugaIntrebarile=findViewById(R.id.butonCreeazaIntrebarile);
        cerinta=findViewById(R.id.edtiTextIntrebare);
        intent=getIntent();
        cheita=intent.getStringExtra("cheita");
        adaugaIntrebarile=findViewById(R.id.butonCreeazaIntrebarile);
        toggleButton1=findViewById(R.id.ToggleButonVarianta1);
        toggleButton2=findViewById(R.id.ToggleButonVarianta2);
        toggleButton3=findViewById(R.id.ToggleButonVarianta3);
        toggleButton4=findViewById(R.id.ToggleButonVarianta4);
        varianta1=findViewById(R.id.editTextVariantaRaspuns1);
        varianta2=findViewById(R.id.editTextVariantaRaspuns2);
        varianta3=findViewById(R.id.editTextVariantaRaspuns3);
        varianta4=findViewById(R.id.editTextVariantaRaspuns4);
        radioGroupToggleButtons=findViewById(R.id.radioGrupToggleVariantaCorecta);
        intrebareDeModificat= (Intrebare) getIntent().getSerializableExtra("intrebareDeModificat");
        if(intrebareDeModificat!=null){
            cerinta.setText(intrebareDeModificat.getCerinta());
            varianta1.setText(intrebareDeModificat.getVarianteRaspuns().get(0));
            varianta2.setText(intrebareDeModificat.getVarianteRaspuns().get(1));
            varianta3.setText(intrebareDeModificat.getVarianteRaspuns().get(2));
            varianta4.setText(intrebareDeModificat.getVarianteRaspuns().get(3));
            switch (intrebareDeModificat.getRaspunsCorect()){
                case 0:
                    toggleButton1.setChecked(true);
                    break;
                case 1:
                    toggleButton2.setChecked(true);
                    break;
                case 2:
                    toggleButton3.setChecked(true);
                    break;
                case 3:
                    toggleButton4.setChecked(true);
                    break;
            }


        }
        CompoundButton.OnCheckedChangeListener changeListener=new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(buttonView==toggleButton1){
                        toggleButton1.setChecked(true);
                        toggleButton2.setChecked(false);
                        toggleButton3.setChecked(false);
                        toggleButton4.setChecked(false);

                    }
                    if(buttonView==toggleButton2){
                        toggleButton1.setChecked(false);
                        toggleButton2.setChecked(true);
                        toggleButton3.setChecked(false);
                        toggleButton4.setChecked(false);
                    }
                    if(buttonView==toggleButton3){
                        toggleButton1.setChecked(false);
                        toggleButton2.setChecked(false);
                        toggleButton3.setChecked(true);
                        toggleButton4.setChecked(false);
                    }
                    if(buttonView==toggleButton4){
                        toggleButton1.setChecked(false);
                        toggleButton2.setChecked(false);
                        toggleButton3.setChecked(false);
                        toggleButton4.setChecked(true);
                    }

                }
            }
        };
        toggleButton1.setOnCheckedChangeListener(changeListener);
        toggleButton2.setOnCheckedChangeListener(changeListener);
        toggleButton3.setOnCheckedChangeListener(changeListener);
        toggleButton4.setOnCheckedChangeListener(changeListener);

        if(intrebareDeModificat==null){
            Toast.makeText(this, "Este ok", Toast.LENGTH_SHORT).show();
            adaugaIntrebarile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createIntreabre();
                }
            });

        }
        else if(cheita!=null){
            Toast.makeText(this, "cheita diferit de null", Toast.LENGTH_SHORT).show();
            adaugaIntrebarile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pozitieInLista>-1){
                        firebaseFirestore.collection("Intrebari").whereEqualTo("materie",numeMaterie).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    String id=task.getResult().getDocuments().get(pozitieInLista).getId();
                                    validareDateUpdateFirebase();

                                    firebaseFirestore.collection("Intrebari").document(id).update("cerinta",CERINTA,"raspunsCorect",RASPUNS_CORECT,"varianteRaspuns",variante).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Intent intent=getIntent();
                                           intent.putExtra("numeMaterie",numeMaterie);
                                           setResult(RESULT_OK,intent);
                                           finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }

                }
            });
        }



    }

    private void obtinereRaspunsCorect() {
        if(toggleButton1.isChecked()){
            RASPUNS_CORECT=0;

        }
        else if(toggleButton2.isChecked()){
            RASPUNS_CORECT=1;
        }
        else if(toggleButton3.isChecked()){
            RASPUNS_CORECT=2;
        }
        else{
            RASPUNS_CORECT=3;
        }
    }


    private void validareDate() {
        if (CERINTA.isEmpty()){
            cerinta.setError("Trebuie sa completezi campul");
            return;
        }
        if(VARIANTA1.isEmpty()){
            varianta1.setError("Trebuie sa completezi");
            return;
        }
        if(VARIANTA2.isEmpty()){
            varianta2.setError("Trebuie sa completezi");
            return;
        }
        if(VARIANTA3.isEmpty()){
            varianta3.setError("Trebuie sa completezi");
            return;
        }
        if(VARIANTA4.isEmpty()){
            varianta4.setError("Trebuie sa completezi");
            return;
        }
        obtinereRaspunsCorect();
        variante.clear();
        variante.add(VARIANTA1);
        variante.add(VARIANTA2);
        variante.add(VARIANTA3);
        variante.add(VARIANTA4);
       Intrebare intrebare=new Intrebare(CERINTA,variante,RASPUNS_CORECT,numeMaterie);
            firebaseFirestore.collection("Intrebari").document().set(intrebare);


        Intent intent=new Intent(getApplicationContext(),DashboardQuizActivity.class);
        intent.putExtra("numeMaterieQuizActivity",numeMaterie);
        startActivityForResult(intent,10);



    }
    private void createIntreabre(){
        CERINTA=cerinta.getText().toString();
        VARIANTA1=varianta1.getText().toString();
        VARIANTA2=varianta2.getText().toString();
        VARIANTA3=varianta3.getText().toString();
        VARIANTA4=varianta4.getText().toString();
        validareDate();

    }
    private  void clear(){
        cerinta.setText("");
        varianta1.setText("");
        varianta2.setText("");
        varianta3.setText("");
        varianta4.setText("");
        //toggleButton1,toggleButton2,toggleButton3,toggleButton4
        toggleButton1.setChecked(false);
        toggleButton2.setChecked(false);
        toggleButton3.setChecked(false);
        toggleButton4.setChecked(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode==RESULT_OK &&data!=null){
            clear();

        }
    }
    private void validareDateUpdateFirebase(){
        CERINTA=cerinta.getText().toString();
        VARIANTA1=varianta1.getText().toString();
        VARIANTA2=varianta2.getText().toString();
        VARIANTA3=varianta3.getText().toString();
        VARIANTA4=varianta4.getText().toString();
        if (CERINTA.isEmpty()){
            cerinta.setError("Trebuie sa completezi campul");
            return;
        }
        if(VARIANTA1.isEmpty()){
            varianta1.setError("Trebuie sa completezi");
            return;
        }
        if(VARIANTA2.isEmpty()){
            varianta2.setError("Trebuie sa completezi");
            return;
        }
        if(VARIANTA3.isEmpty()){
            varianta3.setError("Trebuie sa completezi");
            return;
        }
        if(VARIANTA4.isEmpty()){
            varianta4.setError("Trebuie sa completezi");
            return;
        }
        obtinereRaspunsCorect();
        variante.clear();
        variante.add(VARIANTA1);
        variante.add(VARIANTA2);
        variante.add(VARIANTA3);
        variante.add(VARIANTA4);

    }
}