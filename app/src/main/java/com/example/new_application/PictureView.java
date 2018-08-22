package com.example.new_application;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.ContentValues.TAG;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class PictureView extends Activity {
    ImageView imageview;
    private View mProgressView;

    private float picsizeX;
    private float picsizeY;
    private float scalefactor;
    private float ScreenSizeX;
    private float ScreenSizeY;
    private int maxXX;
    public int maccumulator;
    private Toolbar toolbar;
    boolean sentinel = true;
    private ActionBar actionBar;
    private byte[] byteArray;
    private static final int EXTERNAL_STORAGE_REQUEST_CODE = 123;
    private Bitmap mbitmap;
   private float displayedWidth;
    private float displayedHeight;
    private int setHeight;
    private int setWidth;

    private Boolean isupload = false;

    private String username;
    private String picName;

    private int RC_PICTUREVIEW_RETURN = 1099;
   // private String encodedImage;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//setting Status bar properties: fullscreen mode
     //   requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//setting status bar color
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           getWindow().setStatusBarColor(android.R.color.transparent);
       }


        // getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE);

//above has to be done before oncreate

        super.onCreate(savedInstanceState);

       // getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_picture_view);


//internet connection
        Intent hi = getIntent();
        picName = hi.getStringExtra("picname");
        System.out.println("from pictureview and picname is " + picName);

//check if it is requesting from myuploadpage
        isupload = hi.getBooleanExtra("IsUpload",false);

//get username
        if(isupload == true){
            username = hi.getStringExtra("username");
        }

//setup image view and url address
        imageview = (ImageView) findViewById(R.id.imageView4);
        String url;
        url = "http://192.168.0.49:81/images/" + picName;
        //System.out.println(picsizeX);
        //Picasso.get().load(url).

//get actual windows screen size without decor using getrealmetrics
        DisplayMetrics size = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindowManager().getDefaultDisplay().getRealMetrics(size);
        }
        ScreenSizeX = size.widthPixels;
        ScreenSizeY = size.heightPixels;


//picasso getting bitmap method, study their library please
        Picasso.get().load(url).into(new Target() {
                    @Override
                    public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from) {
                        /* Save the bitmap or do something with it here */
                        //DisplayMetrics size = new DisplayMetrics();
                        mbitmap = bitmap;
                        picsizeX = bitmap.getWidth();
                        picsizeY = bitmap.getHeight();

                       // float[] f = new float[9];
                      //  imageview.getImageMatrix().getValues(f);

                        imageview.requestLayout();

//calculate required imageview size based on dimension ratio, a little bit of math
                        setHeight = Math.round(ScreenSizeY);
                        setWidth = Math.round(((ScreenSizeY/picsizeY)*picsizeX));

                        if(setWidth <= ScreenSizeX){
                            setWidth = Math.round(ScreenSizeX);
                        }

//set imageview height/width based on calculation property
                        imageview.getLayoutParams().height = setHeight;
                        imageview.getLayoutParams().width  =  setWidth;
                        imageview.setScaleType(ImageView.ScaleType.FIT_XY);




                      //  displayedWidth = picsizeX * f[Matrix.MSCALE_X];
                      //  displayedHeight = picsizeY * f[Matrix.MSCALE_Y];




                     //   System.out.println("don't you fking kidding me" + ScreenSizeX);
                      //  System.out.println("don't you fking kidding me" + ScreenSizeY);
                     //   if((int)picsizeY < 1808) {

                    //        scalefactor = 1808 / picsizeY;
                    //        picsizeX = scalefactor * picsizeX;
                    //    }

                    //    System.out.println(displayedWidth);
                   //     System.out.println(displayedHeight);


//converting bitmap to bytearray
                      //  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                     //   bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                     //   byteArray = byteArrayOutputStream .toByteArray();
                        // encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);




            //System.out.println(bitmap.getWidth());
                        // Set it in the ImageView
                        //theView.setImageBitmap(bitmap);
                    }
                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable){

                    }
                });
