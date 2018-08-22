package com.example.new_application;

import android.os.AsyncTask;

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

public class SPgetsearchresult extends AsyncTask<Void,Void,List<String>> {

private String mwrod;
private List<String> listobj;

    public Asynresponse delegate = null;

public SPgetsearchresult(String wrod, Asynresponse delegate) {
        mwrod = wrod;
    this.delegate = delegate;
        //System.out.println(mnumber);
        listobj = new ArrayList<String>();


        }


    public interface Asynresponse{
    void processFinish(List<String> output);

}
@Override
protected List<String> doInBackground(Void... voids) {
        //System.out.println("mark 5");
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
        while ((inputLine = in.readLine()) != null) {
        listobj.add(inputLine);
        }
        in.close();

        // System.out.println(response.toString());
        con.disconnect();


        } catch (IOException e) {
        e.printStackTrace();
        }

        return listobj;
        }

    protected void onPostExecute(List<String> result) {

        delegate.processFinish(listobj);
    }



}
