package com.example.aplicatielicenta.profesor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import utilitare.general.Sarcina;

public class CreareTaskProfesorActivity extends AppCompatActivity {
    EditText descriereaCerintei,dataDeadline,oraDeadline,titlu;
    TextView textView;
    Calendar calendar;
    RadioGroup tipFisierSuportat;
    Spinner saptamanaDeLucru;
    List<String>saptamani=new ArrayList<>();
    String codMaterie;
    Button creareTaskNou;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creare_task_profesor);
        descriereaCerintei=findViewById(R.id.editTextDescriereTask);
        creareTaskNou=findViewById(R.id.buttonCreareTaskNou);
        firebaseFirestore=FirebaseFirestore.getInstance();
        dataDeadline=findViewById(R.id.editTextDataDeadline);
        titlu=findViewById(R.id.editTextTitluTask);
        codMaterie=getIntent().getExtras().getString("cod materie");
        calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();

            }
        };
        dataDeadline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DatePickerDialog(CreareTaskProfesorActivity.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        oraDeadline=findViewById(R.id.editTextOraDeadline);
        oraDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int ora=calendar.get(Calendar.HOUR_OF_DAY);
                int minut=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                timePickerDialog=new TimePickerDialog(CreareTaskProfesorActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        oraDeadline.setText(""+String.valueOf(hourOfDay)+":"+String.valueOf(minut));

                    }
                },ora,minut,true);
                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();
            }
        });
        tipFisierSuportat=findViewById(R.id.optiuneTipFisierDorit);
        saptamanaDeLucru=findViewById(R.id.spinnerSaptamanaDeLucru);
        saptamani.add("1");
        saptamani.add("2");
        saptamani.add("3");
        saptamani.add("4");
        saptamani.add("5");
        saptamani.add("6");
        saptamani.add("7");
        saptamani.add("8");
        saptamani.add("9");
        saptamani.add("10");
        saptamani.add("11");
        saptamani.add("12");
        saptamani.add("13");
        saptamani.add("14");
        ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,saptamani);
        saptamanaDeLucru.setAdapter(adapter);
        textView=findViewById(R.id.textViewTipFisierSuportat);
        creareTaskNou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(descriereaCerintei.getText().toString().isEmpty()){
                    descriereaCerintei.setError("Campul nu poate fi gol");
                    return;
                }
                if(dataDeadline.getText().toString().isEmpty()){
                    dataDeadline.setError("Campul nu poate fi gol");
                    return;
                }
                if(oraDeadline.getText().toString().isEmpty()){
                    oraDeadline.setError("Campul nu poate fi gol");
                    return;
                }
                if(tipFisierSuportat.getCheckedRadioButtonId()==-1){
                    textView.setError("Trebuie sa selectezi un tip de fisier ");
                    return;

                }
                if(titlu.getText().toString().isEmpty()){
                    titlu.setError("Campul nu poate fi gol");
                    return;
                }
                Sarcina sarcina=new Sarcina();
                sarcina.setIdMaterie(codMaterie);
                sarcina.setDataDeadline(dataDeadline.getText().toString().trim());
                sarcina.setOraDeadline(oraDeadline.getText().toString().trim());
                sarcina.setDescriere(descriereaCerintei.getText().toString());
                sarcina.setTitlu(titlu.getText().toString());
                sarcina.setTipFisierDorit(fisierSuportat());
                sarcina.setNrSaptDeadline(saptamanaDeLucru.getSelectedItem().toString());
                Intent intent=getIntent();
                firebaseFirestore.collection("Sarcini").add(sarcina).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                       // Toast.makeText(CreareTaskProfesorActivity.this, "Adaugare cu succes", Toast.LENGTH_SHORT).show();
                        intent.putExtra("codTask",documentReference.getId());
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });

            }
        });




    }


    private void updateLabel() {
        String formatData="dd/MM";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(formatData, Locale.ENGLISH);
        dataDeadline.setText(simpleDateFormat.format(calendar.getTime()));

    }

    private String fisierSuportat(){
        switch (tipFisierSuportat.getCheckedRadioButtonId()){
            case R.id.pdf:
                return "pdf";
            case R.id.word:
                return "word";
            case R.id.txt:
                return "txt";
            case R.id.RAR:
                return "rar";
            default:
                return "default";

        }
    }
}