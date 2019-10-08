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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetBilgileri extends ArrayAdapter<String>{

    ArrayList<String> petname;
    ArrayList<String> petcins;
    ArrayList<String> pettur;
    ArrayList<String> petcinsiyet;
    ArrayList<String> petdt;
    ArrayList<String> petasilar;
    ArrayList<String> petimage;
    ArrayList<String> petsehir;
    DostEkle url;

   private Activity petcontext;

    public PetBilgileri(ArrayList<String> petname, ArrayList<String> petcins, ArrayList<String> pettur, ArrayList<String> petcinsiyet, ArrayList<String> petdt, ArrayList<String> petasilar,ArrayList<String> petimage,ArrayList<String>petsehir, Activity petcontext){
        super(petcontext,R.layout.petlist,petname);
        this.petname = petname;
        this.petcins = petcins;
        this.pettur = pettur;
        this.petimage = petimage;
        this.petcinsiyet = petcinsiyet;
        this.petdt = petdt;
        this.petasilar = petasilar;
        this.petsehir = petsehir;
        this.petcontext = petcontext;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = petcontext.getLayoutInflater();
        View petview = layoutInflater.inflate(R.layout.petlist,null,true);
        TextView petnameText = petview.findViewById(R.id.petname);
        TextView petcinsText = petview.findViewById(R.id.petcins);
        TextView petturText = petview.findViewById(R.id.petturu);
        TextView petcinsiyetText = petview.findViewById(R.id.petcinsiyet);
        TextView petdtText = petview.findViewById(R.id.petdt);
        TextView petasiText = petview.findViewById(R.id.petasi);
        TextView petsehirText = petview.findViewById(R.id.petsehir);
        CircleImageView petimageview = petview.findViewById(R.id.petimage);

        petnameText.setText("Adı: "+petname.get(position));
        petcinsiyetText.setText("Cinsiyet: "+petcinsiyet.get(position));
        petcinsText.setText("Cins: "+petcins.get(position));
        petturText.setText("Tür: "+pettur.get(position));
        petdtText.setText("DoğumTarihi: "+petdt.get(position));
        petasiText.setText("Aşılar: "+petasilar.get(position));
        petsehirText.setText("Şehir: "+petsehir.get(position));
       Picasso.get().load(petimage.get(position)).centerCrop().fit().into(petimageview);



        return petview;
    }
}
