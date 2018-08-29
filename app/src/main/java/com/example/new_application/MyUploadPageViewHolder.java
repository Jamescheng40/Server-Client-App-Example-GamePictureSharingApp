package com.example.new_application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyUploadPageViewHolder extends RecyclerView.ViewHolder {
    public ImageView displayedImage;
    public TextView picname;
    protected Context mcontext;
    private String musername = "";
//ipadres info
    private String ipadres;

    public MyUploadPageViewHolder(@NonNull View itemView, Context context, String username, final String ipadress) {

        super(itemView);
//get context and username from myupload page
        this.mcontext = context;
        this.musername = username;
        this.ipadres = ipadress;

//set up components
        displayedImage = (ImageView) itemView.findViewById(R.id.AMUP_img);
        picname= (TextView)itemView.findViewById(R.id.AMUP_imgname);

        displayedImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//transfer to crop and setwallpaper intent or crop and download intent; picture name is required, ipadress is required
                Intent hi = new Intent(displayedImage.getContext(),PictureView.class);
                //System.out.println(picname.getText().toString());
                hi.putExtra("picname",picname.getText().toString());
                hi.putExtra("IsUpload",true);
                hi.putExtra("username", musername);
                hi.putExtra("ip", ipadress);

//starting request for refreshing adapter
                ((Activity)mcontext).startActivityForResult(hi,1);

            }
        });

    }
}
