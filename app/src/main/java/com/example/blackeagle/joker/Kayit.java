package com.example.blackeagle.joker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;;
import android.util.Patterns;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.FirebaseDatabase;

public class Kayit extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone,editTextPasswordAgain;
    private ProgressBar progressBar;
    private RadioButton rderkek,rdkadın,rdvet,rdstd;
    private Spinner sehirsec;
    String cinsiyet,tip,sehir,userid;
    private Button kayitvazgec;



    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);
        kayitvazgec = findViewById(R.id.kayitvazgec);


        sehirsec = findViewById(R.id.sehirsec);
        editTextName = findViewById(R.id.isimtext);
        editTextEmail = findViewById(R.id.emailtext);
        editTextPassword = findViewById(R.id.sifre);
        editTextPasswordAgain = findViewById(R.id.sifretekrar);
        editTextPhone = findViewById(R.id.phone);
        rderkek = findViewById(R.id.erkek);
        rdkadın = findViewById(R.id.kadın);
        rdvet = findViewById(R.id.veterinerbtn);
        rdstd = findViewById(R.id.normalkullanıcı);
        ArrayAdapter<CharSequence> adapterr = ArrayAdapter.createFromResource(this, R.array.sehir, android.R.layout.simple_spinner_item);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sehirsec.setAdapter(adapterr);
        sehirsec.setOnItemSelectedListener(this);

        kayitvazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Kayit.this,Login.class));
            }
        });




        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.kaydolbtn).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
            //Toast.makeText(Kayit.this,"Böyle bir kullanıcı zaten var!",Toast.LENGTH_LONG).show();
        }
    }

    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();

        if(sehir.equals("Şehir Seçiniz"))
        {
            Toast.makeText(getApplicationContext(),"Lütfen Şehir belirtiniz!!",Toast.LENGTH_LONG).show();
            sehirsec.requestFocus();
            return;
        }
        if(rderkek.isChecked()){
            cinsiyet = "Erkek";

        }
        else
            cinsiyet = "Kadın";
        if(!rderkek.isChecked() && !rdkadın.isChecked()){
            Toast.makeText(Kayit.this,"Lütfen Cinsiyet Belirtiniz!",Toast.LENGTH_LONG).show();
            rderkek.requestFocus();
            rdkadın.requestFocus();
            return;
        }
        if(rdvet.isChecked()){
            tip = "Veteriner";
        }
        else{
            tip = "Normal Kullanıcı";
        }
        if(!rdvet.isChecked() && !rdstd.isChecked()){
            Toast.makeText(Kayit.this,"Lütfen Kullanıcı Tipi Belirtiniz!",Toast.LENGTH_LONG).show();
            rdvet.requestFocus();
            rdstd.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            editTextName.setError("Bu alan boş bırakılamaz!");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Bu alan boş bırakılamaz!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Geçersiz e-mail!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Bu alan boş bırakılamaz!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Şifre en az 6 karakter olmalı!");
            editTextPassword.requestFocus();
            return;
        }
        if(!editTextPassword.getText().toString().trim().equals(editTextPasswordAgain.getText().toString().trim())){
            editTextPasswordAgain.setError("Şifreler uyuşmuyor!");
            editTextPassword.setError("Şifreler uyuşmuyor!");
            editTextPasswordAgain.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            editTextPhone.setError("Bu alan boş bırakılamaz!");
            editTextPhone.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            editTextPhone.setError("Telefon numaranızı 10 haneli olacak şekilde giriniz!");
            editTextPhone.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            if(tip=="Veteriner") {
                                userid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                                User user = new User(
                                        name,
                                        email,
                                        phone,
                                        cinsiyet,
                                        tip,
                                        password,
                                        sehir,
                                        userid
                                );
                                FirebaseDatabase.getInstance().getReference("Veterinerler")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Kayit.this, "Kayıt başarıyla tamamlandı.", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent (Kayit.this,Login.class));
                                        } else {
                                            Toast.makeText(Kayit.this, "Hatalı işlem!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Kayit.this, "Kayıt başarıyla tamamlandı.", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent (Kayit.this,Login.class));
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(Kayit.this, "Hatalı işlem!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                            }

                            else{
                            userid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                            User user = new User(
                                    name,
                                    email,
                                    phone,
                                    cinsiyet,
                                    tip,
                                    password,
                                    sehir,
                                    userid
                            );
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Kayit.this, "Kayıt başarıyla tamamlandı.", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent (Kayit.this,Login.class));
                                    } else {
                                        Toast.makeText(Kayit.this, "Hatalı işlem!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }} else {
                            Toast.makeText(Kayit.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });


    }

    @Override
      public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kaydolbtn:
                registerUser();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        sehir = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

