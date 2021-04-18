package com.example.aplicatielicenta.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
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


import com.example.aplicatielicenta.R;
import com.example.aplicatielicenta.general.ChatAlegereGrupFragment;
import com.example.aplicatielicenta.general.ChatMainFragment;
import com.example.aplicatielicenta.logare.MainActivity;
import com.example.aplicatielicenta.profesor.ContPersonalProfesorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.ArrayList;
import java.util.List;

import utilitare.general.User;

public class ContPersonalActivity extends AppCompatActivity {
DrawerLayout drawerLayout;
Fragment fragmentulSelectat;
User user=new User();
TextView numeCont,email;
NavigationView navigationView;
View headerView;
StorageReference storageReference;
ImageView imagineProfil;
FirebaseFirestore firebaseFirestore;
FirebaseAuth firebaseAuth;
ChipNavigationBar bottomNavigationView;
String permisiuni[]=new String[2];
   static final int requestCode=1;
    static final int REQUEST_CODE=1111;

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
        bottomNavigationView=findViewById(R.id.bottom_navigation_view_chat);
        bottomNavigationView.setVisibility(View.GONE);
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference=FirebaseStorage.getInstance().getReference("pozeProfil");
        selectareFragmente();
        permisiuni[0]=Manifest.permission.CAMERA;
        permisiuni[1]=Manifest.permission.RECORD_AUDIO;
        headerView=navigationView.getHeaderView(0);
        numeCont=headerView.findViewById(R.id.mainPageTextViewNume);
        email=headerView.findViewById(R.id.mainPageTextViewEmail);
        imagineProfil=headerView.findViewById(R.id.imageViewImagineProfil);
        storageReference= FirebaseStorage.getInstance().getReference("pozeProfil");
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Studenti").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                            Toast.makeText(ContPersonalActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
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
                    fragmentulSelectat= ChatAlegereGrupFragment.newInstance(user);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentulSelectat).commit();
                }
                else if(bottomNavigationView.getSelectedItemId()==R.id.nav_bottom_private_messages){
                    fragmentulSelectat=PrivateChatFragment.newInstance(user);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentulSelectat).commit();

                }
                else if(bottomNavigationView.getSelectedItemId()==R.id.nav_bottom_calls){
                    fragmentulSelectat=AlegereParticipantApelFragment.newInstance(user);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentulSelectat).commit();
                }
            }
        });

    }

    private boolean isPermissionGranted(){
        for(int i=0;i<permisiuni.length;i++){
            if(ActivityCompat.checkSelfPermission(getApplicationContext(),permisiuni[i])!= PackageManager.PERMISSION_GRANTED){

            }
        }
        return true;
    }
    private void askPermissions() {
            ActivityCompat.requestPermissions(this,permisiuni,requestCode);

    }

    private void selectareFragmente() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               if(item.getItemId()==R.id.nav_timetable){
                   fragmentulSelectat= ScheduleFragment.newInstance(user);
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentulSelectat).commit();

                   bottomNavigationView.setVisibility(View.INVISIBLE);
                   drawerLayout.closeDrawer(GravityCompat.START);

               }
               else if(item.getItemId()==R.id.nav_chat_item){
                   fragmentulSelectat= ChatAlegereGrupFragment.newInstance(user);
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentulSelectat).commit();
                   drawerLayout.closeDrawer(GravityCompat.START);
                   bottomNavigationView.setVisibility(View.VISIBLE);
                   bottomNavigationView.setItemSelected(R.id.nav__bottom_group_messages,true);
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
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}