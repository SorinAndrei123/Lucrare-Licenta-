package com.example.aplicatielicenta.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.dpro.widgets.WeekdaysPicker;
import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.michaldrabik.classicmaterialtimepicker.CmtpDateDialogFragment;
import com.michaldrabik.classicmaterialtimepicker.CmtpTimeDialogFragment;
import com.michaldrabik.classicmaterialtimepicker.OnTime12PickedListener;
import com.michaldrabik.classicmaterialtimepicker.OnTime24PickedListener;
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12;
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import utilitare.general.ChatMessage;
import utilitare.general.Materie;
import utilitare.general.User;

public class AdminActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    EditText numeMaterie,an,time,serie,grupa;
    RadioGroup tipMaterie;
    Spinner spinner;
    List<String>listaProfesori=new ArrayList<>();
    ArrayAdapter adapter;
    String ora;
    String minute;
    WeekdaysPicker weekdaysPicker;
    Button buttonAdd;
    Materie materie;
    String ziuaSaptamaniiString;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        firebaseFirestore=FirebaseFirestore.getInstance();
        numeMaterie=findViewById(R.id.editTextAddNumeMaterie);
        an=findViewById(R.id.editTextAddAn);
        time=findViewById(R.id.editTextAddTime);
        serie=findViewById(R.id.editTextAddSerie);
        grupa=findViewById(R.id.editTextAddGrupa);
        tipMaterie=findViewById(R.id.radioGrupAddMaterie);
        spinner=findViewById(R.id.spinnerAddProfesor);
        weekdaysPicker=findViewById(R.id.dayPicker);
        List<Integer> days = Arrays.asList();
        weekdaysPicker.setSelectedDays(days);
        buttonAdd=findViewById(R.id.buttonAddMaterie);
        weekdaysPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminActivity.this, weekdaysPicker.getSelectedDaysText().get(0), Toast.LENGTH_SHORT).show();
            }
        });
        firebaseFirestore.collection("Profesori").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot:task.getResult()){
                        User user=documentSnapshot.toObject(User.class);
                        listaProfesori.add(user.getNume());
                    }

                    adapter=new ArrayAdapter(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,listaProfesori);
                    spinner.setAdapter(adapter);
                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CmtpTimeDialogFragment timeDialogFragment=CmtpTimeDialogFragment.newInstance();
                            timeDialogFragment.setInitialTime24(5, 15);
                            timeDialogFragment.show(getSupportFragmentManager(),"Tag");
                            timeDialogFragment.setOnTime24PickedListener(new OnTime24PickedListener() {
                                @Override
                                public void onTimePicked(@NonNull @NotNull CmtpTime24 cmtpTime24) {
                                    time.setText("Ora "+String.valueOf(cmtpTime24.component1())+":"+String.valueOf(cmtpTime24.component2()));
                                    ora=String.valueOf(cmtpTime24.component1());
                                    minute=String.valueOf(cmtpTime24.component2());

                                }
                            });


                        }


                    });
                    tipMaterie.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if(checkedId==R.id.radioButtonCurs){
                                serie.setVisibility(View.VISIBLE);
                                grupa.setVisibility(View.INVISIBLE);
                            }
                            else
                                if(checkedId==R.id.radioButtonSeminar){
                                    serie.setVisibility(View.INVISIBLE);
                                    grupa.setVisibility(View.VISIBLE);
                                }
                        }
                    });



                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(numeMaterie.getText().toString().isEmpty()){
                                numeMaterie.setError("Trebuie sa completezi acest camp");
                                return;
                            }
                            if(an.getText().toString().isEmpty()){
                                an.setError("Trebuie sa completezi acest camp");
                                return;
                            }

                            if(time.getText().toString().isEmpty()){
                                time.setError("Trebuie sa completezi acest camp");
                                return;
                            }
                            int checkedid=tipMaterie.getCheckedRadioButtonId();
                            if(checkedid==R.id.radioButtonCurs){
                                try {
                                    materie=new Materie(numeMaterie.getText().toString(),spinner.getSelectedItem().toString(),"curs",getZiuFromPicker(),ora,"grupa test",serie.getText().toString(),an.getText().toString());
                                 FirebaseDatabase.getInstance().getReference("Mesaje").child(materie.getNume()+materie.getSerie()).push().setValue(new ChatMessage("Buna ziua","Preda Sorin"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                try {
                                    materie=new Materie(numeMaterie.getText().toString(),spinner.getSelectedItem().toString(),"seminar",getZiuFromPicker(),ora,grupa.getText().toString(),an.getText().toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            firebaseFirestore.collection("Materii").add(materie).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(AdminActivity.this, "Adaugat cu succes", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    });





                }

            }
        });
    }
    String getZiuFromPicker() throws Exception {
        switch (weekdaysPicker.getSelectedDaysText().get(0)){
            case "Monday":
                return "luni";
            case "Tuesday":
                return "marti";
            case "Wednesday":
                return "miercuri";
            case "Thursday":
                return "joi";
            case "Friday":return "vineri";
            default:
                throw new Exception();
        }
    }
}