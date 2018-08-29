package com.example.new_application;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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

public class SearchPageRecyclerViewHolder extends RecyclerView.ViewHolder  {
    public TextView textview;
    public ImageView imageview;
    public EditText textviewforresult;
    private Context mcontext;
    private String mgame;

    private View mProgressView;
    private View include;



//stuff should be passing to fragments class
    private int final_loading;

//ipadress
    private String ipadress;

    public SearchPageRecyclerViewHolder(View itemView, Context context, String ipadress) {
        super(itemView);
        this.mcontext = context;
        this.ipadress = ipadress;


//setting up components
        textview = (TextView) itemView.findViewById(R.id.test123);
        imageview = (ImageView) itemView.findViewById(R.id.avatar);


        textview.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


//progress bar begins
        include =  ((Activity)mcontext).findViewById(R.id.ASP_mainframe);
        mProgressView = ((Activity)mcontext).findViewById(R.id.ASP_progressbar);
        showProgress(true);


        if(textview.getText().toString().equals("Result Not Found, Click Here To Report This(What You Typed) Missing Game")){
            System.out.println("reporting an missing game ");
//get edittext from searchpage and send it to server
            EditText editText = ((Activity)mcontext).findViewById(R.id.editText7);
            String gametosend = editText.getText().toString();
            new sendmissinggame(gametosend).execute();

        }else {
//getting game name and ready for getmaxloading() processing
            mgame = textview.getText().toString();
            new getmaxloading().execute();

        }
    }
});
        // Intent i = new Intent();
        // i.putExtra("game",textView.getText().toString());
        // setResult(312,i);
        // finish();

    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = mcontext.getResources().getInteger(android.R.integer.config_shortAnimTime);

           include.setVisibility(show ? View.GONE : View.VISIBLE);
            include.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                   include.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            //  include.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    private class getmaxloading extends AsyncTask<Void,Void,Void> {
        private String message;
        @Override
        protected Void doInBackground(Void... voids){
            //System.out.println("mark 4");
            String url;
            String url1;

            url = ipadress + "getmaxloadingsearch.php";
            URL urlobj = null;
            String msg = "";
            //String msg = "";

            try {
//encode words
                String a, b,c,e,f ;
                e = URLEncoder.encode("games", "UTF-8");
                f = URLEncoder.encode(mgame, "UTF-8");
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






//buffer reader
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                line = rd.readLine();
                message = line;
                rd.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

//convert message to int
            if(message == null) {
                final_loading = 5;
            }else{     final_loading = Integer.parseInt(message);}

            //final_loading = 10;



//Thread freeze cancel
            /*
            synchronized (lock){
                lock.notifyAll();

            }
            */

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
//calling back to oncreateview when action completed
            //oncomplete.oncomplete()
            //TextView textview = (TextView)findViewById(R.id.textView);
            //textview.setText(message);
            // System.out.println("your searchpage message is " + message);
            //long code need to change
            //ininumber = Integer.parseInt(message);
            // System.out.println("your search page loading number is:" + message);

//here should cancel the progress bar


//intent from the searchpage context to return search result back to browse page
            System.out.println("from searchpagerecyclerviewholder the finalloading is"+ final_loading);
            Intent i = new Intent();

// sending game name and final_loading information to the browsepage for setting up viewpager adatper
            i.putExtra("finalloading",final_loading);
            i.putExtra("game",mgame);
            ((Activity)mcontext).setResult(312,i);
            ((Activity)mcontext).finish();

        }



    }


    private class sendmissinggame extends AsyncTask<Void,Void,Void> {
        private String message;
        private String gametosend;
        protected sendmissinggame(String gametosend){
            this.gametosend = gametosend;

        }

        @Override
        protected Void doInBackground(Void... voids){
            //System.out.println("mark 4");
            String url;
            String url1;

            url = ipadress  + "setMissingGame.php";
            URL urlobj = null;
            String msg = "";
            //String msg = "";

            try {
//encode words
                String a, b,c,e,f ;
                e = URLEncoder.encode("game", "UTF-8");
                f = URLEncoder.encode(this.gametosend, "UTF-8");
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

//buffer reader
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
//on failed listener
            if(message.equals("failed")){
                    System.out.println(message);
            }else if(message.equals("something is wrong")){ System.out.println(message);}
            else{((Activity)mcontext).finish();}

        }



    }


}
