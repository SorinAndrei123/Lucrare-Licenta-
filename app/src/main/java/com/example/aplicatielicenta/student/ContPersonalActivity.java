package com.example.aplicatielicenta.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import utilitare.general.User;

public class ContPersonalActivity extends AppCompatActivity {
DrawerLayout drawerLayout;
Fragment fragmentulSelectat;
User user=new User();
TextView numeCont,email;
NavigationView navigationView;
View headerView;
FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cont_personal);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView=findViewById(R.id.nav_view);
        selectareFragmente();
        headerView=navigationView.getHeaderView(0);
        numeCont=headerView.findViewById(R.id.mainPageTextViewNume);
        email=headerView.findViewById(R.id.mainPageTextViewEmail);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Studenti").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                user=documentSnapshot.toObject(User.class);
                numeCont.setText(user.getNume());
                email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                }
            }
        });

    }

    private void selectareFragmente() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               if(item.getItemId()==R.id.nav_timetable){
                   fragmentulSelectat= ScheduleFragment.newInstance(user);
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentulSelectat).commit();
                   drawerLayout.closeDrawer(GravityCompat.START);
               }
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }


    }
}