package com.example.new_application;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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

public class SignUpClass extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

//user information section
    private EditText username;
    private AutoCompleteTextView email;
//

    private Button Bconfirm;
    private TextView textview7;
    private TextView textview8;
    private String usergid = "";
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 8011;
    private GoogleApiClient mGoogleApiClient;
    private String fullname = "";
    private int RC_RETURN = 1091;
    private android.support.v7.app.ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






//initializaing google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.server_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API).build();

// sign out in case of the autosign option has already been used
       signOut();


        setContentView(R.layout.activity_sign_up_class2);


//customizing action bar: to set up back button for the actionbar
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);



      //  textview7 = (TextView)findViewById(R.id.textView7);
      //  textview8 = (TextView)findViewById(R.id.textView8);

        username = (EditText) findViewById(R.id.ASU_username);
        email = (AutoCompleteTextView) findViewById(R.id.ASU_email);
        Bconfirm = (Button) findViewById(R.id.ASU_confirm);

//gooogle sign in api
        SignInButton signInButton = findViewById(R.id.sign_in_button1);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        setGooglePlusButtonText(signInButton,"Sign Up With Google");


        signInButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.sign_in_button1:
                        signIn();
                        break;

                }
            }
        }));


        Bconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ///if(formatcheck) {
                   verify_database();
               ///}else{

               //}
                }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }

//change text inside googlesign in button
    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("sign out complete");
            }
        });
    }

    private void signIn() {
//initializaing google intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        System.out.println("wtf");
        startActivityForResult(signInIntent,RC_SIGN_IN);


    }

    private void verify_database() {
        String username_local;
        String email_local;
        username_local = username.getText().toString();
        email_local = email.getText().toString();

        new uname_email_verify(email_local, username_local).execute();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            handleSignInResult(task);
        }
        if(requestCode == 1){
            if(resultCode == RC_RETURN){
                Intent tempintent = new Intent();
                tempintent.putExtra("musername", data.getStringExtra("musername"));
                tempintent.putExtra("memail", data.getStringExtra("memail"));
                System.out.println("SIGNUPCLASS CHECK WITH MUSERNAME" + data.getStringExtra("musername"));
                setResult(RC_RETURN,tempintent);
                finish();

            }

        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {

// account objects contain account information(email,name and etc)
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            email.setText(account.getEmail());
            usergid = account.getId();
            fullname = account.getDisplayName();

        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


    public class uname_email_verify extends AsyncTask<Void,Void,Void> {


        private final String memail;
        private final String musername;
        private HttpResponse response;
        private View focusView = null;
        private View focusView1 = null;
        uname_email_verify(String email, String username){
         //   midtoken = account.getIdToken();
         //   mname = account.getDisplayName();
            memail = email;
            musername = username;



        }
        @Override
        protected Void doInBackground(Void... voids){
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.0.49:81/nonGoogleV.php");

            List namevalue = new ArrayList(2);
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

                if((responsejson.get("status")).equals(0)){

// status ok startactivity for user to enter username
                    Intent hi = new Intent(SignUpClass.this, SignUpClass2.class);
                    hi.putExtra("user_name", musername);
                    hi.putExtra("e_mail",memail);
                    hi.putExtra("fullname", fullname);
                     hi.putExtra("usergid",usergid);
                    startActivityForResult(hi, 1);

// status not ok section: todo:relace them with autocomplete textview
                }else if((responsejson.get("status")).equals(1)){
                    username.setError("Screen Name has been used");
                    focusView = username;
                    focusView.requestFocus();
         //           textview7.setText("username have been used");
                 //   textview8.setText("email is ok");

                }else if((responsejson.get("status")).equals(2)){
                 //   textview7.setText("username is ok");
                  //  textview8.setText("email has been used ");
                    email.setError("Email has been used");
                    focusView = email;
                    focusView.requestFocus();

                }else{
                    //textview7.setText("username have been used");
                  //  textview8.setText("email has been used ");
                    email.setError("Email has been used ");
                    username.setError("username has been used");
                    focusView = email;
                    focusView1 = username;
                    focusView.requestFocus();
                    focusView1.requestFocus();

                }
               // TextView textview = (TextView)findViewById(R.id.textView4);
                //textview.setText((responsejson.get("status")).toString());


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
