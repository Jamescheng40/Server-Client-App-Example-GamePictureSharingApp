package com.example.new_application;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MyUploadPage extends AppCompatActivity {
    private static final int RC_PICTUREVIEW_RETURN = 1099;
    private ActionBar actionbar;
    private RecyclerView recyclerView;
    private MyUploadPageAdapter recyclerViewAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<String> adapterData;
    private int final_loading;
    private String musername = "";
//ipadres info
    private String ipadres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_upload_page);




        Intent i = getIntent();
        musername = i.getStringExtra("username");
        ipadres = i.getStringExtra("ip");
//establish maximum loading
        new getmaxandsetupadapter(musername).execute();

//action bar setting
        SetUpActionBar(actionbar);

//Set up recyclerview and its linear layout manager
        recyclerView = findViewById(R.id.AMUP_recycleview);

        //gra = new LinearLayoutManager(MyUploadPage.this);
//spanCount: vertical column
        gridLayoutManager = new GridLayoutManager(MyUploadPage.this, 3);


        recyclerView.setLayoutManager(gridLayoutManager);

//setup adapter data(moved to onpostexecution for better accuracy










    }

    private List<String> getfirstdata(int maxloading) {
        List<String> listObject = new ArrayList<String>();
        for(int i = 0;i < (maxloading + 1);i++){
               listObject.add(Integer.toString(i));

            }

            return listObject;
    }

    private void SetUpActionBar(ActionBar actionbar) {
        actionbar = getSupportActionBar();

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);

    }
    //action bar
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }

    private class getmaxandsetupadapter extends AsyncTask<Void,Void,Void> {
        private String message;
        private String musername;

                private getmaxandsetupadapter(String username){
            this.musername = username;


                }
        @Override
        protected Void doInBackground(Void... voids){
            //System.out.println("mark 4");
            String url;
            String url1;
            url1 = "getmaxloadinguserpics.php";

            url = ipadres + url1;
            URL urlobj = null;
            String msg = "";

            try {
                String c,e,f ;
//encode messsage
                e = URLEncoder.encode("username", "UTF-8");
                f = URLEncoder.encode(musername, "UTF-8");
                c = "&" + e + "=" + f;
                byte[] postDataBytes = c.getBytes();

//internet connection
                urlobj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) urlobj.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                OutputStream out = new BufferedOutputStream(con.getOutputStream());
                out.write(postDataBytes);
                out.close();

// input reader
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                line = rd.readLine();
                message = line;
                rd.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
            //TextView textview = (TextView)findViewById(R.id.textView);
            //textview.setText(message);

            System.out.println("The message from myuploadpage is " + message);

            final_loading = Integer.parseInt(message);

// get the first data and set up adatper
            adapterData = getfirstdata(final_loading);

            recyclerViewAdapter = new MyUploadPageAdapter(MyUploadPage.this, adapterData, musername, ipadres);
            recyclerView.setAdapter(recyclerViewAdapter);

        }




    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RC_PICTUREVIEW_RETURN) {

//refresh adapter to reflect change
           recyclerView.setAdapter(null);
          new getmaxandsetupadapter(musername).execute();


            }
        }
    }
}
