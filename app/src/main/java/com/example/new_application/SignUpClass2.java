package com.example.new_application;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SignUpClass2 extends AppCompatActivity {


    private TextView textview5;
    private Button Bconfirm;
    private EditText edittext4;
    private EditText edittext5;
    private TextView errortext;
    private String usergid;
    private String fullname;
    private ActionBar actionbar;
    private int RC_RETURN = 1091;
//ipadred
    private String ipadres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_sign_up_class3);






//action bar setting
        SetUpActionBar(actionbar);


//initilizaing View components
        errortext = (TextView) findViewById(R.id.ASU2_errormessage);
        textview5 = (TextView)findViewById(R.id.textView5);
        Bconfirm = (Button)findViewById(R.id.button9);
        edittext4 = (EditText)findViewById(R.id.editText4);
        edittext5 = (EditText)findViewById(R.id.editText5);

//setting edittext color
        edittext4.getBackground().setColorFilter(getResources().getColor(R.color.android_green), PorterDuff.Mode.SRC_ATOP);
        edittext5.getBackground().setColorFilter(getResources().getColor(R.color.android_green), PorterDuff.Mode.SRC_ATOP);





// intent get information
        Intent intent = getIntent();
        final String username;
        final String email;
        username = intent.getStringExtra("user_name");
        email = intent.getStringExtra("e_mail");
        usergid = intent.getStringExtra("usergid");
        fullname = intent.getStringExtra("fullname");
        ipadres = intent.getStringExtra("ip");
        System.out.println(email);
        textview5.setText("Hello, " + username + " Please create your own Password");

        edittext5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
               if(errortext.getVisibility() == (View.VISIBLE)) {
//back to normal state after focus(cursor change ) change in edittext
                   errortext.setVisibility(View.GONE);
                   edittext5.getBackground().setColorFilter(getResources().getColor(R.color.android_green), PorterDuff.Mode.SRC_ATOP);
               }
            }
        });

        Bconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(verifypsw()){

                   if(usergid.equals("")) {
                       connect_database(username, edittext4.getText().toString(), email);
                   }else{
                       new google_verify(username,fullname,email,usergid).execute();
                   }
                }else{
                   System.out.println("nope1");
                }
            }
        });
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

    private boolean verifypsw() {
        String psw1 = edittext4.getText().toString();
        String psw2 = edittext5.getText().toString();
        View focusview = null;
        if(psw1.equals(psw2)){


            return true;
        }else{
// changing the underline color of edittext
            edittext5.getBackground().setColorFilter(getResources().getColor(R.color.pure_red), PorterDuff.Mode.SRC_ATOP);
//set cursor posiiton
            focusview = edittext5;
            focusview.requestFocus();

//set text color
            errortext.setTextColor(getResources().getColor(R.color.pure_red));
//Visibility
          //  errortext.setText("");
            errortext.setVisibility(View.VISIBLE);


            return false;
        }
    }


    private void connect_database(String username, String psw, String email) {
           new record_database(psw,username,email).execute();

    }

    public class record_database extends AsyncTask<Void,Void,Void> {


        private final String mpsw;
        private final String musername;
        private final String memail;
        private HttpResponse response;
        record_database(String psw, String username, String email){
            //   midtoken = account.getIdToken();
            //   mname = account.getDisplayName();
            mpsw = psw;
            musername = username;
            memail = email;
        }
        @Override
        protected Void doInBackground(Void... voids){
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ipadres + "pswadd.php");

            List namevalue = new ArrayList(3);
            namevalue.add(new BasicNameValuePair("psw", mpsw));
            namevalue.add(new BasicNameValuePair("username", musername));
            namevalue.add(new BasicNameValuePair("email",memail));

            try {
                httppost.setEntity(new UrlEncodedFormEntity(namevalue));
                response = httpClient.execute(httppost);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){

            try {
                HttpEntity responsebody = response.getEntity();
                BufferedReader reader;
                StringBuilder sb;
                String line;
                String result;
                JSONObject responsejson;
                InputStream instream = responsebody.getContent();
                reader = new BufferedReader(new InputStreamReader(instream));

                sb = new StringBuilder();
                line = null;
                String test = null;
                while ((line = reader.readLine()) != null){
                    // System.out.println(line.charAt(0));
                    if(line.charAt(0) == '{') {
                        sb.append(line + "\n");
                    }
                }
                result = sb.toString();
                responsejson = new JSONObject(result);

                if(responsejson.get("status").equals(1)){
                    Intent tempintent = new Intent();
                    tempintent.putExtra("memail",memail);
                    tempintent.putExtra("musername",musername);
                    setResult(RC_RETURN,tempintent);
                    finish();

                }else{

                    //animation progress bar here

                    //System.out.println("nope2");
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public class google_verify extends AsyncTask<Void,Void,Void>{

        private final String midtoken;
        private final String mname;
        private final String memail;
        private final String musername;
        private HttpResponse response;
        google_verify(String username, String fullname, String email, String usergid){
            midtoken = usergid;
            mname = fullname;
            memail = email;
            musername = username;
            System.out.println(midtoken);
            System.out.println(mname);
            System.out.println(memail);
            System.out.println(musername);
        }
        @Override
        protected Void doInBackground(Void... voids){
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ipadres + "/googleRegister.php");


            List namevalue = new ArrayList(4);
            namevalue.add(new BasicNameValuePair("idtoken", midtoken));
            namevalue.add(new BasicNameValuePair("fullname", mname));
            namevalue.add(new BasicNameValuePair("email", memail));
            namevalue.add(new BasicNameValuePair("username", musername));

            try {
                httppost.setEntity(new UrlEncodedFormEntity(namevalue));
                response = httpClient.execute(httppost);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){

            try {
                HttpEntity responsebody = response.getEntity();
                InputStream instream = responsebody.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                StringBuilder sb = new StringBuilder();
                String line = null;
                String test = null;
                while ((line = reader.readLine()) != null){
                    // System.out.println(line.charAt(0));
                    if(line.charAt(0) == '{') {
                        sb.append(line + "\n");
                    }
                }
                String result = sb.toString();

                JSONObject responsejson = new JSONObject(result);
                //System.out.println();


                //TextView textview = (TextView)findViewById(R.id.textView4);
             //   textview.setText((responsejson.get("username")).toString());

               if((responsejson.get("status".toString()).equals("1"))){
                   System.out.println("status ok");
                   Intent tempintent = new Intent();
                   tempintent.putExtra("memail",memail);
                   tempintent.putExtra("musername",musername);
                   setResult(RC_RETURN,tempintent);
                   finish();

                }else{
                   System.out.println("status is not ok");
               }
                //  JSONObject responsejson = new JSONObject(response.);
                //   responseBody = EntityUtils.toString(response.getEntity());
                // int statusCode = response.getStatusLine().getStatusCode();

                // if(responseBody.equals("repeat")){
                //     TextView textview = (TextView)findViewById(R.id.textView4);
                //    textview.setText(musername);
                //}
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


}
