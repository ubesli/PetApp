package com.example.blackeagle.joker;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatList extends AppCompatActivity {

    ArrayList<String> receivernamefromfb;
    ArrayList<String> receivertypefromfb;
    ArrayList<String> receiverimagefromfb;
    ArrayList<String> receiveridfromfb;
    DatabaseReference dref,ref,msjsilref;
    ListView chatlistview;
    FirebaseUser cruser,receive;
    ChatAdapterClass chatadapter;
    TextView deneme;
    Toolbar chatlisttoolbar;
    String drefstring;

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_a, menu);
        return true;
    }*/






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Intent inten = getIntent();
        chatlistview = findViewById(R.id.listviewchat);
        cruser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        chatlisttoolbar = findViewById(R.id.chatlisttoolbar);
        setSupportActionBar(chatlisttoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Sohbetler");

        chatlisttoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatList.this,Anamenu.class));
            }
        });




        deneme = findViewById(R.id.deneme);
        receivernamefromfb = new ArrayList<String>();
        receivertypefromfb = new ArrayList<String>();
        receiverimagefromfb= new ArrayList<String>();
        receiveridfromfb = new ArrayList<String>();
        chatadapter = new ChatAdapterClass(receivernamefromfb,receiverimagefromfb,this);
        chatlistview.setAdapter(chatadapter);

chatadapter.clear();
receiveridfromfb.clear();
receivernamefromfb.clear();
receiverimagefromfb.clear();

getbilgi();
chatlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent in =new Intent(ChatList.this,ChatActivity.class);
        in.putExtra("visiteduserid",receiveridfromfb.get(i));
        in.putExtra("isim",receivernamefromfb.get(i));
        startActivity(in);
    }
});

chatlistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
    @Override

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        new android.app.AlertDialog.Builder(ChatList.this)
                .setTitle("Dikkat")
                .setMessage("Kullanıcıyı Sohbetten Silmek İstediğinize Emin Misiniz?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dref = FirebaseDatabase.getInstance().getReference("Users").child(cruser.getUid()).child("Contacts").child(receiveridfromfb.get(i));
                        dref.removeValue();
                        msjsilref = FirebaseDatabase.getInstance().getReference("Messages").child(cruser.getUid()).child(receiveridfromfb.get(i));
                        msjsilref.removeValue();
                        Toast.makeText(getApplicationContext(),"Kullanıcı Başarıyla Silindi!",Toast.LENGTH_LONG).show();
                        chatadapter.clear();
                        receiveridfromfb.clear();
                        receivernamefromfb.clear();
                        receiverimagefromfb.clear();
                    }})
                .setNegativeButton("HAYIR", null).show();




        return false;
    }
});
    }



    private void getbilgi(){

        ref.child("Users").child(cruser.getUid().toString()).child("Contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               // deneme.setText(dataSnapshot.getValue().toString());
                for( DataSnapshot ds : dataSnapshot.getChildren()){
                    HashMap<String,String> hashMap = (HashMap<String,String>) ds.getValue();

                    receivernamefromfb.add(hashMap.get("chatisim"));
                    receiverimagefromfb.add(hashMap.get("chatimg"));
                    receiveridfromfb.add(hashMap.get("chatid"));
                    chatadapter.notifyDataSetChanged();
               //    deneme.setText(ChatActivity.msgReceiverId);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
