package com.example.aplicatielicenta.logare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicatielicenta.profesor.ContPersonalProfesorActivity;
import com.example.aplicatielicenta.R;
import com.example.aplicatielicenta.student.ContPersonalActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText mail, parola;
    Button logare;
    TextView resetareParola, creareCont;
    FirebaseAuth auth;
    String tipCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializare();
    }



    private void initializare() {
        mail = findViewById(R.id.editTextMailLogin);
        parola = findViewById(R.id.editTextParolaLogin);
        creareCont = findViewById(R.id.textViewCreareCont);
        auth = FirebaseAuth.getInstance();
        creareCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        resetareParola = findViewById(R.id.textViewResetareParola);
        resetareParola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ResetareParolaActivity.class);
                startActivity(intent);

            }
        });
        logare = findViewById(R.id.butonLogin);
        logare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Mail = mail.getText().toString();
                String Parola = parola.getText().toString();
                if (Mail.isEmpty()) {
                    mail.setError("Please provide an email");
                    return;
                }

                if (Parola.length() < 6) {
                    parola.setError("please provide a more secure password.>6 characters");
                    return;
                }
                if(Mail.contains("@stud.ase.ro")){
                    tipCont="Studenti";
                }
                if(Mail.contains("@csie.ase.ro")){
                    tipCont="Profesori";
                }
                auth.signInWithEmailAndPassword(Mail,Parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(tipCont.equals("Studenti")){
                                Intent intent=new Intent(getApplicationContext(), ContPersonalActivity.class);
                                startActivity(intent);
                            }
                            else if(tipCont.equals("Profesori")){
                                Intent intent=new Intent(getApplicationContext(), ContPersonalProfesorActivity.class);
                                startActivity(intent);
                            }



                        }
                        else{
                            Toast.makeText(MainActivity.this, "Nu ai introdus bine parola/emailul", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


}