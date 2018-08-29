package com.example.new_application;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class MyUploadPageAdapter extends RecyclerView.Adapter<MyUploadPageViewHolder> {
    private List<String> itemList;
    protected Context mcontext;
    private RecyclerView recyclerView;
    private String musername;

//ipadres info
    private String ipadres;
    public MyUploadPageAdapter(Context context, List<String> itemList, String username, String ipadres){
       mcontext = context;
        this.itemList = itemList;
        this.musername = username;
         this.ipadres = ipadres;

        System.out.println("from myuploadpageadapter " + musername);


    }

    public int getItemViewType(int position) {
        int i;

        i = itemList.get(position) != null ? 1 : 0;
        return i;
    }

    @Override
    public MyUploadPageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MyUploadPageViewHolder viewHolder = null;
        if(i == 1){
            //System.out.println("create");
            //  Random rand = new Random();
            // int n = rand.nextInt(2) + 1;
            //   if(n == 1){

            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.amup_itemlist, viewGroup, false);
            viewHolder = new MyUploadPageViewHolder(layoutView,mcontext,musername,ipadres);
/*
            }else{
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.random_text, parent, false);
                viewHolder = new RecyclerViewHolders(layoutView);


            }
*/
        }else{
            // System.out.println("create1");
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emptylayout, viewGroup, false);
            viewHolder = new MyUploadPageViewHolder(layoutView,mcontext,musername,ipadres);
            // viewHolder = null;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyUploadPageViewHolder holder, int position) {
        if(holder instanceof MyUploadPageViewHolder) {
            new getimage(position, holder,musername).execute();


        }

    }



    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    public class getimage extends AsyncTask<Void,Void,Void> {

        private int mnumber;
        private String musername;
        private MyUploadPageViewHolder mholder;
        private String[] response = new String[10];
        private String murl;
        public getimage(int number, MyUploadPageViewHolder holder, String username){
            this.mnumber = number;
            this.mholder = holder;
            this.musername = username;
            this.murl = "getOneImageFromUsername.php";

        }
        @Override
        protected Void doInBackground(Void... voids){
            System.out.println("mark 5");
            String url;
            url = ipadres + this.murl;
            URL urlobj = null;

            // url = "https://selfsolve.apple.com/wcResults.do";
            // HttpURLConnection con = new HttpURLConnection(urlobj);
/*
            Map<String,Object> params = new LinkedHashMap<>();
            params.put("id","1");
            params.put("asdf","asdf");
            StringBuilder postData = new StringBuilder();

*/

            try {
                String a, b,c,e,f ;
                a = URLEncoder.encode("position", "UTF-8");
                b = URLEncoder.encode(Integer.toString(this.mnumber), "UTF-8");
                e = URLEncoder.encode("username", "UTF-8");
                f = URLEncoder.encode(this.musername, "UTF-8");
                c = "&" + a + "=" + b + "&" + e + "=" + f;
                byte[] postDataBytes = c.getBytes();

                urlobj = new URL(url);


 // internet connection
                HttpURLConnection con = (HttpURLConnection) urlobj.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                OutputStream out = new BufferedOutputStream(con.getOutputStream());
                out.write(postDataBytes);
                out.close();
                // System.out.println("chck");
                int responseCode = con.getResponseCode();
                // System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;

                int i;
                i = 0;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("from inputline within myuploadpageadtaper" + inputLine);
                    response[i] = inputLine;
                    i++;
                }
                in.close();

                con.disconnect();


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
            //((RecyclerViewHolders)mholder).textTitle.setText();
            String url;
            String url1;
            // String url2;

            // ## bug fixed: crash app when resposne[1] is refercing to a null object
            if(response[0].equals("ResultNotFound")){
                System.out.println("from recyclerviewadapter doinbackground function and msg from server" + response[0]);

            }else{
                url = ipadres+  "images/" + response[0];
                Picasso.get().load(url).into((mholder).displayedImage);
                 mholder.picname.setText(response[0].toString());



          //      url1 = "http://192.168.0.49:81/game_images/" + response[1] + ".jpg";
            //    Picasso.get().load(url1).into(((RecyclerViewHolders) mholder).gameImage);
            //    ((RecyclerViewHolders) mholder).username.setText("Contributed By: " + response[2].toString());
            //    ((RecyclerViewHolders) mholder).picname.setText(response[0].toString());

            }



        }



    }


}
