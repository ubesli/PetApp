package com.example.blackeagle.joker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VetGoruntule extends AppCompatActivity {

    ListView vetlistview;
    List<String> list;
    ArrayAdapter<String> adapter;
    CircleImageView vetphoto;
    String vetname,vetemail,vetphone,vetsehir,vetcinsiyet,vetphotourl,vetid,currentuserid;
    Button btngeri,btnmesaj;
    DatabaseReference dref;
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_goruntule);
        vetlistview = findViewById(R.id.vetlistview);
        list = new ArrayList<>();
        vetphoto = findViewById(R.id.vetphoto);
        btngeri = findViewById(R.id.geriibtnn);
        btnmesaj = findViewById(R.id.vetmsjbtn);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
       // dref = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        currentuserid = fuser.getUid().toString();

        Intent i = getIntent();

        vetname = i.getStringExtra("name");
        vetemail = i.getStringExtra("email");
        vetphotourl = Veterinerler.selectedimage;
        vetsehir = i.getStringExtra("sehir");
        vetid = i.getStringExtra("uniqid");
        vetphone = i.getStringExtra("phone");
        vetcinsiyet = i.getStringExtra("cinsiyeti");

        list.clear();
        list.add("\n\n  İsim : "+vetname + "\n\n  "+vetemail+"\n\n  "+vetphone+"\n\n  "+vetsehir+"\n\n  "+ vetcinsiyet);
        adapter = new ArrayAdapter<>(VetGoruntule.this,android.R.layout.simple_list_item_1,list);
        vetlistview.setAdapter(adapter);
        Picasso.get().load(vetphotourl).fit().centerCrop().into(vetphoto);

        btngeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Veterinerler.class));
            }
        });

        if(vetid.equals(currentuserid)){
            btnmesaj.setEnabled(false);
            btnmesaj.setClickable(false);
        btnmesaj.setVisibility(View.GONE);
        }
        else {
            btnmesaj.setEnabled(true);
            btnmesaj.setClickable(true);
        }
            btnmesaj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getApplicationContext(), ChatActivity.class);
                    in.putExtra("visiteduserid", vetid);
                    in.putExtra("isim", vetname);
                    startActivity(in);
                }
            });


    }
}
