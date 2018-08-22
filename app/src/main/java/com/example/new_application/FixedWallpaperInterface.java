package com.example.new_application;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.ContentValues.TAG;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

/**
 * Update V1.0: Added file download with file name
 * **/

public class FixedWallpaperInterface extends Activity {
    private ImageView imageview;
    private ActionBar actionBar;
    private static final int EXTERNAL_STORAGE_REQUEST_CODE = 123;
    private byte[] mbyte;
    private String uri;
    private String action = "";
    private Bitmap mbitmap;
    private Button conrim;

    private View mProgressView;

    private String picName = "";


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.R.color.transparent);
        }


        super.onCreate(savedInstanceState);

        System.out.println("From fixedwallpaper speed test end");


        setContentView(R.layout.activity_fixed_wallpaper_interface);
//getting information(action, uri, picName)from intent
        Intent tempintent = getIntent();
        action = tempintent.getStringExtra("action");
        uri = tempintent.getStringExtra("image");
        picName = tempintent.getStringExtra("picname");

//set up important view that is related to this thread
        imageview = (ImageView) findViewById(R.id.imageViewAFWI);
        System.out.println("hi123");

//background setup bitmap from its url link and set progress bar here if possible
        mProgressView = findViewById(R.id.FWPI_progressbar);
        showProgress(true);
        new setupimage().execute();




//initializaing action bar
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbarlayoutfor_a_f_w_i,
                null);
        actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        final int actionBarColor = getResources().getColor(R.color.transparent_color);
        actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));
        conrim = (Button) findViewById(R.id.afwi_confirm);
        conrim.setText("Confirm");




//request imageview layout informaiton and ready for resizing

        imageview.requestLayout();
        DisplayMetrics size = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindowManager().getDefaultDisplay().getRealMetrics(size);
        }

//imageview height and width
        imageview.getLayoutParams().width = size.widthPixels;
        imageview.getLayoutParams().height = size.heightPixels;



//please make sure mbitmap is nonnull here
       // while(mbitmap == null){}





//creating bytearray for bitmap and image scale processing;creating bytearry takes large amount of time, suggesting bypassing it in order not to affect user experience
     // method 1: using splash screen in different thread
     // method 2: creating new thread for processing mbyte:





        // method 3:

    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

           imageview.setVisibility(show ? View.GONE : View.VISIBLE);
            imageview.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                   imageview.setVisibility(show ? View.GONE : View.VISIBLE);
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
             imageview.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("permission granted");
                            download(mbitmap, picName);
                    FixedWallpaperInterface.this.finish();
                } else {
                    Toast.makeText(this,  "Read/Write External Storage Not Granted", Toast.LENGTH_LONG).show();
                }
        }
    }

    private void download(Bitmap bitmap, String picName) {
            byte[] bytearray = bitmaptoarray(bitmap);
        String uri;
        uri = writeToFile(bytearray);
        Uri u = Uri.parse(uri);
        FixedWallpaperInterface.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, u));

    }

    private String writeToFile(byte[] data) {
        System.out.println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));



// Get the directory for the user's public pictures directory.
          final File path = Environment.getExternalStoragePublicDirectory
                  (
                          //Environment.DIRECTORY_PICTURES
                          Environment.DIRECTORY_PICTURES + "/new_application/"
                  );

         if (!path.exists()) {
              Log.d(TAG, "Folder doesn't exist, creating it...");
              boolean rv = path.mkdir();
               Log.d(TAG, "Folder creation " + ( rv ? "success" : "failed"));
           } else {
               Log.d(TAG, "Folder already exists.");
            }


 //creation of file with name
        final File file = new File(path, picName);



        // Save your stream, don't forget to flush() it before closing it.
        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            fOut.write(data);





            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        String uri = (Uri.fromFile(file)).toString();
        System.out.println(uri);
        return uri;
    }

    private byte[] bitmaptoarray(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
         byte[] byteArray = byteArrayOutputStream .toByteArray();
        return byteArray;
    }

    private byte[] createbytearray(String uri){
        Uri uri123 = Uri.parse(uri);


        File file;
        byte[] byteee = null;

        file = new File(uri123.getPath());

        try {
            FileInputStream io = new FileInputStream(file);
            ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
            int i;
            i = io.read();
            while (i != -1) {
                bytearray.write(i);
                i = io.read();
            }
            io.close();
            byteee =  bytearray.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return byteee;
    }

    private class setwallpaper extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            final WallpaperManager wmng = (WallpaperManager) FixedWallpaperInterface.this.getSystemService(FixedWallpaperInterface.this.WALLPAPER_SERVICE);
            try {
//setbitmap takes too long and should be ran in different thread
                wmng.setBitmap(mbitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

 //exit this activity
            FixedWallpaperInterface.this.finish();

        }
    }



    private class setupimage extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids){
            mbyte = createbytearray(uri);

            System.out.println("hi456");
            //byte[] imagebyte = tempintent.getByteArrayExtra("image");

            final Bitmap scaledimg = BitmapFactory.decodeByteArray(mbyte, 0, mbyte.length);
            mbitmap = scaledimg;






            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
            showProgress(false);
            imageview.setImageBitmap(mbitmap);


//confirm listener; put it after onpostexecute in case of app crash
            conrim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (action != null && action.equals("download")) {
    //permission requesting at run time
                        if (ContextCompat.checkSelfPermission(FixedWallpaperInterface.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(FixedWallpaperInterface.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(FixedWallpaperInterface.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST_CODE);
                        } else {
                            download(mbitmap, picName);
                            FixedWallpaperInterface.this.finish();
                        }

                    } else {
                       // final WallpaperManager wmng = (WallpaperManager) FixedWallpaperInterface.this.getSystemService(FixedWallpaperInterface.this.WALLPAPER_SERVICE);

                        showProgress(true);
                        new setwallpaper().execute();
                        //try {

                            //System.out.println("fixedwallpaperinterface testspeed start");
         //user interface experience--, setbitmap takes too long and should be ran in different thread
                          //  wmng.setBitmap(mbitmap);
                          //  System.out.println("fixedwallpaperinterface testspeed end ");
                          //  FixedWallpaperInterface.this.finish();

                    //    } catch (IOException e) {
                    //        e.printStackTrace();
                    //    }


                    }
                }
            });


        }



    }



}
