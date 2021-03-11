package com.example.aplicatielicenta.profesor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.aplicatielicenta.R;
import com.example.aplicatielicenta.profesor.ScheduleFragmentProfesor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import utilitare.general.User;

public class ContPersonalProfesorActivity extends AppCompatActivity {
DrawerLayout drawerLayout;
User user=new User();
TextView numeCont,email;
NavigationView navigationView;
View headerView;
FirebaseFirestore firebaseFirestore;
Fragment fragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cont_personal_profesor);
        initializare();
    }

    private void initializare() {
        Toolbar toolbar=findViewById(R.id.toolbarprofesor);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer_layout_profesor);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView=findViewById(R.id.nav_view_profesor);
        headerView=navigationView.getHeaderView(0);
        numeCont=headerView.findViewById(R.id.mainPageTextViewNume);
        email=headerView.findViewById(R.id.mainPageTextViewEmail);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Profesori").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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


selectareFragment();


    }

    private void selectareFragment() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.nav_timetable_profesor){
                    fragment= ScheduleFragmentProfesor.newInstance(user);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_profesor,fragment).commit();
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