//unstable method of getting bitmap for picasso
       // Bitmap imag = ((BitmapDrawable)imageview.getDrawable()).getBitmap();
     //  System.out.println(imag.getWidth());
        //float PicSizeX;


//load image into imageview
        //Picasso.get().load(url).into(imageview);
        imageview.setImageBitmap(mbitmap);

//preparing for the ontouch event calculation(max scrolling range)
        maxXX = Math.round(setWidth - ScreenSizeX);
       /*
        if(maxX <= 0){
            maxX = 0;
        }
        */
            System.out.println("maxX" + maxXX);

//inflate layout file from layout folder

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbarlayout,
                null);
      //  final ViewGroup bottomtoolbarlayout = (ViewGroup) getLayoutInflater().inflate(R.layout.bottomtoolbarlayout,null);



//action customization & set up


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          // actionBar= (Toolbar) findViewById(R.id.toolbar1234);
          //  setActionBar(actionBar);
          //  actionBar.setTitle("toolbar");
            toolbar =  findViewById(R.id.toolbarViewPageBottom);
            actionBar = getActionBar();
        }


        actionBar.setDisplayShowHomeEnabled(false);
       actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

//adding color for the action bar
        final int actionBarColor = getResources().getColor(R.color.transparent_color);
        actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));
        toolbar.setBackgroundDrawable(new ColorDrawable(actionBarColor));
        mProgressView = findViewById(R.id.APV_progressbar);

//checking to see if this intent is coming from uplaod page or not; below is if no situation
        if(isupload == false) {

//setting up button
            Button actionBarsets = (Button) findViewById(R.id.set_wallpaperSrol);
            actionBarsets.setText("Set Scrollable Wallpaper");

            Button backbutton = (Button) findViewById(R.id.back_PV);
            backbutton.setText("Back");

            Button actionBarSetf = (Button) findViewById(R.id.set_wallpaperFix);
            actionBarSetf.setText("Set Fixed Wallpaper");


            Button download = (Button) findViewById(R.id.Download_PV);
            download.setText("Download");

//button listener module
            backbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PictureView.this.finish();
                }
            });

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//return a bitmap that is scaled according to scrollable or not
                    Bitmap scaledbackimg = scaledimagebylocation(maccumulator, Math.round(setWidth), Math.round(picsizeX), Math.round(picsizeY), mbitmap, (int) ScreenSizeX, (int) ScreenSizeY, false);

//turning bitmap into bytearray
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    scaledbackimg.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray123 = byteArrayOutputStream.toByteArray();

//creating uri for the image file created in the data folder
                    String uri123 = writeToFile(byteArray123);

//starting new activity for confirmation
                    Intent temintent = new Intent(PictureView.this, FixedWallpaperInterface.class);
                    scaledbackimg.recycle();
                    temintent.putExtra("image", uri123);
                    temintent.putExtra("action", "download");
                    temintent.putExtra("picname", picName);
                    System.out.println("download");
                    startActivity(temintent);
                    PictureView.this.finish();


                }
            });


            actionBarsets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //   if (ContextCompat.checkSelfPermission(PictureView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(PictureView.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //       ActivityCompat.requestPermissions(PictureView.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST_CODE);
                    // } else {
                  /*
                    System.out.println("granted permission");
                    String uri;
                    uri = writeToFile(byteArray);


                   final WallpaperManager wmng = (WallpaperManager) PictureView.this.getSystemService(PictureView.this.WALLPAPER_SERVICE);

//get content scheme type of the image



//Update the System
                    Uri u = Uri.parse(uri);
                    PictureView.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, u));

//Get the abs path using a file, this is important
                    File wallpaper_file = new File(u.getPath());
                    Uri contentURI = getImageContentUri(PictureView.this, wallpaper_file.getAbsolutePath());
*/


                    Bitmap scaledbackimg = scaledimagebylocation(maccumulator, Math.round(setWidth), Math.round(picsizeX), Math.round(picsizeY), mbitmap, (int) ScreenSizeX, (int) ScreenSizeY, true);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    scaledbackimg.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray123 = byteArrayOutputStream.toByteArray();
                    String uri123 = writeToFile(byteArray123);
                    Intent temintent = new Intent(PictureView.this, FixedWallpaperInterface.class);
                    scaledbackimg.recycle();
                    temintent.putExtra("image", uri123);
                 //  System.out.println("from pictureview testing speed start");
                    startActivity(temintent);
                    PictureView.this.finish();


                    //wmng.setWallpaperOffsetSteps(1,1);
                    // wmng.suggestDesiredDimensions((int)400,(int)400);

                    //try {
                    //      wmng.setBitmap(mbitmap);
                    //    } catch (IOException e) {
                    //         e.printStackTrace();
                    //     }


                    //  }


                }
            });


