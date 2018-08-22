package com.example.new_application;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, GoogleApiClient.OnConnectionFailedListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

// UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

//google client initialization
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;

// onactivity request codes
    private int RC_RETURN = 1091;
    private int RC_SIGN_IN = 8011;

//action bar
    private android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//action bar setting
        SetUpActionBar(actionBar);



// Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
//progress bar set up
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
//google sign in button
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

// google sign in initialization
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.server_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API).build();



        /*
// google silent sign in
        OptionalPendingResult<GoogleSignInResult> pendingResult =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (pendingResult.isDone()) {
            // There's immediate result available.
           // updateButtonsAndStatusFromSignInResult(pendingResult.get());


            GoogleSignInAccount account = pendingResult.get().getSignInAccount();
            System.out.println(account.getIdToken());
            TextView textview = (TextView)findViewById(R.id.textView4);
            textview.setText(account.getEmail());
           // System.out.println(pendingResult.get().getSignInAccount().getDisplayName());

        } else {
            // There's no immediate result ready, displays some progress indicator and waits for the
            // async callback.
           // showProgressIndicator();
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                   // updateButtonsAndStatusFromSignInResult(result);
             //       hideProgressIndicator();
                    System.out.println("hithere2");
                }
            });

        }
 */
        signInButton.setOnClickListener((new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.sign_in_button:
                        signIn();
                        break;

                }
            }
        }));
/*
        Button button6 = (Button)findViewById(R.id.button6);
        button6.setOnClickListener((new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.button6:
                        signOut();
                        break;
                }
            }
        }));
        */

        /*
        Button button7 = (Button)findViewById((R.id.button7));
        button7.setOnClickListener((new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.button7:
                    signup();
                    break;

                }
            }
        }));

*/
    }

    private void SetUpActionBar(ActionBar actionBar) {
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }
    /*
    private void signup() {

        Intent intent = new Intent(this,SignUpClass.class);
        startActivity(intent);


    }
*/
    /*
//also the silient sign in module saved for later use
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              //  TextView textview = (TextView)findViewById(R.id.textView4);
                //textview.setText("no current user");
            }
        });
    }
*/
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void signIn(){
        //RC_SIGN_IN = 56;
        signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        System.out.println("wtf");
        startActivityForResult(signInIntent,RC_SIGN_IN);

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

// Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

// Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

// Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

// Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private HttpResponse response;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
/*
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
*/


/*
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }
*/


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.0.49:81/nonGoogleSignInV.php");


            List namevalue = new ArrayList(2);
            namevalue.add(new BasicNameValuePair("email", mEmail));
            namevalue.add(new BasicNameValuePair("psw", mPassword));
           //namevalue.add(new BasicNameValuePair("email", memail));
            //  namevalue.add(new BasicNameValuePair("username", musername));

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


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
          /*
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
            */
            mAuthTask = null;
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
//initializaing json object
                JSONObject responsejson = new JSONObject(result);


                //System.out.println();
                //  TextView textview = (TextView)findViewById(R.id.textView4);
                // textview.setText((responsejson.get("username")).toString());
                String status = (responsejson.get("status")).toString();
                System.out.println("FROM login activity" + status);

                if(status.equals("3")){
                    Intent tempinten = new Intent();
                    tempinten.putExtra("musername",(responsejson.get("username")).toString());
                    tempinten.putExtra("memail",(responsejson.get("useremail")).toString());
                    setResult(RC_RETURN, tempinten);

                    finish();

                }else{
                    showProgress(false);
                    System.out.println("FAILED");

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



        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class google_verify extends AsyncTask<Void,Void,Void>{

        private final String midtoken;
        private final String mname;
        private final String memail;
       // private final String musername;
        private HttpResponse response;
        google_verify(GoogleSignInAccount account){
            midtoken = account.getIdToken();
            mname = account.getDisplayName();
            memail = account.getEmail();
           // musername = username;
            System.out.println(midtoken);
            System.out.println(mname);
            System.out.println(memail);
            showProgress(true);
         //   System.out.println(musername);
        }
        @Override
        protected Void doInBackground(Void... voids){
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.0.49:81/google.php");


            List namevalue = new ArrayList(3);
            namevalue.add(new BasicNameValuePair("idtoken", midtoken));
            namevalue.add(new BasicNameValuePair("fullname", mname));
            namevalue.add(new BasicNameValuePair("email", memail));
          //  namevalue.add(new BasicNameValuePair("username", musername));

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
 // internet response body(getting response from the server for the info sent above)
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


//initializaing json object
                JSONObject responsejson = new JSONObject(result);
//determining whether record exists in the server or not
                if(responsejson != null && (responsejson.get("status").equals("1"))) {
                    //System.out.println();
                    //  TextView textview = (TextView)findViewById(R.id.textView4);
                    // textview.setText((responsejson.get("username")).toString());

                    System.out.println("FROM login activity " + (responsejson.get("username")).toString());

//creates intent to send back data
                    Intent tempinten = new Intent();
                    tempinten.putExtra("musername", (responsejson.get("username")).toString());
                    tempinten.putExtra("memail", (responsejson.get("useremail")).toString());
                    setResult(RC_RETURN, tempinten);

                    finish();
                }else{
                    System.out.println("Failed" );


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                handleSignInResult(task);
            }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> donetask) {
       // String username;
       // EditText edittext = (EditText) findViewById(R.id.editText2);
       // username = edittext.getText().toString();



        try {

            GoogleSignInAccount account = donetask.getResult(ApiException.class);
            //username has to be sent at the same tiem as account object
            new google_verify(account).execute();






           // System.out.println(account.getEmail());
           // System.out.println(account.getGivenName());
           // System.out.println(account.getFamilyName());
           // System.out.println(account.getDisplayName());
           // System.out.println(account.getIdToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }


}

