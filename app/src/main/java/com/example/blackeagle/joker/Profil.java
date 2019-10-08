package com.example.blackeagle.joker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Profil extends AppCompatActivity {

    Button editbtn;
    Button gotopetpage,anasayfadon;
    ImageView profilphoto;
    ListView listView;
    FirebaseAuth mAuth;
    DatabaseReference dref;
    FirebaseStorage storage;
    StorageReference storageref;
    FirebaseUser currentuser;
    List<String> itemlist;
    ArrayAdapter<String> adapter;
    static String i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        anasayfadon = findViewById(R.id.btnanasayfa);
        editbtn = findViewById(R.id.editbtn);
        gotopetpage = findViewById(R.id.petpagebtn);
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentuser.getUid();
        itemlist = new ArrayList<>();
        profilphoto = findViewById(R.id.pphoto);
        listView = findViewById(R.id.listviewprofilgor);
        storageref = FirebaseStorage.getInstance().getReference(currentuser.getUid());

        anasayfadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Anamenu.class));

            }
        });


        profilphoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent iii = new Intent(Profil.this,PPYukle.class);

                startActivity(iii);
            }
        });





        editbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profil.this, EditProfile.class));
            }
        });


        dref = FirebaseDatabase.getInstance().getReference();

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Kullanıcı bilgilerini databaseden alıyoruz.
                itemlist.clear();
                String isim = dataSnapshot.child("Users").child(uid).child("name").getValue(String.class);
                String email = dataSnapshot.child("Users").child(uid).child("email").getValue(String.class);
                String telefon = dataSnapshot.child("Users").child(uid).child("phone").getValue(String.class);
                String cinsiyet = dataSnapshot.child("Users").child(uid).child("cinsiyet").getValue(String.class);
                String ktipi = dataSnapshot.child("Users").child(uid).child("tip").getValue(String.class);


                itemlist.add("\n\nİsim :" + isim + "\n\nE-Mail :" + email + "\n\nTelefon :" + telefon + "\n\nCinsiyet :" + cinsiyet + "\n\nKullanıcı Tipi :" + ktipi);
              /*  itemlist.add("E-Mail :"+email);
                itemlist.add(telefon);
                itemlist.add(cinsiyet);
                itemlist.add(ktipi);*/

                adapter = new ArrayAdapter<>(Profil.this, android.R.layout.simple_list_item_1, itemlist);
                listView.setAdapter(adapter);
               //Profil resmi storagedan imageviewa yüklüyoruz.
                storageref.child("images").child("profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //progressDialog.dismiss();
                        Picasso.get().load(uri).fit().centerCrop().into(profilphoto);



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(Profil.this, "Bir Hata Meydana Geldi!", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });

                gotopetpage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent it = new Intent(Profil.this,Dostlarim.class);
                startActivity(it);
            }
        });


    }
}