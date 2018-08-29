package com.example.new_application;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


/** trending class would be named random class and it only has maximum picture of 10 randomly selected from database
 *
 * requirement: minimum picture counts of 10 in the database
 *
 *
 * **/
public class trendingClassAdapter extends Fragment {

    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Handler handler;
    private List<String> adapterData;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rv;

    private String ipadres;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//getting ipadress
        ipadres = getArguments().getString("ip");

// method 1(scrapped due to UI inefficiency): local arraylist shoud have 10 data set
        //adapterData = getFirstData();
// method 2: preload data in browsepage and use string array list to get data here
        adapterData = new ArrayList<String>();
        adapterData = getArguments().getStringArrayList("adapterdata");


//finding refreshlayout
        View rootView = inflater.inflate(R.layout.content_main_for_refreshlayout, container, false);
        //RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.content_main, container, false);
//pick recyclerview from the refreshlayout
        rv = (RecyclerView) rootView.findViewById(R.id.CM_recycler_view);

//linear layout manager vertical
        linearLayoutManager = new LinearLayoutManager(rv.getContext());
        System.out.println("trendingcalssadatper oncreateview called ");
        // return the data object

//swipe refresh layout setuup
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("on fresh called");
//clear adatper and set new adatper in background
                rv.setAdapter(null);

                new getrandomimageandsetadapter().execute();


            }
        });



        setupRecyclerView(rv);


        return rootView;
    }


//requesting random name from server
    /** setup viewpager after this is done because this takes longer **/
    private class getrandomimageandsetadapter extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//clear the arraylist and ten initialize it
            adapterData = null;
            adapterData = new ArrayList<String>();

//rv visibility setting
            rv.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids){
            String url;
            url = ipadres + "getRandomImage.php";
            URL urlobj = null;
            String msg = "";
            //String msg = "";

            try {
//open connection
                urlobj = new URL(url);
                URLConnection lu = urlobj.openConnection();

                //  System.out.println("1");
//buffer reader for reading response from the server
                BufferedReader rd = new BufferedReader(new InputStreamReader(lu.getInputStream()));
                String line = "";
                int count;
                count = 0;
                while((line = rd.readLine()) != null){

//use arraylist to load data
                    adapterData.add(line);

                    //message[count] = line;
                    count++;
                }
                rd.close();

                for(int i = 0;i < count;i++){
                    System.out.println("getrandomimage background thread:" + adapterData.get(i));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

//set up new adatper
            recyclerViewAdapter = new RecyclerViewAdapter(rv.getContext(),adapterData, rv,"getSingleImage.php","randomnoneedloading",ipadres);


            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
//install new adatper adn set refreshing icon to false
            rv.setAdapter(recyclerViewAdapter);
//refresh icon off
            mSwipeRefreshLayout.setRefreshing(false);
//setting visibility
            rv.setVisibility(View.VISIBLE);
        }

    }


    private void setupRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(rv.getContext(),adapterData, rv, "getSingleImage.php","randomnoneedloading",ipadres);
        rv.setAdapter(recyclerViewAdapter);

    /*
        recyclerViewAdapter.setOnLoadMoreListener(new RecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapterData.add(null);
                recyclerViewAdapter.notifyItemInserted(adapterData.size() - 1);
                final int loadnumber;
                // loadnumber = loading.getIncrement();
                handler = new Handler();
                // loadnumber = 10;
                //loading begins
                //  if(loadnumber > 0){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterData.remove(adapterData.size() - 1);
                        recyclerViewAdapter.notifyItemRemoved(adapterData.size());
                        //System.out.println("create");  loadinnumber below
                        for (int i = 0; i < 5; i++) {
                            adapterData.add("Item" + (adapterData.size() + 1));
                            recyclerViewAdapter.notifyItemInserted(adapterData.size());
                        }
                        recyclerViewAdapter.setLoaded();
                    }
                }, 2000);
                System.out.println("load");
                //}else{
                //    System.out.println("load failed, caused by maxout exception");
                //  }

            }
        });
*/

    }

/*
    private List<String> getFirstData(){

        List<String> listObject = new ArrayList<String>();

        for(int i = 0;i < 10;i++) {
            listObject.add(Integer.toString(i));
        }

        return listObject;

    }
*/

}
