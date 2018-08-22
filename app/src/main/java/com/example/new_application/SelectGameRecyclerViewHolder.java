package com.example.new_application;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SelectGameRecyclerViewHolder extends RecyclerView.ViewHolder  {
    public TextView textview;
    public ImageView imageview;
    public EditText textviewforresult;
    private Context mcontext;
    private String musername;

    public SelectGameRecyclerViewHolder(View itemView, Context context, String username) {
        super(itemView);
//context of the searchpage class
        this.mcontext = context;
//user username
        musername = username;

        textview = (TextView) itemView.findViewById(R.id.test123);
        imageview = (ImageView) itemView.findViewById(R.id.avatar);

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//intent for starting a new class and return with status code(eg, success or fail)
                    Intent i = new Intent(mcontext, MainActivity.class);
                    i.putExtra("game", textview.getText().toString());
                    i.putExtra("username", musername);
                    ((Activity) mcontext).startActivityForResult(i, 1);

            }
        });
        // Intent i = new Intent();
        // i.putExtra("game",textView.getText().toString());
        // setResult(312,i);
        // finish();



    }


}