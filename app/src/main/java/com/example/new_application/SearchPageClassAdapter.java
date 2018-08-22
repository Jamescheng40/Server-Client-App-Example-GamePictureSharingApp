package com.example.new_application;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/** this class does not have restriction on its photo numbers **/

public class SearchPageClassAdapter extends Fragment{

    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Handler handler;
    private List<String> adapterData;
    private String mgame;
    private String message;
    private int final_loading;
    private loading_algorithm loading;
    private int ininumber;
    private Object lock = new Object();

    private oncompleted oncomplete;

    public void OnSetCompletedListener(oncompleted complete){
        System.out.println("searchpageclassadapter listerner called");
        this.oncomplete = complete;
    }
    public interface oncompleted{
        void oncomplete();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //this.mgame = text;



    // new getmaxloading().execute();
//System.out.println("now is the time to fuck");

//trying to use interface to create view, which is better than loop method
        //method 1(scrapped): using loop with sleep delay; weekness: not very energy efficient
        // while(final_loading == 0){
        //      try {
        //         Thread.sleep(400);
        //      } catch (InterruptedException e) {
        //         e.printStackTrace();
        ///    }
        //    System.out.println("check it now");

        //  }
        //method 2(scrapped): using callback method within class; weekness: onCreateView can only be initilized once and it demands rv immediately
        //method 3: using thread synchronization eg, wait() and notifyAll() to handle the process(needs more improvement eg add a splash screen); splash screen can be added due to Activity class rules
      /*
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        */
        //method 4: preload information in the holder and send here instead

//get the data(final_loading) passed from the activity class and ready for server processing

        final_loading = getArguments().getInt("finalloading");
        mgame = getArguments().getString("game");

//to do this in case of app crashing, separate situations for picture numbers exceed 5 or below 5 and handle them in accordance. Here steps include set up loading algorithm and settup loading number
        if(final_loading >= 5) {
            final_loading = final_loading - 3;
            loading = new loading_algorithm(0, final_loading);
            System.out.println("this is flag one to chec" + final_loading);
            loading.setcurrentomax();
        }else{
            System.out.println("this is flag two to chec" + message);
            loading = new loading_algorithm(0, 0);
            loading.setcurrentomax();

        }




        RecyclerView rv = (RecyclerView) inflater.inflate( R.layout.content_main, container, false);



      //  System.out.println("the game from searchpage adapter is" + mgame);
       //while System.out.println("so you fuckign disapear huh?");


//setting up linear layout manager
        linearLayoutManager = new LinearLayoutManager(rv.getContext());
        // return the data object




//creating listobj for adapter to process(itemcount specifically) otherwise adapter would crash
        if(final_loading >= 5) {
            adapterData = getFirstData(5);
        }else if(final_loading < 5 && final_loading > 0){
            adapterData = getFirstData(final_loading);
        }else{
            adapterData = getFirstData(0);
        }


        setupRecyclerView(rv);
 //oncomplete setting up listenrer
        System.out.println("searchpageclassadapter test");
        oncomplete.oncomplete();

        return rv;
    }

    private void setupRecyclerView(RecyclerView rv) {
// setting view adapter the recycler view
        rv.setLayoutManager(linearLayoutManager);

        recyclerViewAdapter = new RecyclerViewAdapter(rv.getContext(),adapterData, rv, "getgamepics.php", mgame);
        rv.setAdapter(recyclerViewAdapter);



        recyclerViewAdapter.setOnLoadMoreListener(new RecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapterData.add(null);
                recyclerViewAdapter.notifyItemInserted(adapterData.size() - 1);
                final int loadnumber;
                 loadnumber = loading.getIncrement();
             //System.out.println("your load number for searchpage is " + loadnumber);

// do in background to notify linearmanager that the size of the adapterdata has been changed
                handler = new Handler();
                // loadnumber = 10;
//loading begins
                  if(loadnumber > 0){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterData.remove(adapterData.size() - 1);
                        recyclerViewAdapter.notifyItemRemoved(adapterData.size());
                        //System.out.println("create");  loadinnumber below
                        for (int i = 0; i < loadnumber; i++) {
                            adapterData.add("Item" + (adapterData.size() + 1));
                            recyclerViewAdapter.notifyItemInserted(adapterData.size());
                        }
                        recyclerViewAdapter.setLoaded();
                    }
                }, 2000);
                System.out.println("load");
                }else{

                    System.out.println("load failed, caused by maxout exception");
                 }

            }
        });

    }

    private List<String> getFirstData(int loopnum) {

        List<String> listObject = new ArrayList<String>();


        for(int i = 0;i <= loopnum;i++){
            listObject.add(Integer.toString(i));
        }

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
    /*
    private class getmaxloading extends AsyncTask<Void,Void,Void> {
        private String message;
        @Override
        protected Void doInBackground(Void... voids){
            //System.out.println("mark 4");
            String url;
            String url1;

            url = "http://192.168.0.49:81/getmaxloadingsearch.php";
            URL urlobj = null;
            String msg = "";
            //String msg = "";

            try {
                String a, b,c,e,f ;

                e = URLEncoder.encode("games", "UTF-8");

                    f = URLEncoder.encode(mgame, "UTF-8");



                c = "&" + e + "=" + f;
                byte[] postDataBytes = c.getBytes();



                urlobj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) urlobj.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                OutputStream out = new BufferedOutputStream(con.getOutputStream());
                out.write(postDataBytes);
                out.close();







                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                line = rd.readLine();
                message = line;
                rd.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


            if(message == null) {
                final_loading = 5;
            }else{     final_loading = Integer.parseInt(message);}

            //final_loading = 10;

            if(final_loading >= 5) {
                final_loading = final_loading - 3;
                loading = new loading_algorithm(0, final_loading);
                System.out.println("this is flag one to chec" + final_loading);
                loading.setcurrentomax();
            }else{
                System.out.println("this is flag two to chec" + message);
                loading = new loading_algorithm(0, 0);
                loading.setcurrentomax();

            }

//Thread freeze cancel
            synchronized (lock){
                lock.notifyAll();

            }


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
        }



    }
*/

}
