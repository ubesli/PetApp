package com.example.blackeagle.joker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Veterinerler extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<String> vetnamefromfb;
    ArrayList<String> vetemailfromfb;
    ArrayList<String> vetphonefromfb;
    ArrayList<String> vetcinsiyetfromfb;
    ArrayList<String> vetsehirfromfb;
    ArrayList<String> vetimageurlfromfb;
    ArrayList<String> vetuidfromfb;
    ListView vetlistview;
    DatabaseReference databaseReference;
    VetBilgileri adapter;
    Spinner spinner;
    String selectedcity;
    static String selectedimage;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veterinerler);
        btn = findViewById(R.id.gomainpage);
        vetuidfromfb = new ArrayList<String>();
        vetcinsiyetfromfb = new ArrayList<String>();
        vetemailfromfb = new ArrayList<String>();
        vetnamefromfb = new ArrayList<String>();
        vetsehirfromfb = new ArrayList<String>();
        vetphonefromfb = new ArrayList<String>();
        vetimageurlfromfb = new ArrayList<String>();
        vetlistview = findViewById(R.id.vetlistview);
        TextView tx = findViewById(R.id.textView3);
        vetlistview.setEmptyView(tx);
        spinner = findViewById(R.id.vetspinner);
        ArrayAdapter<CharSequence> adapterr = ArrayAdapter.createFromResource(this, R.array.cities, android.R.layout.simple_spinner_item);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterr);
        spinner.setOnItemSelectedListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Anamenu.class));
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();

        adapter = new VetBilgileri(vetnamefromfb,vetemailfromfb,vetphonefromfb,vetsehirfromfb,vetcinsiyetfromfb,vetimageurlfromfb,this);
        vetlistview.setAdapter(adapter);
       /* adapter.clear();
        vetnamefromfb.clear();
        vetphonefromfb.clear();
        vetsehirfromfb.clear();
        vetemailfromfb.clear();
        vetcinsiyetfromfb.clear();
        vetimageurlfromfb.clear();
        vetuidfromfb.clear();
         getvetdatas();*/
        vetlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intenttt = new Intent(getApplicationContext(),VetGoruntule.class);
              //  intenttt.setClass(Veterinerler.this,SahipProfiliniGor.class);
                intenttt.putExtra("vetid",vetuidfromfb.get(i));
                intenttt.putExtra("name",vetnamefromfb.get(i));
                intenttt.putExtra("email", vetemailfromfb.get(i));
                intenttt.putExtra("cinsiyeti", vetcinsiyetfromfb.get(i));
                intenttt.putExtra("phone", vetphonefromfb.get(i));
                intenttt.putExtra("sehir", vetsehirfromfb.get(i));
                selectedimage = vetimageurlfromfb.get(i);
                //intenttt.putExtra("sayfa","fromveterinerler");

                startActivity(intenttt);
            }
        });
    }

    private void getvetdatas(){

        databaseReference.child("Veterinerler").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    vetnamefromfb.add(hashMap.get("name"));
                    vetemailfromfb.add("E-mail : "+hashMap.get("email"));
                    vetsehirfromfb.add("Şehir : "+hashMap.get("sehir"));
                    vetphonefromfb.add("Telefon : "+hashMap.get("phone"));
                    vetcinsiyetfromfb.add("Cinsiyet : "+hashMap.get("cinsiyet"));
                    vetimageurlfromfb.add(hashMap.get("photourl"));
                    vetuidfromfb.add(hashMap.get("uid"));
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
        vetnamefromfb.clear();
        vetphonefromfb.clear();
        vetsehirfromfb.clear();
        vetemailfromfb.clear();
        vetcinsiyetfromfb.clear();
        vetimageurlfromfb.clear();
        vetuidfromfb.clear();
        selectedcity = adapterView.getItemAtPosition(i).toString();

        //selectedcity = spinner.getSelectedItem().toString();
        if(selectedcity.equals("Tüm Şehirler")){
            adapter.clear();
            vetnamefromfb.clear();
            vetphonefromfb.clear();
            vetsehirfromfb.clear();
            vetemailfromfb.clear();
            vetcinsiyetfromfb.clear();
            vetimageurlfromfb.clear();
            vetuidfromfb.clear();
            getvetdatas();
            vetlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intenttt = new Intent(getApplicationContext(),VetGoruntule.class);
                   // intenttt.setClass(Veterinerler.this,SahipProfiliniGor.class);
                    intenttt.putExtra("uniqid",vetuidfromfb.get(i));
                    intenttt.putExtra("name",vetnamefromfb.get(i));
                    intenttt.putExtra("email", vetemailfromfb.get(i));
                    intenttt.putExtra("cinsiyeti", vetcinsiyetfromfb.get(i));
                    intenttt.putExtra("phone", vetphonefromfb.get(i));
                    intenttt.putExtra("sehir", vetsehirfromfb.get(i));
                    selectedimage = vetimageurlfromfb.get(i);
                    intenttt.putExtra("sayfa","fromveterinerler");

                    startActivity(intenttt);
                }
            });
        }
        else {
            adapter.clear();
            vetnamefromfb.clear();
            vetphonefromfb.clear();
            vetsehirfromfb.clear();
            vetemailfromfb.clear();
            vetcinsiyetfromfb.clear();
            vetimageurlfromfb.clear();


            databaseReference.child("Veterinerler").orderByChild("sehir").equalTo(selectedcity).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                            vetnamefromfb.add(hashMap.get("name"));
                            vetemailfromfb.add("E-mail : " + hashMap.get("email"));
                            vetsehirfromfb.add("Şehir : " + hashMap.get("sehir"));
                            vetphonefromfb.add("Telefon : " + hashMap.get("phone"));
                            vetcinsiyetfromfb.add("Cinsiyet : " + hashMap.get("cinsiyet"));
                            vetimageurlfromfb.add(hashMap.get("photourl"));
                            vetuidfromfb.add(hashMap.get("uid"));




                        adapter.notifyDataSetChanged();


                        vetlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intenttt = new Intent(getApplicationContext(),VetGoruntule.class);
                              //  intenttt.setClass(Veterinerler.this,SahipProfiliniGor.class);
                                intenttt.putExtra("uniqid",vetuidfromfb.get(i));
                                intenttt.putExtra("name",vetnamefromfb.get(i));
                                intenttt.putExtra("email", vetemailfromfb.get(i));
                                intenttt.putExtra("cinsiyeti", vetcinsiyetfromfb.get(i));
                                intenttt.putExtra("phone", vetphonefromfb.get(i));
                                intenttt.putExtra("sehir", vetsehirfromfb.get(i));
                                selectedimage = vetimageurlfromfb.get(i);
                                intenttt.putExtra("sayfa","fromveterinerler");

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
