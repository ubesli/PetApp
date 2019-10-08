package com.example.blackeagle.joker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button kayitbtn,girisbtn;
    EditText emailtxt,sifretxt;
    FirebaseAuth mAuth;
    ProgressBar progressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        kayitbtn = findViewById(R.id.kayitbtn);
        girisbtn = findViewById((R.id.girisbtn));
        emailtxt = findViewById((R.id.emailtxt));
        sifretxt = findViewById((R.id.sifretxt));
        progressBar2 = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
        progressBar2.setVisibility(View.GONE);



        kayitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,Kayit.class);
                startActivity(i);
            }
        });

    }
    public void girisyap(View view) {
        if (emailtxt.getText().toString().equals("")){
            emailtxt.setError("Bu alan boş bırakılamaz!");
            emailtxt.requestFocus();
            return;
        }
        else if(sifretxt.getText().toString().equals("")) {
            sifretxt.setError("Bu alan boş bırakılamaz!");
            sifretxt.requestFocus();
            return;
        }
        else  {
            progressBar2.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(emailtxt.getText().toString(), sifretxt.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                progressBar2.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Giriş Başarılı!", Toast.LENGTH_LONG).show();
                                Intent intnt = new Intent(Login.this, Anamenu.class);
                                intnt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intnt);
                                FirebaseUser user = mAuth.getCurrentUser();

                            } else {
                                // If sign in fails, display a message to the user.
                                progressBar2.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Giriş Hatalı! Lütfen bilgilerinizi kontrol ediniz...",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });

        }
    }
}
