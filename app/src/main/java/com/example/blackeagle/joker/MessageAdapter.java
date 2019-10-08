package com.example.blackeagle.joker;

import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private StorageReference userdref;
    String userimage,username;

    public MessageAdapter(List<Messages> userMessagesList){
        this.userMessagesList = userMessagesList;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_layout,parent,false);
        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
        final String message_sender_id = mAuth.getCurrentUser().getUid().toString();
        Messages messages = userMessagesList.get(position);
        final String fromUserId = messages.getFrom();
        String MessageType = messages.getType();
       userdref = FirebaseStorage.getInstance().getReference(fromUserId).child("images").child("profile.jpg");
       userdref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
           @Override
           public void onSuccess(Uri uri) {
               if(fromUserId.equals(message_sender_id)) {
                   if(!uri.toString().equals(null)) {
                       Picasso.get().load(uri).placeholder(R.drawable.erkek).centerCrop().fit().into(holder.senderImage);
                   }
                   else{
                       //Picasso.get().load(R.drawable.erkek).placeholder(R.drawable.erkek).centerCrop().fit().into(holder.senderImage);

                   }
               }
               else{
                   if(!uri.toString().equals(null)) {
                       Picasso.get().load(uri).placeholder(R.drawable.erkek).centerCrop().fit().into(holder.receiverImage);
                   }
                   else{
                      // Picasso.get().load(R.drawable.erkek).placeholder(R.drawable.erkek).centerCrop().fit().into(holder.receiverImage);

                   }

                   Picasso.get().load(uri).centerCrop().fit().into(holder.senderImage);

                   Picasso.get().load(uri).centerCrop().fit().into(holder.receiverImage);

               }
           }
       });

         holder.msgimage.setVisibility(View.GONE);
        holder.msgimage2.setVisibility(View.GONE);
        holder.messageText2.setVisibility(View.GONE);
        holder.messageText.setVisibility(View.GONE);
        holder.receiverImage.setVisibility(View.GONE);
        holder.senderImage.setVisibility(View.GONE);

        if(MessageType.equals("text"))

        {

            if(fromUserId.equals(message_sender_id)){
              //  holder.userProfileImage2.setVisibility(View.GONE);
             //   holder.messageText2.setVisibility(View.GONE);
               // holder.messageText.setBackgroundResource(R.drawable.msg_layout2);
                holder.senderImage.setVisibility(View.VISIBLE);
                holder.messageText.setVisibility(View.VISIBLE);
                // holder.relativemsglayout.setGravity(Gravity.RIGHT);
                //   holder.relativemsglayout.setVerticalGravity(Gravity.RIGHT);
                //holder.messageText.setTextColor(Color.WHITE);
                holder.messageText.setGravity(Gravity.RIGHT);
                holder.messageText.setText(messages.getMessage());

            }
            else{
             //   holder.userProfileImage.setVisibility(View.GONE);
              //  holder.messageText.setVisibility(View.GONE);
               // holder.messageText.setBackgroundResource(R.drawable.msgtext_background);
               // holder.messageText2.setTextColor(Color.BLACK);
                // holder.relativemsglayout.setGravity(Gravity.LEFT);
                holder.receiverImage.setVisibility(View.VISIBLE);
                holder.messageText2.setVisibility(View.VISIBLE);
                holder.messageText2.setGravity(Gravity.LEFT);
                holder.messageText2.setText(messages.getMessage());

            }
        }
        else
        {

            if(fromUserId.equals(message_sender_id)){
                holder.senderImage.setVisibility(View.VISIBLE);

                holder.msgimage.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).placeholder(R.drawable.pet).centerCrop().fit().into(holder.msgimage);
            }
            else{
               holder.receiverImage.setVisibility(View.VISIBLE);

                holder.msgimage2.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).placeholder(R.drawable.pet).centerCrop().fit().into(holder.msgimage2);

            }
        }




    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText,messageText2;
        public CircleImageView senderImage,receiverImage;
        public ImageView msgimage,msgimage2;
        public RelativeLayout relativemsglayout;
        public MessageViewHolder(View view){
            super(view);
            messageText = view.findViewById(R.id.msg_text);
            messageText2 = view.findViewById(R.id.msg_text2);
           // relativemsglayout = view.findViewById(R.id.relativemsglayout);
          senderImage = view.findViewById(R.id.senderphoto);
          receiverImage = view.findViewById(R.id.receiverphoto);


            msgimage = view.findViewById(R.id.msg_imageview);
            msgimage2 = view.findViewById(R.id.msg_imageview2);
        }


    }
}
