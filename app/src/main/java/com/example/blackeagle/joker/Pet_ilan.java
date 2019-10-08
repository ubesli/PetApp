package com.example.blackeagle.joker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Pet_ilan extends AppCompatActivity {

    TextView txtcins,textisim,texttur,textdtar,textasi,textcinsiyeti,textsehir;
    String petisim,petcins,pettur,petdt,petasi,petcinsiyet,petphoto,currentuserid,petsehir,petid;
    Button kayipbtn,sahiplendirmebtn,silbtn,geribtn4;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference2;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    FirebaseUser currentuser;
    private CircleImageView petimage;
    static String kayipid,sahiplendirmeid;

     String randomid = UUID.randomUUID().toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_ilan);
       // storageReference = FirebaseStorage.getInstance().getReference(currentuserid).child("PetPhotos").child(petid+".jpg");
        geribtn4 = findViewById(R.id.geribtn4);
        silbtn = findViewById(R.id.btn_sil);
        textisim = findViewById(R.id.txtisim);
        txtcins = findViewById(R.id.txtcins);
        textsehir = findViewById(R.id.sehirtxt);
        textcinsiyeti = findViewById(R.id.txtcinsiyeti);
        texttur = findViewById(R.id.txttur);
        textdtar = findViewById(R.id.txtdogum);
        textasi = findViewById(R.id.txtasi);
        petimage = findViewById(R.id.petpp);
        kayipbtn = findViewById(R.id.btn_kayip);
        sahiplendirmebtn = findViewById(R.id.btn_sahiplendir);

        geribtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Dostlarim.class));
            }
        });
       petphoto = Dostlarim.selectedimageurl;
      //  petphoto = storageReference.getDownloadUrl().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("Losts");
        databaseReference2 = firebaseDatabase.getInstance().getReference("Sahiplendirilecekler");
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        Intent intent = getIntent();

        petid = intent.getStringExtra("pid");
        petisim = intent.getStringExtra("name");
        petcins = intent.getStringExtra("cins");
        petcinsiyet = intent.getStringExtra("cinsiyeti");
        pettur = intent.getStringExtra("türü");
        petdt  = intent.getStringExtra("dogumtarihi");
        petasi = intent.getStringExtra("asilar");
        petsehir = intent.getStringExtra("sehir");
        textisim.setText("İsim : "+petisim);
        txtcins.setText("Cins  : "+petcins);
        textcinsiyeti.setText("Cinsiyeti : "+petcinsiyet);
        texttur.setText("Türü : "+pettur);
        textdtar.setText("Doğum Tarihi : "+petdt);
        textasi.setText("Asilar : "+petasi);
        textsehir.setText("Şehir :"+petsehir);
        Picasso.get().load(petphoto).centerCrop().fit().into(petimage);

        petkayitsil();
        kayipbtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(Pet_ilan.this)
                            .setTitle("Dikkat")
                            .setMessage("Kayıp ilanı vermek istediğinizden emin misiniz?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    kayipilan();
                                    Toast.makeText(getApplicationContext(),"Kayıp ilanı başarıyla verildi!",Toast.LENGTH_LONG).show();
                                }})
                            .setNegativeButton("HAYIR", null).show();



                }



        });

        sahiplendirmebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                new AlertDialog.Builder(Pet_ilan.this)
                        .setTitle("Dikkat")
                        .setMessage("Sahiplendirme ilanı vermek istediğinizden emin misiniz?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                sahiplendirmeilan();
                                Toast.makeText(getApplicationContext(),"Sahiplendirme ilanı başarıyla verildi!",Toast.LENGTH_LONG).show();
                            }})
                        .setNegativeButton("HAYIR", null).show();



            }


        });


    }

    public void kayipilan(){



            kayippet kayippet = new kayippet(petisim, petcins, pettur, petcinsiyet, petdt, petasi, petphoto, petsehir, currentuserid, petid);

            databaseReference.child(petid).setValue(kayippet);
            //petid = databaseReference.child("Losts").child(randomid).toString();
            //kayipid = databaseReference.child("Losts").child(randomid).child("currentuserid").toString();
            // Toast.makeText(getApplicationContext(),"Kayıp ilanı verildi",Toast.LENGTH_LONG).show();
        }


    public void sahiplendirmeilan(){



            kayippet sahiplendirilecekpet = new kayippet(petisim, petcins, pettur, petcinsiyet, petdt, petasi, petphoto, petsehir, currentuserid, petid);

            databaseReference2.child(petid).setValue(sahiplendirilecekpet);
            // petid = databaseReference.child("Losts").child(randomid).toString();
            // sahiplendirmeid= databaseReference2.child("Sahiplendirme").child(randomid).child("currentuserid").toString();
            // Toast.makeText(getApplicationContext(),"Sahiplendirme ilanı verildi.",Toast.LENGTH_LONG).show();
        }


    public  void petkayitsil(){

        silbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Pet_ilan.this)
                        .setTitle("DİKKAT! Bu kaydı silmek bu kaydın bütün ilanlarını da siler!")
                        .setMessage("Pet kaydını silmek istediğinizden emin misiniz?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Pets").child(currentuserid).child(petid);
                                dref.removeValue();
                                DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("Losts").child(petid);
                                DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Sahiplendirilecekler").child(petid);
                                d.removeValue();
                                dr.removeValue();
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                StorageReference sref = storageReference.child(currentuserid+"/PetPhotos/"+petid+".jpg");
                                sref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });


                                Toast.makeText(getApplicationContext(),"Kayıt başarıyla silindi!",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Pet_ilan.this,Dostlarim.class));
                            }})
                        .setNegativeButton("HAYIR", null).show();
            }
        });


    }
}
