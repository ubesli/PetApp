package com.example.blackeagle.joker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class Sahiplenme extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button anamenuyedon;
    private ArrayList<String> petnameFromfb;
    private ArrayList<String> petcinsFromfb;
    private ArrayList<String> petturFromfb;
    private ArrayList<String> petcinsiyetFromfb;
    private ArrayList<String> petdtFromfb;
    private ArrayList<String> petasilarFromfb;
    private ArrayList<String> petimageFromfb;
    private ArrayList<String> petsehirFromfb;
    private ArrayList<String> sahipid;
    private ArrayList<String> petidfromfb;
    private ListView liste;
    DatabaseReference newRef, ref2;
    private CheckBox checkBox;
    StorageReference sRef;
    private FirebaseUser mUser;
    PetBilgileri adapter;
    static String selectedimage;
    static String selectedkayipid;
    String selectedcity,keys;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sahiplenme);
        anamenuyedon = findViewById(R.id.anamenuyedon);
        liste = findViewById(R.id.kayip_listview);
        checkBox = findViewById(R.id.checkBox);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        newRef = FirebaseDatabase.getInstance().getReference("Sahiplendirilecekler");
        ref2 = FirebaseDatabase.getInstance().getReference("Sahiplendirilecekler");
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapterr = ArrayAdapter.createFromResource(this, R.array.cities, android.R.layout.simple_spinner_item);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterr);
        spinner.setOnItemSelectedListener(this);

        anamenuyedon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Anamenu.class));
            }
        });

        petidfromfb = new ArrayList<String>();
        petnameFromfb = new ArrayList<String>();
        petcinsFromfb = new ArrayList<String>();
        petcinsiyetFromfb = new ArrayList<String>();
        petturFromfb = new ArrayList<String>();
        petdtFromfb = new ArrayList<String>();
        petasilarFromfb = new ArrayList<String>();
        petsehirFromfb = new ArrayList<String>();
        petimageFromfb = new ArrayList<String>();
        sahipid = new ArrayList<String>();
        adapter = new PetBilgileri(petnameFromfb, petcinsFromfb, petturFromfb, petcinsiyetFromfb, petdtFromfb, petasilarFromfb, petimageFromfb, petsehirFromfb, this);
        liste.setAdapter(adapter);
        TextView tx = findViewById(R.id.textView2);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        // tx.setVisibility(adapter.isEmpty()?View.GONE:View.VISIBLE);
        liste.setEmptyView(tx);

       /* adapter.clear();
        petasilarFromfb.clear();
        petnameFromfb.clear();
        petcinsFromfb.clear();
        petcinsiyetFromfb.clear();
        petturFromfb.clear();
        petdtFromfb.clear();
        petsehirFromfb.clear();
        petimageFromfb.clear();
        sahipid.clear();


        getData();*/
        //getselectedcitydata();
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intenttt = new Intent();
                intenttt.setClass(Sahiplenme.this,KayipGoruntule.class);
                intenttt.putExtra("uniqid","from_activitysahiplenme");
                intenttt.putExtra("name", petnameFromfb.get(i));
                intenttt.putExtra("cins", petcinsFromfb.get(i));
                intenttt.putExtra("cinsiyeti", petcinsiyetFromfb.get(i));
                intenttt.putExtra("türü", petturFromfb.get(i));
                intenttt.putExtra("dogumtarihi", petdtFromfb.get(i));
                intenttt.putExtra("asilar", petasilarFromfb.get(i));
                intenttt.putExtra("sehir", petsehirFromfb.get(i));
                selectedimage = petimageFromfb.get(i);
                intenttt.putExtra("imgid",selectedimage);
                intenttt.putExtra("sahipid", sahipid.get(i));
                intenttt.putExtra("idpet",petidfromfb.get(i));
                startActivity(intenttt);
            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox.isChecked()){
                    adapter.clear();
                    petasilarFromfb.clear();
                    petnameFromfb.clear();
                    petcinsFromfb.clear();
                    petcinsiyetFromfb.clear();
                    petturFromfb.clear();
                    petdtFromfb.clear();
                    petsehirFromfb.clear();
                    petimageFromfb.clear();
                    sahipid.clear();
                    benimilanlarım();

                    liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intenttt = new Intent();
                            intenttt.setClass(Sahiplenme.this,KayipGoruntule.class);
                            intenttt.putExtra("uniqid","from_activitysahiplenme");
                            intenttt.putExtra("name", petnameFromfb.get(i));
                            intenttt.putExtra("cins", petcinsFromfb.get(i));
                            intenttt.putExtra("cinsiyeti", petcinsiyetFromfb.get(i));
                            intenttt.putExtra("türü", petturFromfb.get(i));
                            intenttt.putExtra("dogumtarihi", petdtFromfb.get(i));
                            intenttt.putExtra("asilar", petasilarFromfb.get(i));
                            intenttt.putExtra("sehir", petsehirFromfb.get(i));
                            selectedimage = petimageFromfb.get(i);
                            intenttt.putExtra("sahipid", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                            intenttt.putExtra("idpet",petidfromfb.get(i));
                            startActivity(intenttt);
                        }
                    });
                }
                else{
                    adapter.clear();
                    petasilarFromfb.clear();
                    petnameFromfb.clear();
                    petcinsFromfb.clear();
                    petcinsiyetFromfb.clear();
                    petturFromfb.clear();
                    petdtFromfb.clear();
                    petsehirFromfb.clear();
                    petimageFromfb.clear();
                    sahipid.clear();

                    getData();

                    liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intenttt = new Intent();
                            intenttt.setClass(Sahiplenme.this,KayipGoruntule.class);
                            intenttt.putExtra("uniqid","from_activitysahiplenme");
                            intenttt.putExtra("name", petnameFromfb.get(i));
                            intenttt.putExtra("cins", petcinsFromfb.get(i));
                            intenttt.putExtra("cinsiyeti", petcinsiyetFromfb.get(i));
                            intenttt.putExtra("türü", petturFromfb.get(i));
                            intenttt.putExtra("dogumtarihi", petdtFromfb.get(i));
                            intenttt.putExtra("asilar", petasilarFromfb.get(i));
                            intenttt.putExtra("sehir", petsehirFromfb.get(i));
                            selectedimage = petimageFromfb.get(i);
                            intenttt.putExtra("sahipid", sahipid.get(i));

                            intenttt.putExtra("idpet",petidfromfb.get(i));
                            startActivity(intenttt);
                        }
                    });
                }

            }
        });





    }

    private void getData() {


        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    petnameFromfb.add(hashMap.get("petisim"));
                    petcinsFromfb.add(hashMap.get("petcinsi"));
                    petcinsiyetFromfb.add(hashMap.get("petcinsiyeti"));
                    petturFromfb.add(hashMap.get("pettur"));
                    petdtFromfb.add(hashMap.get("petdtar"));
                    petasilarFromfb.add(hashMap.get("petasilar"));
                    petimageFromfb.add(hashMap.get("petphoto"));
                    petsehirFromfb.add(hashMap.get("petsehir"));
                    sahipid.add(hashMap.get("currentuserid"));
                    petidfromfb.add(hashMap.get("petid"));


                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }






    private void benimilanlarım() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Sahiplendirilecekler");
        ref.orderByChild("currentuserid").equalTo(mUser.getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {

                    HashMap<String, String> hashMap = (HashMap<String, String>) datas.getValue();
                    petnameFromfb.add(hashMap.get("petisim"));
                    petcinsFromfb.add(hashMap.get("petcinsi"));
                    petcinsiyetFromfb.add(hashMap.get("petcinsiyeti"));
                    petturFromfb.add(hashMap.get("pettur"));
                    petdtFromfb.add(hashMap.get("petdtar"));
                    petasilarFromfb.add(hashMap.get("petasilar"));
                    petimageFromfb.add(hashMap.get("petphoto"));
                    petsehirFromfb.add(hashMap.get("petsehir"));
                    sahipid.add(hashMap.get("currentuserid"));
                    petidfromfb.add(hashMap.get("petid"));



                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        adapter.clear();
        petasilarFromfb.clear();
        petnameFromfb.clear();
        petcinsFromfb.clear();
        petcinsiyetFromfb.clear();
        petturFromfb.clear();
        petdtFromfb.clear();
        petsehirFromfb.clear();
        petimageFromfb.clear();
        sahipid.clear();
        selectedcity = adapterView.getItemAtPosition(i).toString();

        //selectedcity = spinner.getSelectedItem().toString();
        if(selectedcity.equals("Tüm Şehirler")){
            adapter.clear();
            petasilarFromfb.clear();
            petnameFromfb.clear();
            petcinsFromfb.clear();
            petcinsiyetFromfb.clear();
            petturFromfb.clear();
            petdtFromfb.clear();
            petsehirFromfb.clear();
            petimageFromfb.clear();
            sahipid.clear();
            getData();
        }
        else {
            adapter.clear();
            petasilarFromfb.clear();
            petnameFromfb.clear();
            petcinsFromfb.clear();
            petcinsiyetFromfb.clear();
            petturFromfb.clear();
            petdtFromfb.clear();
            petsehirFromfb.clear();
            petimageFromfb.clear();
            sahipid.clear();

            ref2.orderByChild("petsehir").equalTo(selectedcity).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                        petnameFromfb.add(hashMap.get("petisim"));
                        petcinsFromfb.add(hashMap.get("petcinsi"));
                        petcinsiyetFromfb.add(hashMap.get("petcinsiyeti"));
                        petturFromfb.add(hashMap.get("pettur"));
                        petdtFromfb.add(hashMap.get("petdtar"));
                        petasilarFromfb.add(hashMap.get("petasilar"));
                        petimageFromfb.add(hashMap.get("petphoto"));
                        petsehirFromfb.add(hashMap.get("petsehir"));
                        sahipid.add(hashMap.get("currentuserid"));
                        petidfromfb.add(hashMap.get("petid"));


                        adapter.notifyDataSetChanged();


                        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intenttt = new Intent();
                                intenttt.setClass(Sahiplenme.this,KayipGoruntule.class);
                                intenttt.putExtra("uniqid","from_activitysahiplenme");
                                intenttt.putExtra("name", petnameFromfb.get(i));
                                intenttt.putExtra("cins", petcinsFromfb.get(i));
                                intenttt.putExtra("cinsiyeti", petcinsiyetFromfb.get(i));
                                intenttt.putExtra("türü", petturFromfb.get(i));
                                intenttt.putExtra("dogumtarihi", petdtFromfb.get(i));
                                intenttt.putExtra("asilar", petasilarFromfb.get(i));
                                intenttt.putExtra("sehir", petsehirFromfb.get(i));
                                selectedimage = petimageFromfb.get(i);
                                intenttt.putExtra("sahipid", sahipid.get(i));
                                intenttt.putExtra("idpet", petidfromfb.get(i));

                                startActivity(intenttt);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
