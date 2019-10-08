package com.example.blackeagle.joker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditProfile extends AppCompatActivity {

    public Button mButtonUpdate,mButtonGeridon;
    private ProgressBar mProgessBar;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser currentuser;
    Profil p;
    private EditText isimedt, phoneedt, yenisifre;
    private TextView txt;
    Login login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        mButtonGeridon = findViewById(R.id.geridon);
        isimedt = findViewById(R.id.isimedit);
        yenisifre = findViewById(R.id.yenisifre);
        phoneedt = findViewById(R.id.phone_edit);
        mButtonUpdate = findViewById(R.id.updatebtn);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        login = new Login();


        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mProgessBar.setVisibility(View.VISIBLE);
                updateuser();
                startActivity(new Intent(EditProfile.this,Profil.class));
               // mProgessBar.setVisibility(View.GONE);
            }
        });
        mButtonGeridon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfile.this,Profil.class));
            }
        });


    }

    private void updateuser() {
        if (!isimedt.getText().toString().isEmpty()) {
            mDatabaseRef.child("Users").child(currentuser.getUid()).child("name").setValue(isimedt.getText().toString());

            if(!mDatabaseRef.child("Veterinerler").child(currentuser.getUid()).toString().equals(null))
            {
                mDatabaseRef.child("Veterinerler").child(currentuser.getUid()).child("name").setValue(isimedt.getText().toString());
            }
            else{

            }


            Toast.makeText(getApplicationContext(), "Değişiklikler Kaydedildi.", Toast.LENGTH_LONG).show();
        }
        else{

        }


        if (!phoneedt.getText().toString().isEmpty()) {

            if (phoneedt.getText().toString().length() != 10) {
                phoneedt.setError("Telefon Numarası 10 haneli olmalı!");

            } else {
                mDatabaseRef.child("Users").child(currentuser.getUid()).child("phone").setValue(phoneedt.getText().toString());
                if(!mDatabaseRef.child("Veterinerler").child(currentuser.getUid()).toString().equals(null))
                {
                    mDatabaseRef.child("Veterinerler").child(currentuser.getUid()).child("phone").setValue(phoneedt.getText().toString());
                }
                else{

                }
                Toast.makeText(getApplicationContext(), "Değişiklikler Kaydedildi.", Toast.LENGTH_LONG).show();
            }

            } else
                mDatabaseRef.child("Users").child(currentuser.getUid()).child("phone").setValue(phoneedt.getText().toString());
            Toast.makeText(getApplicationContext(), "Değişiklikler Kaydedildi.", Toast.LENGTH_LONG).show();






// Prompt the user to re-provide their sign-in credentials

        if(!yenisifre.getText().toString().isEmpty()) {
            currentuser.updatePassword(yenisifre.getText().toString().trim()).
                    addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfile.this, "Şifreniz başarıyla güncellendi!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditProfile.this, "Şifre güncelleme işlemi gerçekleşemedi!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }



        }

}