//imageview listener mistake: 1, for accurate handling, variable memory storage should be within context class not global class eg. accumulator 2, never let INTeger(not uncertain) deicde next move

            final int max = maxXX;
            imageview.setOnTouchListener(new View.OnTouchListener() {
                float currentXPos;
                int accumulator;
                int changedX;
                boolean localsentinel = false;
                float fakeaccumulator;
                float tacker;
                int fixevelocity = 8;
                // int movebydistance;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    float changingXPos;
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            currentXPos = motionEvent.getX();


                            if (sentinel == true) {

                                View decorView;
                                decorView = getWindow().getDecorView();
// Hide the status bar.
                                // int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
                                // actionBar = getActionBar();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    actionBar.hide();
                                    toolbar.setVisibility(View.INVISIBLE);

                                }

                                sentinel = false;
                            } else {

                                View decorView;
                                decorView = getWindow().getDecorView();
// Hide the status bar.
                                // int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
                                decorView.setSystemUiVisibility(uiOptions);
                                getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
                                // ActionBar actionBar = getActionBar();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    actionBar.show();
                                    toolbar.setVisibility(View.VISIBLE);
                                }
                                sentinel = true;

                            }

                            // View decorView = getWindow().getDecorView();
// Hide the status bar.
                            // int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                            // int uiOptions = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                            //   decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
                            //  ActionBar actionBar = getActionBar();
                            //  actionBar.show();


                            break;

                        case MotionEvent.ACTION_MOVE:
                            changingXPos = motionEvent.getX();
                            changedX = (int) (currentXPos - changingXPos);
                            if (changingXPos < currentXPos) {
                                if (accumulator == max) {
                                    changedX = 0;

                                }

                                if (accumulator < max) {
                                    accumulator = accumulator + (changedX);
                                }
                                if (accumulator > max) {
                                    changedX = max - (accumulator - changedX);
                                    accumulator = max;


                                }

                            }
                            if (changingXPos > currentXPos) {
                                if (accumulator == 0) {
                                    changedX = 0;

                                }

                                if (accumulator > 0) {
                                    accumulator = accumulator + changedX;
                                }
                                if (accumulator < 0) {
                                    changedX = 0 - (accumulator - changedX);
                                    accumulator = 0;


                                }

                            }

                       /*
                        if(localsentinel == false) {
                            changingXPos = motionEvent.getX();
                            changedX = changingXPos - currentXPos;
                            if(changedX < 0) {
                                fixevelocity = 8;
                            }else if(changedX > 0){
                                fixevelocity = -8;
                           }
                          //  if(-(accumulator += changedX) >= Math.round(maxX)){
                           //     accumulator += (-accumulator - maxX);
                            //    changedX = changedX - (-accumulator - maxX);
                          //  }

                            if((accumulator += fixevelocity) >= Math.round(maxXX) ){
                                accumulator -= fixevelocity;
                                fixevelocity = Math.round(maxXX) % fixevelocity;
                                accumulator += fixevelocity;

                            }



                            //accumulator += fixevelocity;
                            fakeaccumulator = accumulator;
                         //   currentXPos = changingXPos;
                        }else{
                            changingXPos = motionEvent.getX();
                            changedX = changingXPos - currentXPos;
                           // accumulator += changedX;


                            if(changedX < 0) {
                                fixevelocity = 8;
                            }else if(changedX > 0){
                                fixevelocity = -8;

                            }

                                fakeaccumulator += fixevelocity;


                          //  currentXPos = changingXPos;

                            if(fakeaccumulator  <= accumulator){
                                accumulator = fakeaccumulator;
                                localsentinel = false;

                            }


                        }
                        */
                            maccumulator = accumulator;
                            System.out.println(accumulator);
                            // System.out.println("maccumulator" + maccumulator);
                            imageview.scrollBy(changedX, 0);
                            currentXPos = changingXPos;

                            break;
                    }

                    // System.out.println();
                    // System.out.println(accumulator);
                    //   if(((accumulator) <= Math.round(maxXX) && (accumulator) >= 0) || ((fakeaccumulator) < Math.round(maxXX) && (fakeaccumulator) >= 0)) {
                    //     localsentinel= false;


                    //  fixevelocity = 8;

                    //  }else{

                    //    if(-accumulator >= Math.round(maxXX)){
                    //    accumulator  =  -Math.round(maxXX);
                    //   }else if(-accumulator <= 0){
                    //     accumulator = 0;
                    // }


                    //  localsentinel = true;

                    //}

                    return true;
                }
            });

            actionBarSetf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // if (ContextCompat.checkSelfPermission(PictureView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(PictureView.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //     ActivityCompat.requestPermissions(PictureView.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST_CODE);
                    // } else{
 /*   save for later download funciton
                    System.out.println("granted permission");
                    String uri;
                    uri = writeToFile(byteArray);


                    final WallpaperManager wmng = (WallpaperManager) PictureView.this.getSystemService(PictureView.this.WALLPAPER_SERVICE);

//get content scheme type of the image

//Update the System
                    Uri u = Uri.parse(uri);
                    PictureView.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, u));

//Get the abs path using a file, this is important
                    File wallpaper_file = new File(u.getPath());
                    Uri contentURI = getImageContentUri(PictureView.this, wallpaper_file.getAbsolutePath());
                    */
                    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //  wmng.clearWallpaperOffsets(view.getWindowToken());
                    // wmng.clearWallpaper();


                    // System.out.println(maccumulator/setWidth);


