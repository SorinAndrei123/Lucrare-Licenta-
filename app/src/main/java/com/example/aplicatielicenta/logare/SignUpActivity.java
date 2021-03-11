package com.example.aplicatielicenta.logare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import utilitare.general.User;

public class SignUpActivity extends AppCompatActivity {
    EditText nume, email, parola;
    TextView tvGrupa;
    Button inregistrare;
    ProgressBar progressBar;
    RadioGroup radioGroup, tipCont;
    Spinner spinner;
    FirebaseAuth auth;
    List<String> grupe = new ArrayList<>();
    ArrayAdapter<String> adaptor;
    FirebaseFirestore firebaseFirestore;
    User user=new User();
    int an;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //database=FirebaseDatabase.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        nume=findViewById(R.id.editTextNumeSignUp);
        tvGrupa=findViewById(R.id.textViewGrupa);
        email=findViewById(R.id.editTextMailSignUp);
        parola=findViewById(R.id.editTextParolaSignUp);
        progressBar=findViewById(R.id.progressBarSignUp);
        radioGroup=findViewById(R.id.radioGrupAn);
        tipCont=findViewById(R.id.radioGrupTipCont);
        spinner=findViewById(R.id.spinnerGrupa);
        auth=FirebaseAuth.getInstance();
        inregistrare=findViewById(R.id.butonSignUp);
        tipCont.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButonStudent){
                    radioGroup.setVisibility(View.VISIBLE);
                    tvGrupa.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                }
                else if(checkedId==R.id.radioButonProfesor){
                    radioGroup.setVisibility(View.INVISIBLE);
                    tvGrupa.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButonAn1){
                    grupe.clear();
                    grupe.add("1005");
                    grupe.add("1006");
                    grupe.add("1007");
                    grupe.add("1008");
                    grupe.add("1009");
                    adaptor=new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,grupe);
                    spinner.setAdapter(adaptor);



                }
                else if(checkedId==R.id.radioButonAn2){
                    grupe.clear();
                    grupe.add("1041");
                    grupe.add("1042");
                    grupe.add("1043");
                    grupe.add("1044");
                    grupe.add("1045");
                    grupe.add("1046");
                    grupe.add("1047");
                    adaptor=new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,grupe);
                    spinner.setAdapter(adaptor);

                }
                else if(checkedId==R.id.radioButonAn3){
                    grupe.clear();
                    grupe.add("1072");
                    grupe.add("1073");
                    grupe.add("1074");
                    grupe.add("1075");
                    grupe.add("1076");
                    grupe.add("1077");
                    grupe.add("1078");
                    adaptor=new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,grupe);
                    spinner.setAdapter(adaptor);

                }
            }
        });


        inregistrare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NUME=nume.getText().toString();
                String EMAIL=email.getText().toString().trim();
                String PAROLA=parola.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                if(NUME.isEmpty()){
                    nume.setError("please provide your full name");
                    return;
                }
                if(EMAIL.length()==0){
                    email.setError("Please provide an email adress");
                    return;
                }
                if(EMAIL.contains("@stud.ase.ro") ||EMAIL.contains("@csie.ase.ro") ){

                }
                else{
                    email.setError("please provide an institutional email adress");
                    return;
                }
                if(PAROLA.length()<6){
                    parola.setError("please provide a more secure password.>6 characters");
                    return;
                }

                if(EMAIL.contains("@csie.ase.ro")){
                   user.setNume(NUME);
                   user.setTipCont("Profesori");
                }
                if(EMAIL.contains("@stud.ase.ro")){
                    user.setNume(NUME);
                    user.setTipCont("Studenti");
                    if(radioGroup.getCheckedRadioButtonId()==R.id.radioButonAn1){
                        user.setAn(1);
                    }
                    else if(radioGroup.getCheckedRadioButtonId()==R.id.radioButonAn2){
                        user.setAn(2);
                    }
                    else if(radioGroup.getCheckedRadioButtonId()==R.id.radioButonAn3){
                        user.setAn(3);
                    }
                    user.setGrupa(Integer.parseInt(spinner.getSelectedItem().toString().trim()));
                    user.setSerie("C");
                }


               


                    auth.createUserWithEmailAndPassword(EMAIL,PAROLA).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                firebaseFirestore.collection(user.getTipCont()).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user);
                                progressBar.setVisibility(View.INVISIBLE);

                                Toast.makeText(SignUpActivity.this, "Adaugare realizata cu succes", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(SignUpActivity.this, "Ai eroare menutule", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });





            }
        });


    }
}