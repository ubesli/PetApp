package com.example.blackeagle.joker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private String fotourl,fotourlsender,msgReceiverId,msgReceiverName,msgsenderid,msgtext,msgsendername;

    private TextView userNameTitle;
    CircleImageView userImage;
    private Toolbar toolbar;
    DatabaseReference dref;
    StorageReference sref,sref2;
    ImageButton sendmsgbutton;
    ImageButton picturebtn;
    EditText typemsg;
    FirebaseAuth mAuth;
    DatabaseReference rootRef,data;
    RecyclerView msglist;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    ArrayList<String> receiverids;
    ChatYaptıklarım chatYaptıklarım,msjatanlar;
    DatabaseReference dataref,msjsilref;
    StorageReference storageReference,imagemsgref;
    ProgressDialog loading;
    private static final int Gallery_Pick = 1;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_a, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== R.id.konusmayısil){

                        msjsilref = FirebaseDatabase.getInstance().getReference("Messages").child(msgsenderid).child(msgReceiverId);
                        msjsilref.removeValue();

                        messagesList.clear();
                        msglist.setAdapter(messageAdapter);
                        messageAdapter.notifyDataSetChanged();


        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
       // userNameTitle = findViewById(R.id.usernamedisplay);
        imagemsgref = FirebaseStorage.getInstance().getReference().child("Messages_Pictures");
        receiverids = new ArrayList<String>();
        Intent i = getIntent();
        msgReceiverId = i.getStringExtra("visiteduserid");
        msgReceiverName = i.getStringExtra("isim");
        sendmsgbutton = findViewById(R.id.send_msgbtn);
        picturebtn = findViewById(R.id.select_image);
        typemsg = findViewById(R.id.type_msg);
        msgsenderid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        rootRef = FirebaseDatabase.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference();
        messageAdapter = new MessageAdapter(messagesList);
        dataref = FirebaseDatabase.getInstance().getReference("Users").child(msgsenderid).child("Contacts");
        data = FirebaseDatabase.getInstance().getReference("Users").child(msgReceiverId).child("Contacts");
       storageReference= FirebaseStorage.getInstance().getReference(msgReceiverId);
       dref.child("Users").child(msgsenderid).child("name").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
              msgsendername = dataSnapshot.getValue().toString();
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });


        msglist = findViewById(R.id.msg_list);
        linearLayoutManager = new LinearLayoutManager(this);
        msglist.setHasFixedSize(true);
        msglist.setLayoutManager(linearLayoutManager);
        msglist.setAdapter(messageAdapter);




        userImage = findViewById(R.id.userimg);

       /* imgurl = SahipProfiliniGor.selectedpp;
        dref = FirebaseDatabase.getInstance().getReference("Users").child(msgReceiverId);*/

        sref = FirebaseStorage.getInstance().getReference(msgReceiverId);

        sref2 = FirebaseStorage.getInstance().getReference(msgsenderid);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loading = new ProgressDialog(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(msgReceiverName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this,ChatList.class));

            }
        });



      //  userNameTitle.setText(msgReceiverName);
       // Picasso.get().load(imgurl).fit().into(userImage);
        sref.child("images").child("profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //progressDialog.dismiss();
                Picasso.get().load(uri).fit().centerCrop().into(userImage);
              fotourl= uri.toString();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(Profil.this, "Bir Hata Meydana Geldi!", Toast.LENGTH_SHORT).show();
            }
        });
      //  Picasso.get().load(imgurl).fit().into(userImage);
        sref2.child("images").child("profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //progressDialog.dismiss();
               // Picasso.get().load(uri).fit().centerCrop().into(userImage);
                fotourlsender= uri.toString();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(Profil.this, "Bir Hata Meydana Geldi!", Toast.LENGTH_SHORT).show();
            }
        });




         sendmsgbutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 sendmsg();

             }
         });

        FetchMessages();

        picturebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Pick);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data != null){
           loading.setTitle("Fotoğraf Gönderiliyor...");
           loading.setMessage("Lütfen Bekleyiniz...");
           loading.show();
            Uri ImgUri = data.getData();
            final String msg_sender_ref = "Messages/"+ msgsenderid + "/" + msgReceiverId;
            final String msg_receiver_ref = "Messages/"+ msgReceiverId + "/" + msgsenderid;
            DatabaseReference user_msg_key = rootRef.child(msgsenderid).child(msgReceiverId).push();

            final String msg_push_id = user_msg_key.getKey();
            StorageReference imgpath = imagemsgref.child(msg_push_id +".jpg");
            imgpath.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final String dwurl= taskSnapshot.getDownloadUrl().toString();
                    Map msgTextBody = new HashMap();
                    msgTextBody.put("message",dwurl);
                    msgTextBody.put("time", ServerValue.TIMESTAMP);
                    msgTextBody.put("type","image");
                    msgTextBody.put("from",msgsenderid);


                    Map msgBodyDetails = new HashMap();

                    msgBodyDetails.put(msg_sender_ref+ "/" + msg_push_id ,msgTextBody);
                    msgBodyDetails.put(msg_receiver_ref+ "/" + msg_push_id, msgTextBody);
                    rootRef.updateChildren(msgBodyDetails, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError!=null){
                                Log.d("Chat_Log",databaseError.getMessage().toString());
                            }
                            msglist.smoothScrollToPosition(msglist.getAdapter().getItemCount()-1);
                            typemsg.setText("");
                            loading.dismiss();
                        }
                    });
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(),"Fotoğraf Başarıyla Gönderildi",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(),"Bir hata oluştu, tekrar deneyiniz",Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void FetchMessages() {
    rootRef.child("Messages").child(msgsenderid).child(msgReceiverId).addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Messages messages = dataSnapshot.getValue(Messages.class);
            messagesList.add(messages);
            msglist.smoothScrollToPosition(msglist.getAdapter().getItemCount()-1);
            messageAdapter.notifyDataSetChanged();
            //dataref.child(msgReceiverId).setValue(chatYaptıklarım);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });


    }

    private void sendmsg(){
        msgtext = typemsg.getText().toString();
        if(TextUtils.isEmpty(msgtext)){
            Toast.makeText(getApplicationContext(),"Boş mesaj gönderilemez!",Toast.LENGTH_SHORT).show();

        }
        else{
            String msg_sender_ref = "Messages/"+ msgsenderid + "/" + msgReceiverId;
            String msg_receiver_ref = "Messages/"+ msgReceiverId + "/" + msgsenderid;
            DatabaseReference user_msg_key = rootRef.child(msgsenderid).child(msgReceiverId).push();

            String msg_push_id = user_msg_key.getKey();


            Map msgTextBody = new HashMap();
            msgTextBody.put("message",msgtext);
            msgTextBody.put("time", ServerValue.TIMESTAMP);
            msgTextBody.put("type","text");
            msgTextBody.put("from",msgsenderid);


            Map msgBodyDetails = new HashMap();

            msgBodyDetails.put(msg_sender_ref+ "/" + msg_push_id ,msgTextBody);
            msgBodyDetails.put(msg_receiver_ref+ "/" + msg_push_id, msgTextBody);
            rootRef.updateChildren(msgBodyDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError!=null){
                        Log.d("Chat_Log",databaseError.getMessage().toString());
                    }
                    msglist.smoothScrollToPosition(msglist.getAdapter().getItemCount()-1);
                    typemsg.setText("");
                }
            });




            chatYaptıklarım = new ChatYaptıklarım(msgReceiverName,msgReceiverId,fotourl);
            msjatanlar = new ChatYaptıklarım(msgsendername,msgsenderid,fotourlsender);
            dataref.child(msgReceiverId).setValue(chatYaptıklarım);
            data.child(msgsenderid).setValue(msjatanlar);

        }


    }






}
