package com.example.new_application;

import android.annotation.SuppressLint;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**!!!!browse page always have photos number greater than 5**/

public class browsePageAdapter extends Fragment {
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Handler handler;
    private List<String> adapterData;
    private String message;
    private int final_loading;
    private loading_algorithm loading;
    private RecyclerView rv;

    private Done done;

    private SwipeRefreshLayout mSwipeRefreshLayout;


//interface callback from browsepageadapter class to browsepage class
    public void SetProgressDoneListener(Done done){
        System.out.println("browsepageadatper interface called");
        this.done = done;

    }
    public interface Done{
        void Done();

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//get view from swipelayout, !this method can be used to called layout if recycleview is wrapped with extra layout
       View rootView = inflater.inflate(R.layout.content_main_for_refreshlayout, container, false);

//recyclerview setuup
   // rv = (RecyclerView) inflater.inflate(R.layout.content_main, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.CM_recycler_view);
        rv.setVisibility(View.INVISIBLE);
        new getmessage().execute();
        linearLayoutManager = new LinearLayoutManager(rv.getContext());

//swipe refresh layout setuup
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("on fresh called");
//clear adatper and set new adatper in background
                rv.setAdapter(null);

                new setnewadapter().execute();


            }
        });

//!!! Usually, it needs a function to prevent minimun loading failure but since admin would guarenee recent tab would have 5 or move pics, it doesn't matter here
        adapterData = getFirstData();
        setupRecyclerView();


        return rootView;
    }

    private void setupRecyclerView() {
        rv.setLayoutManager(linearLayoutManager);

        recyclerViewAdapter = new RecyclerViewAdapter(rv.getContext(),adapterData, rv,"getSingleImage.php",null);

//set up adapter
        rv.setAdapter(recyclerViewAdapter);

//as the function
        setupCallbackAndInfiniteLoad();

    }
    private void setupCallbackAndInfiniteLoad(){
//recyclerview interface setup needed for transmitting information between classes; this callback is from recyclerview adapter
        recyclerViewAdapter.setEndProgressBarListener(new RecyclerViewAdapter.progresscallback() {
            @Override
            public void progresscall() {
                rv.setVisibility(View.VISIBLE);
//##fixed bug where app crashes when searchresult return
                System.out.println("browsepage interface called ");

                mSwipeRefreshLayout.setRefreshing(false);
                if(done != null) {
                    done.Done();
                }
            }
        });

//on load more listner for only after refreshing only
        recyclerViewAdapter.setOnLoadMoreListener(new RecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapterData.add(null);
                recyclerViewAdapter.notifyItemInserted(adapterData.size() - 1);
                final int loadnumber;
                loadnumber = loading.getIncrement();
                handler = new Handler();
                // loadnumber = 10;
                //loading begins
                if(loadnumber > 0){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapterData.remove(adapterData.size() - 1);
                            recyclerViewAdapter.notifyItemRemoved(adapterData.size());
                            //System.out.println("create");  //loadinnumber below
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


    private List<String> getFirstData(){

        List<String> listObject = new ArrayList<String>();

        listObject.add("1");
        listObject.add("2");
        listObject.add("3");
        listObject.add("4");
        listObject.add("5");


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

    private class setnewadapter extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//update information about the maximum load and set recyclerview to invisible while loading
            adapterData = getFirstData();
            new getmessage().execute();
            rv.setVisibility(View.INVISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {
 //set up new adatper
            recyclerViewAdapter = new RecyclerViewAdapter(rv.getContext(),adapterData, rv,"getSingleImage.php",null);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//install new adatper adn set refreshing icon to false
            rv.setAdapter(recyclerViewAdapter);


          setupCallbackAndInfiniteLoad();

        }
    }

    private class getmessage extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids){
            String url;
            url = "http://192.168.0.49:81/getmaxloading.php";
            URL urlobj = null;
            String msg = "";
            //String msg = "";

            try {

                urlobj = new URL(url);
                URLConnection lu = urlobj.openConnection();
                BufferedReader rd = new BufferedReader(new InputStreamReader(lu.getInputStream()));
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
            //TextView textview = (TextView)findViewById(R.id.textView);
            //textview.setText(message);

            System.out.println(message);
            //long code need to change
            if(message == null) {
                final_loading = 5;
            }else{     final_loading = Integer.parseInt(message);}

            //final_loading = 10;

// minus 4  meaning the picture needs to be load is less than number return by the server because of first data list object

            final_loading = final_loading - 3;
            loading = new loading_algorithm(0,final_loading);
            loading.setcurrentomax();
            // System.out.println(message);
        }



    }



}
