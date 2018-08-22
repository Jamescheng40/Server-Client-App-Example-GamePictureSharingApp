package com.example.new_application;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ImageView displayedImage;
    public TextView textTitle;
    public ImageView gameImage;
    public TextView gamename;
    public TextView username;
    public TextView picname;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        textTitle = (TextView)itemView.findViewById(R.id.title_header);
        displayedImage = (ImageView) itemView.findViewById(R.id.icon_image);
        gameImage = (ImageView)itemView.findViewById(R.id.title_image);
        username = (TextView)itemView.findViewById(R.id.title_usernmae);
        picname= (TextView)itemView.findViewById(R.id.textView9);



        displayedImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//transfer to crop and setwallpaper intent or crop and download intent; picture name is required
                Intent hi = new Intent(displayedImage.getContext(),PictureView.class);
                //System.out.println(picname.getText().toString());
                hi.putExtra("picname",picname.getText().toString());
                hi.putExtra("IsUpload", false);
                ContextCompat.startActivity(displayedImage.getContext(),hi, null);

            }
        });

//textitle here todo: add your action down below
        textTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hi" + textTitle.getText().toString());
            }
        });

    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.title_header:

                break;
        }

        // System.out.println("hi");
    }


}