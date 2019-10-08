package com.example.blackeagle.joker;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VetBilgileri extends ArrayAdapter<String> {

    private final ArrayList<String> vetname;
    private final ArrayList<String> vetemail;
    private final ArrayList<String> vetphone;
    private final ArrayList<String> vetsehir;
    private final ArrayList<String> vetcinsiyet;
    private final ArrayList<String> vetimageurl;
    private final Activity vetpage;



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = vetpage.getLayoutInflater();
        View vetview = layoutInflater.inflate(R.layout.vetview,null,true);
        TextView txtvetname = vetview.findViewById(R.id.txt_vetisim);
        TextView txtvetemail = vetview.findViewById(R.id.txt_vetemail);
        TextView txtvetphone = vetview.findViewById(R.id.txt_vetphone);
        TextView txtvetsehir= vetview.findViewById(R.id.txt_vetsehir);
        TextView txtvetcinsiyet = vetview.findViewById(R.id.txt_vetcinsiyet);
        CircleImageView vetimage = vetview.findViewById(R.id.vetimage);

        txtvetname.setText(vetname.get(position));
        txtvetemail.setText(vetemail.get(position));
        txtvetphone.setText(vetphone.get(position));
        txtvetcinsiyet.setText(vetcinsiyet.get(position));
        txtvetsehir.setText(vetsehir.get(position));

        Picasso.get().load(vetimageurl.get(position)).placeholder(R.drawable.erkek).centerCrop().fit().into(vetimage);

        Picasso.get().load(vetimageurl.get(position)).centerCrop().fit().into(vetimage);



        return vetview;
    }

    public VetBilgileri(ArrayList<String> vetname, ArrayList<String> vetemail, ArrayList<String> vetphone, ArrayList<String> vetsehir, ArrayList<String> vetcinsiyet, ArrayList<String> vetimageurl, Activity vetpage) {
        super(vetpage,R.layout.vetview,vetemail);
        this.vetname = vetname;
        this.vetemail = vetemail;
        this.vetphone = vetphone;
        this.vetsehir = vetsehir;
        this.vetcinsiyet = vetcinsiyet;
        this.vetimageurl = vetimageurl;
        this.vetpage = vetpage;
    }
}
