package com.example.blackeagle.joker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class PPYukle extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView ppyukle;
    private Button vazgec,yukle;
    private Uri mImageUri;
    FirebaseUser currentuser;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef,mref;
    String id,photourl,ktipi,normalid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppyukle);
        ppyukle = findViewById(R.id.petpp);
        vazgec = findViewById(R.id.btncancel);
        yukle = findViewById(R.id.btnonay);
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference(currentuser.getUid());
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        mref = FirebaseDatabase.getInstance().getReference("Veterinerler");
        Intent i = getIntent();
         mDatabaseRef.child(currentuser.getUid()).child("tip").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ktipi = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        normalid = i.getStringExtra("normal");


        id = currentuser.getUid().toString();

        vazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PPYukle.this,Profil.class));
            }
        });

        ppyukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        yukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();

            }
        });
    }
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(ppyukle);
        }
    }
    private void uploadFile(){
        if(mImageUri !=null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Değişiklikler Kaydediliyor...");
            progressDialog.show();

            StorageReference riversRef = mStorageRef.child("images/profile.jpg");

            riversRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            photourl = taskSnapshot.getDownloadUrl().toString();

                            if(ktipi.equals("Veteriner")){
                                mref.child(currentuser.getUid()).child("photourl").setValue(photourl);

                            }else {
                                mDatabaseRef.child(currentuser.getUid()).child("photourl").setValue(photourl);
                            }






                            Toast.makeText(PPYukle.this, "Başarıyla tamamlandı...", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Profil.class));

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            progressDialog.dismiss();
                            Toast.makeText(PPYukle.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage(((int)progress)+"% yüklendi...");
                        }
                    });
        }
        else{
            Toast.makeText(this, "No image is selected.", Toast.LENGTH_SHORT).show();
        }

    }

}
