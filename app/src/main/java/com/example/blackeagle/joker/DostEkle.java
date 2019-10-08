package com.example.blackeagle.joker;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.UUID;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class DostEkle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button vazgec, onay, tarihbtn;
    private Uri mImageUri;
    private CircleImageView petimg;
    private EditText petisim, petcins, petdogumtarih, petasilar, petsehir;
    private Spinner cityspinner;
    private RadioButton rderkek, rddisi, rdcat, rddog;
    private FirebaseUser currentuser;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    String uniqueID = UUID.randomUUID().toString();
    String cinsiyeti, hayvancinsi, petname, tur, petdt, asi, downloadurl, petcity, tumsehir;
    // MyPets dost = new MyPets(petname, hayvancinsi, tur, petdt, cinsiyeti, asi,downloadurl);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dost_ekle);
        rderkek = findViewById(R.id.erkek);
        cityspinner = findViewById(R.id.city_spinner);
        rddisi = findViewById(R.id.disi);
        rddog = findViewById(R.id.dog);
        rdcat = findViewById(R.id.kedi);
        vazgec = findViewById(R.id.vazgecbtn);
        onay = findViewById(R.id.onaylabtn);
        petimg = findViewById(R.id.petpp);
        petisim = findViewById(R.id.pet_isim);
        petcins = findViewById(R.id.pet_cins);

        //  petdogumtarih = findViewById(R.id.pet_dt);
        petdogumtarih = findViewById(R.id.pet_dt);

        petasilar = findViewById(R.id.pet_asilar);
        tarihbtn = findViewById(R.id.tarihsecbtn);
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference(currentuser.getUid());
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<CharSequence> adapterr = ArrayAdapter.createFromResource(this, R.array.cities, android.R.layout.simple_spinner_item);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityspinner.setAdapter(adapterr);
        cityspinner.setOnItemSelectedListener(this);
        // cityspinner.setPrompt("Şehir Seçiniz...");


        petimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

       /* onay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petkayit();
                // uploadFile();
            }
        });*/
        onay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addpet();

            }
        });
        vazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DostEkle.this, Dostlarim.class));
            }
        });
        tarihbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tarihsec();
            }
        });


    }

    private void tarihsec() {
        final Calendar takvim = Calendar.getInstance();
        int yil = takvim.get(Calendar.YEAR);
        int ay = takvim.get(Calendar.MONTH);
        int gun = takvim.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                        // değeri 1 artırarak gösteriyoruz.
                        month += 1;
                        // year, month ve dayOfMonth değerleri seçilen tarihin değerleridir.
                        // Edittextte bu değerleri gösteriyoruz.

                        tarihbtn.setText(dayOfMonth + "/" + month + "/" + year);

                        petdogumtarih.setText(dayOfMonth + "/" + month + "/" + year);

                    }
                }, yil, ay, gun);
        // datepicker açıldığında set edilecek değerleri buraya yazıyoruz.
        // şimdiki zamanı göstermesi için yukarda tanmladığımz değşkenleri kullanyoruz.

        // dialog penceresinin button bilgilerini ayarlıyoruz ve ekranda gösteriyoruz.
        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
        dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
        dpd.show();

    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(petimg);
        }
    }

    private void addpet() {

        petname = petisim.getText().toString();
        tur = petcins.getText().toString();

        petdt = tarihbtn.getText().toString();

        petdt = petdogumtarih.getText().toString();

        asi = petasilar.getText().toString();
        //  petcity = petsehir.getText().toString();
        if (petcity.equals(tumsehir)) {
            cityspinner.requestFocus();
            Toast.makeText(getApplicationContext(), "Lütfen Şehir Belirtiniz", Toast.LENGTH_LONG).show();
            return;

        }
        if (petname.isEmpty()) {
            petisim.setError("Bu alan boş bırakılamaz.");
            petisim.requestFocus();
            return;
        }
        if (tur.isEmpty()) {
            petcins.setError("Bu alan boş bırakılamaz");
            petcins.requestFocus();
            return;
        }

        if (petdt.isEmpty() || petdt.equals("  Doğum Tarihi Seçiniz")) {
            tarihbtn.setError("Lütfen Tarih seçiniz");
            tarihbtn.requestFocus();
            if (petdt.isEmpty()) {
                petdogumtarih.setError("Bu alan boş bırakılamaz");
                petdogumtarih.requestFocus();

                return;
            }
            if (asi.isEmpty()) {
                petasilar.setError("Bu alan boş bırakılamaz");
                petasilar.requestFocus();
                return;
            }
            if (!rderkek.isChecked() && !rddisi.isChecked()) {
                Toast.makeText(DostEkle.this, "Lütfen Cinsiyet Belirtiniz", Toast.LENGTH_SHORT).show();
                rderkek.requestFocus();
                rddisi.requestFocus();
                return;
            }
            if (rddisi.isChecked()) {
                cinsiyeti = "Dişi";
            } else {
                cinsiyeti = "Erkek";
            }
            if (!rdcat.isChecked() && !rddog.isChecked()) {
                Toast.makeText(DostEkle.this, "Lütfen Cins Belirtiniz", Toast.LENGTH_SHORT).show();
            }
            if (rdcat.isChecked()) {
                hayvancinsi = "Kedi";
            } else {
                hayvancinsi = "Köpek";
            }


            //upload photo
            if (mImageUri != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Değişiklikler Kaydediliyor...");
                progressDialog.show();
                String imagepath = "PetPhotos/" + uniqueID + ".jpg";


                final StorageReference riversRef = mStorageRef.child(imagepath);
                //BURADAYIM     downloadurl

                riversRef.putFile(mImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                downloadurl = taskSnapshot.getDownloadUrl().toString();
                                // pid= mDatabaseRef.child("Pets").child(currentuser.getUid()).child(uniqueID).toString();
                                MyPets pets = new MyPets(petname, hayvancinsi, tur, petdt, cinsiyeti, asi, downloadurl, petcity, uniqueID);
                                mDatabaseRef.child("Pets").child(currentuser.getUid()).child(uniqueID).setValue(pets);


                                Toast.makeText(DostEkle.this, "Başarıyla tamamlandı...", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Profil.class));

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                progressDialog.dismiss();
                                Toast.makeText(DostEkle.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage(((int) progress) + "% yüklendi...");


                            }
                        });


            } else {
                Toast.makeText(this, "No image is selected.", Toast.LENGTH_SHORT).show();
            }


        }

     /*   @Override
        public void onItemSelected(AdapterView < ? > adapterView, View view,int i, long l){


            petcity = adapterView.getItemAtPosition(i).toString();
            tumsehir = adapterView.getItemAtPosition(0).toString();

        }

        @Override
        public void onNothingSelected (AdapterView < ? > adapterView){
            petcity.equals("Tüm Şehirler");

        }*/
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        petcity = adapterView.getItemAtPosition(i).toString();
        tumsehir = adapterView.getItemAtPosition(0).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        petcity.equals("Tüm Şehirler");

    }
}