//return a bitmap that is scaled according to scrollable or not
                    Bitmap scaledbackimg = scaledimagebylocation(maccumulator, Math.round(setWidth), Math.round(picsizeX), Math.round(picsizeY), mbitmap, (int) ScreenSizeX, (int) ScreenSizeY, false);

//turning bitmap into bytearray
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    scaledbackimg.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray123 = byteArrayOutputStream.toByteArray();
//creating uri for the image file created in the data folder
                    String uri123 = writeToFile(byteArray123);
//starting new activity for confirmation
                    Intent temintent = new Intent(PictureView.this, FixedWallpaperInterface.class);
                    scaledbackimg.recycle();
                    temintent.putExtra("image", uri123);
                    startActivity(temintent);
                    PictureView.this.finish();

                    // uri = writeToFile(byteArray123);


                    // Intent hi = wmng.getCropAndSetWallpaperIntent(contentURI);
                    //  startActivity(hi);
                    // wmng.setWallpaperOffset(0,0,0);
                    // wmng.suggestDesiredDimensions((int)ScreenSizeX,(int)ScreenSizeY);
                    // }
                    //  }
                }
            });

        }else{
//setting up button
            Button actionDelete = (Button) findViewById(R.id.set_wallpaperSrol);
            actionDelete.setText("Delete This Image");

            Button backbutton = (Button) findViewById(R.id.back_PV);
            backbutton.setText("Back");

//button listener module
            backbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PictureView.this.finish();
                }
            });

