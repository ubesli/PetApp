package com.example.blackeagle.joker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class Dostlarim extends AppCompatActivity {


    private FirebaseUser mUser;
    private Button petekle,geridon;
    private ArrayList<String> petnameFromfb;
    private ArrayList<String> petcinsFromfb;
    private ArrayList<String> petturFromfb;
    private ArrayList<String> petcinsiyetFromfb;
    private ArrayList<String> petdtFromfb;
    private ArrayList<String> petasilarFromfb;
    private ArrayList<String> petimageFromfb;
    private ArrayList<String> petsehirFromfb;
    private ArrayList<String> petidFromfb;
    private ListView liste;
    DatabaseReference newRef;
    StorageReference sRef;
    PetBilgileri adapter;
    static String selectedimageurl;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dostlarim);
        liste = findViewById(R.id.liste);
        petekle = findViewById(R.id.gopetyukle);
        geridon = findViewById(R.id.geridon);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        newRef = FirebaseDatabase.getInstance().getReference("Pets").child(mUser.getUid());

        TextView tx = findViewById(R.id.txtview2);
        liste.setEmptyView(tx);






        petekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentt = new Intent(Dostlarim.this, DostEkle.class);
                startActivity(intentt);
            }
        });
        geridon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dostlarim.this, Profil.class));
            }
        });
//burada kaldım

        petnameFromfb = new ArrayList<String>();
        petsehirFromfb = new ArrayList<String>();
        petcinsFromfb = new ArrayList<String>();
        petcinsiyetFromfb = new ArrayList<String>();
        petturFromfb = new ArrayList<String>();
        petdtFromfb = new ArrayList<String>();
        petasilarFromfb = new ArrayList<String>();
        petimageFromfb = new ArrayList<String>();
        petidFromfb = new ArrayList<String>();
        //mydbref = firebaseDatabase.getReference();

        adapter = new PetBilgileri(petnameFromfb,petcinsFromfb,petturFromfb,petcinsiyetFromfb,petdtFromfb,petasilarFromfb,petimageFromfb,petsehirFromfb,this);
        liste.setAdapter(adapter);
       getData();
       liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Intent intenttt = new Intent(getApplicationContext(),Pet_ilan.class);
              intenttt.putExtra("name",petnameFromfb.get(i));
              intenttt.putExtra("cins",petcinsFromfb.get(i));
               intenttt.putExtra("cinsiyeti",petcinsiyetFromfb.get(i));
               intenttt.putExtra("türü",petturFromfb.get(i));
               intenttt.putExtra("dogumtarihi",petdtFromfb.get(i));
               intenttt.putExtra("asilar",petasilarFromfb.get(i));
               intenttt.putExtra("sehir",petsehirFromfb.get(i));
               intenttt.putExtra("pid",petidFromfb.get(i));
               selectedimageurl = petimageFromfb.get(i);
              startActivity(intenttt);
           }
       });
    }
    private void getData(){


        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    HashMap<String,String> hashMap = (HashMap<String,String>) ds.getValue();
                    petnameFromfb.add(hashMap.get("petName"));
                    petcinsFromfb.add(hashMap.get("hayvan"));
                    petcinsiyetFromfb.add(hashMap.get("petCinsiyet"));
                    petturFromfb.add(hashMap.get("petSpecy"));
                    petdtFromfb.add(hashMap.get("petDogumTarihi"));
                    petasilarFromfb.add(hashMap.get("petYapilanAsilar"));
                    petsehirFromfb.add(hashMap.get("sehir"));
                    petimageFromfb.add(hashMap.get("photodownloadurl"));
                    petidFromfb.add(hashMap.get("petid"));



                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


    }

    }
