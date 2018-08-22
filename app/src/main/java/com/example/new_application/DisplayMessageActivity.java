package com.example.new_application;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class DisplayMessageActivity extends AppCompatActivity {
    private String[] message = new String[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        /*
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView textview = findViewById(R.id.textView);
        textview.setText(message);
    */
    }

    public void change_images(View view){
            new getmessage().execute();
        // String msg;
        //msg = performPostCall("http://192.168.0.49:81/image_download.php");
        //System.out.println(msg);
        //tryLogin("adf","123");
      //  TextView textview = (TextView)findViewById(R.id.textView);
        //textview.setText(msg);


    }


/*
    public static void Connect(){
        String url;
        url = "http://192.168.0.49:81/image_download.php";
        URL urlobj = null;
        String msg = "";

        try {
            urlobj = new URL(url);
            URLConnection lu = urlobj.openConnection();
          //  System.out.println("1");
            BufferedReader rd = new BufferedReader(new InputStreamReader(lu.getInputStream()));
            String line = "", res = "";
            msg = rd.readLine();

            //msg = res;
            rd.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(msg);

    }
*/
/*
public String performPostCall(String requestURL) {
    URL url;
    String response = "";
    try {
        url = new URL(requestURL);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);

        conn.setDoInput(true);




//        int responseCode = conn.getResponseCode();

      //  if (responseCode == HttpsURLConnection.HTTP_OK) {
            //String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
           response = br.readLine();
       // } else {
         //   response = "";

      //  }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return response;
}

*/
/*
    protected void tryLogin(String mUsername, String mPassword)
    {
        HttpURLConnection connection;
    //    OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "username="+mUsername+"&password="+mPassword;

        try
        {
            url = new URL("http://192.168.0.49:81/image_download.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

          //  request = new OutputStreamWriter(connection.getOutputStream());
          //  request.write(parameters);
          //  request.flush();
           // request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            // You can perform UI operations here
            Toast.makeText(this,"Message from Server: \n"+ response, 0).show();
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
        }
    }
*/
//requesting random name from server
    private class getmessage extends AsyncTask<Void,Void,Void> {
        @Override

        protected Void doInBackground(Void... voids){
            String url;
            url = "http://192.168.0.49:81/image_download.php";
            URL urlobj = null;
            String msg = "";
            //String msg = "";

            try {
                urlobj = new URL(url);
                URLConnection lu = urlobj.openConnection();
                //  System.out.println("1");
                BufferedReader rd = new BufferedReader(new InputStreamReader(lu.getInputStream()));
                String line = "";
                int count;
                count = 0;
               while((line = rd.readLine()) != null){
                   message[count] = line;
                   count++;
               }
                rd.close();

                for(int i = 0;i < count;i++){
                    System.out.println(message[i]);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
            //TextView textview = (TextView)findViewById(R.id.textView);
            //textview.setText(message);

            //long code need to change
            String url;
            String url2;
            url = "http://192.168.0.49:81/images/" + message[0];
            url2 = "http://192.168.0.49:81/images/" + message[1];
            ImageView imageview1 = (ImageView) findViewById(R.id.imageView2);
            ImageView imageview2 = (ImageView) findViewById(R.id.imageView3);
            Picasso.get().load(url).into(imageview1);
            Picasso.get().load(url2).into(imageview2);

        }



    }

}