//Delete button aciton listener
            actionDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//set progress bar and internet connection to deliver delete request
                showProgress(true);
//reqeusting internet connection
                new deletegame(picName,username).execute();

                }
            });


        }


    }


    private class deletegame extends AsyncTask<Void,Void,Void> {
        private String message;
        private String picname;
        private String username;
        protected deletegame(String picname, String username){
             this.picname = picname;
             this.username = username;

        }

        @Override
        protected Void doInBackground(Void... voids){
            //System.out.println("mark 4");
            String url;
            String url1;

            url = "http://192.168.0.49:81/removeImage.php";
            URL urlobj = null;
            String msg = "";
            //String msg = "";

            try {
//encode words
                String a, b,c,e,f ;
                e = URLEncoder.encode("name", "UTF-8");
                f = URLEncoder.encode(this.picname, "UTF-8");
                a = URLEncoder.encode("username", "UTF-8");
                b = URLEncoder.encode(this.username, "UTF-8");
                c = "&" + e + "=" + f + "&" + a + "=" + b;
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
            else{
//on success
    //show progress bar
                showProgress(false);

    //set activity back and request refresh adapter
                Intent i = new Intent();
                setResult(RC_PICTUREVIEW_RETURN,i);
                PictureView.this.finish();

            }

        }



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


    public Bitmap scaledimagebylocation(int maccumulator,int setWidth ,int picsizeX,int picsizeY ,Bitmap mbitmap, int ScreenSizeX, int ScreenSizeY, Boolean Scrollable){

       int realX = Math.round(((float)maccumulator/setWidth) * picsizeX);

       int realwidth = Math.round(((float)ScreenSizeX/setWidth)*picsizeX);


       // 10 percent increase in size
       if(Scrollable == true){
                ScreenSizeX = (int)Math.round(ScreenSizeX + (ScreenSizeX * 0.1));

       }


       System.out.println("realX" + realX);
       System.out.println("realwidth" + realwidth);
       Bitmap backimg = Bitmap.createBitmap(mbitmap,realX,0,realwidth,Math.round(picsizeY));
       Bitmap scaledbackimg = Bitmap.createScaledBitmap(backimg, ScreenSizeX, ScreenSizeY,false);

       //Garbage collection
       //backimg.recycle();
       return scaledbackimg;
   }




    public String writeToFile(byte[] data)
    {
       // System.out.println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));



// Get the directory for the user's public pictures directory.
      //  final File path = new File(this.getFilesDir(), "/address/");

      //  if (!path.exists()) {
      //      Log.d(TAG, "Folder doesn't exist, creating it...");
      //      boolean rv = path.mkdir();
     //       Log.d(TAG, "Folder creation " + ( rv ? "success" : "failed"));
     //   } else {
     //       Log.d(TAG, "Folder already exists.");
    //    }


 //creation of file with name; this filesdir does not require permission
        final File file = new File(this.getFilesDir(), "/temp.jpg" );



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
//getting uril from file
        String uri = (Uri.fromFile(file)).toString();
        System.out.println(uri);
        return uri;
    }

//convert uri to content uri
    public static Uri getImageContentUri(Context context, String absPath) {
        Log.v(TAG, "getImageContentUri: " + absPath);

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[] { MediaStore.Images.Media._ID }
                , MediaStore.Images.Media.DATA + "=? "
                , new String[] { absPath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , Integer.toString(id));

        } else if (!absPath.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, absPath);
            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("permission granted");
                    writeToFile(byteArray);
                } else {
                    Toast.makeText(this,  "Read/Write External Storage Not Granted", Toast.LENGTH_LONG).show();
                }
        }
    }

}
