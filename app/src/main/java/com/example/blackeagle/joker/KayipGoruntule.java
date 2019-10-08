package com.example.blackeagle.joker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class KayipGoruntule extends AppCompatActivity {
    TextView txtcins,textisim,texttur,textdtar,textasi,textcinsiyeti,sehirtext;
    String petisim,petcins,pettur,petdt,petasi,petcinsiyet,petphoto,petpht,sehr,petid,uniqid;
    String user,userid;
    Button mesaj,ilansil,geri;
    private CircleImageView petimage;

    DatabaseReference databaseReference;

    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayip_goruntule);
        textisim = findViewById(R.id.txtisim);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        geri = findViewById(R.id.button2);
        sehirtext = findViewById(R.id.txtshr);
        txtcins = findViewById(R.id.txtcins);
        textcinsiyeti = findViewById(R.id.txtcinsiyeti);
        texttur = findViewById(R.id.txttur);
        textdtar = findViewById(R.id.txtdogum);
        textasi = findViewById(R.id.txtasi);
        petimage = findViewById(R.id.petpp);
        petphoto = Kayiplar.selectedimage;
        petpht = Sahiplenme.selectedimage;
        mesaj = findViewById(R.id.mesajat);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userid = firebaseUser.getUid().toString();
        ilansil = findViewById(R.id.ilansil_btn);

        Intent intent = getIntent();




        uniqid = intent.getStringExtra("uniqid");
        user = intent.getStringExtra("sahipid");
        petid = intent.getStringExtra("idpet");
        petisim = intent.getStringExtra("name");
        petcins = intent.getStringExtra("cins");
        petcinsiyet = intent.getStringExtra("cinsiyeti");
        pettur = intent.getStringExtra("türü");
        petdt  = intent.getStringExtra("dogumtarihi");
        petasi = intent.getStringExtra("asilar");
      //  petpht = intent.getStringExtra("petphoto");
        sehr = intent.getStringExtra("sehir");
        textisim.setText("İsim : "+petisim);
        txtcins.setText("Cins  : "+petcins);
        textcinsiyeti.setText("Cinsiyeti : "+petcinsiyet);
        texttur.setText("Türü : "+pettur);
        textdtar.setText("Doğum Tarihi : "+petdt);
        textasi.setText("Asilar : "+petasi);
        sehirtext.setText("Şehir : "+sehr);


if(uniqid.equals("from_activitykayiplar")) {
    Picasso.get().load(petphoto).centerCrop().fit().into(petimage);
    geri.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),Kayiplar.class));
        }
    });
}
if(uniqid.equals("from_activitysahiplenme")) {
    Picasso.get().load(petpht).centerCrop().fit().into(petimage);
    // Picasso.get().load(petphoto).centerCrop().fit().into(petimage);
    geri.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),Sahiplenme.class));
        }
    });
}

ilankaldir();
btnfunc();
        mesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                    Intent i = new Intent(getApplicationContext(), SahipProfiliniGor.class);
                    i.putExtra("uid", user);
                    startActivity(i);



            }
        });
    }

    private void btnfunc(){

        if(user.equals(userid)){
            mesaj.setEnabled(false);
            mesaj.setClickable(false);
            mesaj.setText("Sahip Zaten Sizsiniz.");
            mesaj.setVisibility(View.GONE);
            ilansil.setVisibility(View.VISIBLE);
            ilansil.setBackgroundColor(Color.TRANSPARENT);

        }else
        {
            mesaj.setClickable(true);
            mesaj.setEnabled(true);
            ilansil.setVisibility(View.GONE);
            ilansil.setClickable(false);
            ilansil.setEnabled(false);
        }
    }
    private void ilankaldir(){



            ilansil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(KayipGoruntule.this)
                            .setTitle("DİKKAT!")
                            .setMessage("İlanı kaldırmak istediğinizden emin misiniz?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    if(uniqid.equals("from_activitysahiplenme")) {
                                        databaseReference.child("Sahiplendirilecekler").child(petid).removeValue();
                                        startActivity(new Intent(getApplicationContext(),Sahiplenme.class));
                                    }
                                    if(uniqid.equals("from_activitykayiplar")) {
                                        databaseReference.child("Losts").child(petid).removeValue();
                                        startActivity(new Intent(getApplicationContext(),Kayiplar.class));
                                    }

                                    Toast.makeText(getApplicationContext(), "İlan başarıyla kaldırıldı!", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("HAYIR", null).show();
                }
            });

    }



}
