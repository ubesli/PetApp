package com.example.blackeagle.joker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapterClass extends ArrayAdapter<String>{

    private final ArrayList<String> receivername;
    private final ArrayList<String> receiverimage;
   // private final ArrayList<String> receiverusertype;
    private final Activity context;

    public ChatAdapterClass(ArrayList<String> receivername, ArrayList<String> receiverimage, Activity context){

        super(context,R.layout.chat_user_info,receivername);
        this.receivername = receivername;
      //  this.receiverusertype = receiverusertype;
        this.receiverimage = receiverimage;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customchatview = layoutInflater.inflate(R.layout.chat_user_info,null,true);
        TextView receivernametext = customchatview.findViewById(R.id.chat_username);
        CircleImageView receiverimageview = customchatview.findViewById(R.id.chat_userimg);

        receivernametext.setText(receivername.get(position));
        Picasso.get().load(receiverimage.get(position)).centerCrop().fit().into(receiverimageview);

        return customchatview;
    }
}
