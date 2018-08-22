package com.example.new_application;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
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


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfristapp.MESSAGE";
    private int PICK_IMAGE_REQUEST = 1;
    private Uri uri = null;
    private Bitmap bitmap;
    private String encoded_string, image_name;
    private String gametostore;
    private String username;
    private int RC_RETURN_UPLOAD = 3030;
    private ActionBar actionbar;
    private TextView textview;
    private View mcontent;
    private View mprogressbar;
    private EditText edittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//action bar setting
        SetUpActionBar(actionbar);

//getting game name and contributor from intent
        Intent i = getIntent();
        gametostore = i.getStringExtra("game");
        username = i.getStringExtra("username");

// setting up content and progress bar

       mcontent = findViewById(R.id.MAIN_scrollview);
       mprogressbar = findViewById(R.id.MAIN_progress);
        textview = findViewById(R.id.textView2);
        edittext = (EditText)findViewById(R.id.editText);

        edittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                textview.setVisibility(View.GONE);
                return true;
            }
        });

       edittext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               textview.setVisibility(View.GONE);
           }
       });

      edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View view, boolean b) {
              textview.setVisibility(View.GONE);
          }
      });
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mcontent.setVisibility(show ? View.GONE : View.VISIBLE);
           mcontent.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mprogressbar.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

           mprogressbar.setVisibility(show ? View.VISIBLE : View.GONE);
           mprogressbar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                   mprogressbar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mprogressbar.setVisibility(show ? View.VISIBLE : View.GONE);
           mcontent.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void SetUpActionBar(ActionBar actionbar) {

        actionbar = getSupportActionBar();

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }
    // switch to another tab method
  /*  public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);
    }
*/
    public void pickimage(View view){
        textview.setVisibility(View.GONE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

    }

    public void upload_server(View view){

        if(uri == null || edittext.getText().toString().equals("") || edittext.getText() == null){

            textview.setVisibility(View.VISIBLE);
            return;
        }else{
           new Encode_image().execute();
           showProgress(true);


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

                uri = data.getData();
            try {

//getting bitmap
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageview = (ImageView) findViewById(R.id.imageView);
                imageview.setImageBitmap(bitmap);


                //TextView textview = (TextView)findViewById(R.id.textView2);
                //textview.setText(uri.getPath().toString());



            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private class Encode_image extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] array = stream.toByteArray();
                encoded_string = Base64.encodeToString(array,0);
            } catch (IOException e) {
                e.printStackTrace();
            }





            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
            make_request();

        }



    }

    private void make_request() {

        RequestQueue requestqueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.0.49:81/index.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            //textview = (TextView)findViewById(R.id.textView2);
                        //    textview.setText(response);

                            if(response.equals("success")){

                                //textview.setText(response);
//update ui with success response
                                Intent i = new Intent();
                                setResult(RC_RETURN_UPLOAD,i);


                                showProgress(false);

                                finish();

                            }else{
                                textview.setText(response);
                            }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String name = edittext.getText().toString();
                HashMap<String,String> map = new HashMap<>();

                map.put("encoded_string",encoded_string);
                map.put("image_name", name + ".jpg");
                map.put("games",gametostore);
                map.put("username",username);


                return map;
            }
        };
        requestqueue.add(request);

    }


}





