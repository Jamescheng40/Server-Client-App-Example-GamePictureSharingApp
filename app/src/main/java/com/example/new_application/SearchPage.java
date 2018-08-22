package com.example.new_application;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

/** this method relies on obj.ArrayList to transfer data set in recyclerview, modify should pertain to the change of obj.ArrayList **/

public class SearchPage extends AppCompatActivity {
    private LinearLayoutManager linearLayoutManager;
    private List<String> mlistobj = null;
    private EditText edittext;
    private RecyclerView rv;
    private String username;
    private int RC_RETURN_UPLOAD = 3030;
    //private android.support.v7.app.ActionBar actionBar;
    private ImageView imageView;
    //listener module
    private Oncompleted oncompleted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

//image set up and initializaing back out funciton
        imageView = findViewById(R.id.sp_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchPage.this.finish();

            }
        });



//action bar set up
      //  SetUpActionBar(actionBar);





//null if string not found
        Intent i = getIntent();
        username = i.getStringExtra("username");


//set up linearmanager for recycle View
        linearLayoutManager = new LinearLayoutManager(SearchPage.this);
         rv = (RecyclerView) findViewById(R.id.SPrecyclerview);
//attach linearmanger to the recyclerview
        rv.setLayoutManager(linearLayoutManager);
        edittext = (EditText) findViewById(R.id.editText7);
//textchange listener for edittext
       edittext.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


               new SPgetsearchresult1(charSequence.toString()).execute();


//not up load and it should be search page
               if(username == null) {
//simulate loading screen; mlistobj must be nonnull at this point, otherwise would cause app crash
    //method 1(scrapped): using while loop to implement loading screen weekness: too energy consuming
                   //while (mlistobj == null) { }
    //method 2: using callback function to complete job
                  OnSetListObjCompletedListener(new Oncompleted() {
                      @Override
                      public void Oncomplete() {

    //set up adapter and turn off progressbar
                          System.out.println("Searchpage Oncomplete transfer setup adapter");
                          SearchViewRecyclerViewAdapter sd = new SearchViewRecyclerViewAdapter(SearchPage.this, mlistobj);
                          rv.setAdapter(sd);
                          mlistobj = null;
                      }
                  });



               }else if(username != null) {
//if this is upload, one should have username ready to pass


//simulate loading screen
                   OnSetListObjCompletedListener(new Oncompleted() {
                       @Override
                       public void Oncomplete() {
      //set up adapter and turn off progressbar

                           SelectGameRecyclerViewAdapter sga = new SelectGameRecyclerViewAdapter(SearchPage.this,mlistobj,username);
                           rv.setAdapter(sga);
                           mlistobj = null;
                       }
                   });



               }

           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });

/*
        RecyclerView rv = (RecyclerView) findViewById(R.id.SPrecyclerview);
        linearLayoutManager = new LinearLayoutManager(rv.getContext());
        rv.setLayoutManager(linearLayoutManager);

        while(mlistobj == null){}
        System.out.println("listtest: " + mlistobj.get(1));

        SearchViewRecyclerViewAdapter adapter = new SearchViewRecyclerViewAdapter();
        rv.setAdapter(adapter);
        */




    }

    //private void SetUpActionBar(ActionBar actionBar) {
    //    actionBar = getSupportActionBar();
    //    actionBar.setDisplayShowTitleEnabled(false);
   // }

//private listerner for local listobj nonNull completed event
    protected void OnSetListObjCompletedListener(Oncompleted oncompleted){
        this.oncompleted = oncompleted;
    }
    protected interface Oncompleted{
        void Oncomplete();

    }

    private List<String> getFirstData(){

        List<String> listObject = new ArrayList<String>();

        listObject.add("1");
        listObject.add("2");
        listObject.add("3");
        listObject.add("4");
        listObject.add("5");
        /*
        listObject.add("5");
        listObject.add("one");
        listObject.add("one");
        listObject.add("one");
        listObject.add("one");
        listObject.add("one");
        */
        return listObject;

    }


    public class SPgetsearchresult1 extends AsyncTask<Void,Void,List<String>> {

        private String mwrod;
        private List<String> listobj;



        public SPgetsearchresult1(String wrod) {
            mwrod = wrod;

            //System.out.println(mnumber);
            listobj = new ArrayList<String>();


        }
        @Override
        protected List<String> doInBackground(Void... voids) {
            System.out.println("mark 5");
            String url;
            url = "http://192.168.0.49:81/getGamesList.php";
            URL urlobj = null;
            try {

// encode string of a,b,c
                String a, b, c;
                a = URLEncoder.encode("keyword", "UTF-8");
                b = URLEncoder.encode(mwrod, "UTF-8");
                c = "&" + a + "=" + b;
                byte[] postDataBytes = c.getBytes();
//create url object and make it connect through android classes
                urlobj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) urlobj.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                OutputStream out = new BufferedOutputStream(con.getOutputStream());
                out.write(postDataBytes);
                out.close();
                // System.out.println("chck");
//please modify this later to account for connection lost
                int responseCode = con.getResponseCode();
                // System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;

                int i;
                i = 0;
// adding games name into the string object and for loading adapter
                while ((inputLine = in.readLine()) != null) {
                    listobj.add(inputLine);
                }
                in.close();

                // System.out.println(response.toString());
                con.disconnect();


            } catch (IOException e) {
                e.printStackTrace();
            }


            mlistobj = listobj;



            return listobj;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            System.out.println("Searchpage Oncomplete");
            oncompleted.Oncomplete();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RC_RETURN_UPLOAD) {

                finish();


            }
        }
    }

}
