package com.example.aplicatielicenta.logare;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.aplicatielicenta.profesor.ContPersonalProfesorActivity;
import com.example.aplicatielicenta.student.ContPersonalActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CheckUser extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseUser !=null){
                    String email=firebaseUser.getEmail();
                    if(email.contains("@stud.ase.ro")){
                        Intent intent=new Intent(getApplicationContext(), ContPersonalActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else if(email.contains("@csie.ase.ro")){
                        Intent intent=new Intent(getApplicationContext(), ContPersonalProfesorActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }


                }
                else{
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}
