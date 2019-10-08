package com.example.blackeagle.joker;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Anamenu extends AppCompatActivity {
    ImageButton profilbtn,sahiplenbtn,kayipbtn,cikisbtn,vetbtn,chatbtn;
    DatabaseReference dref,msgref;
    FirebaseUser cuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamenu);
        vetbtn = findViewById(R.id.btnvetbul);
        profilbtn = findViewById(R.id.btnprofil);
        sahiplenbtn = findViewById(R.id.btnsahiplen);
        kayipbtn = findViewById(R.id.btnkayip);
        cikisbtn = findViewById(R.id.btncikis);
        chatbtn = findViewById(R.id.chatbutton);
        cuser = FirebaseAuth.getInstance().getCurrentUser();
        dref = FirebaseDatabase.getInstance().getReference().child("Messages").child(cuser.getUid());




        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ChatList.class));
            }
        });

        vetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vetbul();
            }
        });
    }
    public void vetbul()
    {
        startActivity(new Intent(getApplicationContext(),Veterinerler.class));

    }
    public void cikis(View view){

        FirebaseAuth.getInstance().signOut();
        finish();
        Toast.makeText(getApplicationContext(),"Çıkış Yapıldı",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,Login.class));

    }
    public void profil(View view){
        startActivity(new Intent(this,Profil.class));

    }
    public void kayipilanları(View view){
        startActivity(new Intent(this,Kayiplar.class));
    }
    public void sahiplen(View view){
        startActivity(new Intent(this,Sahiplenme.class));
    }
}
