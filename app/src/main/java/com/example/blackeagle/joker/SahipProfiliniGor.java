package com.example.blackeagle.joker;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SahipProfiliniGor extends AppCompatActivity {

    String id,isim;
    ListView lview;
    CircleImageView photo;
    DatabaseReference databaseReference;
    Button sohbetet,geri;
    StorageReference storageref;
    FirebaseUser muser;
    List<String> itemlist;
    ArrayAdapter<String> adapter;
  //  static String selectedpp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sahip_profilini_gor);
        lview = findViewById(R.id.listviewprofilgor);
        itemlist = new ArrayList<>();
        photo = findViewById(R.id.pphoto);
        sohbetet = findViewById(R.id.msjat);
        geri = findViewById(R.id.btngeri);
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Anamenu.class));
            }
        });

        Intent inte = getIntent();
        id = inte.getStringExtra("uid");

        storageref = FirebaseStorage.getInstance().getReference(id);

        databaseReference = FirebaseDatabase.getInstance().getReference();




            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Kullanıcı bilgilerini databaseden alıyoruz.
                    itemlist.clear();
                     isim = dataSnapshot.child("Users").child(id).child("name").getValue(String.class);
                    String email = dataSnapshot.child("Users").child(id).child("email").getValue(String.class);
                    String telefon = dataSnapshot.child("Users").child(id).child("phone").getValue(String.class);
                    String cinsiyet = dataSnapshot.child("Users").child(id).child("cinsiyet").getValue(String.class);
                    String ktipi = dataSnapshot.child("Users").child(id).child("tip").getValue(String.class);


                    itemlist.add("\n\nİsim :" + isim + "\n\nE-Mail :" + email + "\n\nTelefon :" + telefon + "\n\nCinsiyet :" + cinsiyet + "\n\nKullanıcı Tipi :" + ktipi);
              /*  itemlist.add("E-Mail :"+email);
                itemlist.add(telefon);
                itemlist.add(cinsiyet);
                itemlist.add(ktipi);*/

                  adapter = new ArrayAdapter<>(SahipProfiliniGor.this, android.R.layout.simple_list_item_1, itemlist);
                    lview.setAdapter(adapter);
                    //Profil resmi storagedan imageviewa yüklüyoruz.
                    storageref.child("images").child("profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //progressDialog.dismiss();
                            Picasso.get().load(uri).fit().centerCrop().into(photo);


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


                }
            });

         //   selectedpp = storageref.child("images").child("profile.jpg").getDownloadUrl().toString();


        sohbetet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatintent = new Intent(getApplicationContext(),ChatActivity.class);
                chatintent.putExtra("visiteduserid",id);
                chatintent.putExtra("nereden","fromsahip");
                chatintent.putExtra("isim",isim);
                startActivity(chatintent);

            }
        });




    }
}
