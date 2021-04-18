package com.example.aplicatielicenta.profesor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.aplicatielicenta.R;
import com.example.aplicatielicenta.general.ChatAlegereGrupFragment;
import com.example.aplicatielicenta.profesor.ScheduleFragmentProfesor;
import com.example.aplicatielicenta.student.PrivateChatFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import utilitare.general.User;

public class ContPersonalProfesorActivity extends AppCompatActivity {
DrawerLayout drawerLayout;
User user=new User();
TextView numeCont,email;
NavigationView navigationView;
View headerView;
FirebaseFirestore firebaseFirestore;
Fragment fragment;
FirebaseAuth firebaseAuth;
ImageView imagineProfil;
static final int REQUEST_CODE=245;
StorageReference storageReference;
ChipNavigationBar bottomNavigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cont_personal_profesor);
        initializare();
    }


    private void initializare() {
        Toolbar toolbar=findViewById(R.id.toolbarprofesor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        firebaseAuth=FirebaseAuth.getInstance();
        drawerLayout=findViewById(R.id.drawer_layout_profesor);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView=findViewById(R.id.nav_view_profesor);
        storageReference=FirebaseStorage.getInstance().getReference("pozeProfil");
        headerView=navigationView.getHeaderView(0);
        numeCont=headerView.findViewById(R.id.mainPageTextViewNume);
        email=headerView.findViewById(R.id.mainPageTextViewEmail);
        imagineProfil=headerView.findViewById(R.id.imageViewImagineProfil);
        firebaseFirestore=FirebaseFirestore.getInstance();
        bottomNavigationView=findViewById(R.id.bottom_navigation_view_chat_prof);
        bottomNavigationView.setVisibility(View.INVISIBLE);
        firebaseFirestore.collection("Profesori").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    user=documentSnapshot.toObject(User.class);
                    numeCont.setText(user.getNume());
                    email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    storageReference.child(user.getNume()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).fit().centerCrop().into(imagineProfil);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ContPersonalProfesorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


selectareFragment();

imagineProfil.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        storageReference.child(user.getNume()+".jpg").delete();
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE);
    }
});
        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if(bottomNavigationView.getSelectedItemId()==R.id.nav__bottom_group_messages){
                    fragment= ChatAlegereGrupFragmentProfesor.newInstance(user);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_profesor,fragment).commit();
                }
                else if(bottomNavigationView.getSelectedItemId()==R.id.nav_bottom_private_messages){
                    fragment=PrivateChatFragmentProfesor.newInstance(user);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_profesor,fragment).commit();



                }
            }
        });

    }

    private void selectareFragment() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.nav_timetable_profesor){
                    fragment= ScheduleFragmentProfesor.newInstance(user);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_profesor,fragment).commit();
                    bottomNavigationView.setVisibility(View.INVISIBLE);
                    drawerLayout.closeDrawer(GravityCompat.START);

                }
                else if(item.getItemId()==R.id.nav_chat_item_profesor){
                    fragment= ChatAlegereGrupFragmentProfesor.newInstance(user);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_profesor,fragment).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
                else if(item.getItemId()==R.id.nav_create_quiz){
                    fragment=AlegeMaterieQuizFragment.newInstance(user);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_profesor,fragment).commit();
                    bottomNavigationView.setVisibility(View.INVISIBLE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.old_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutAplicatie){
            firebaseAuth.signOut();


        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            Uri imageUri=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(imageUri);
                Bitmap imagineSelectata= BitmapFactory.decodeStream(inputStream);
                imagineProfil.setImageBitmap(imagineSelectata);
                uploadPoza(imageUri);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadPoza(Uri uri) {
        StorageReference referinta=storageReference.child(user.getNume()+"."+getFileExtension(uri));
        referinta.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ContPersonalProfesorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}