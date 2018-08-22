package com.example.new_application;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List<String> itemList;
    protected Context context;
    private int visibleThreshold = 4;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private progresscallback pcb;
    private int final_loading = 100;
    private int temp = 1;
    private String murl;
    public String extradata;

//is it random adapter calling?
    public Boolean isRandom = false;

    public RecyclerViewAdapter(Context context, List<String> itemList, RecyclerView recyclerView, String uri, String data) {
        //new getmaxloading().execute();  can not be placed here due to overlap issue?
       // this.itemList = itemList;
        this.itemList = itemList;
        this.context = context;
        this.murl = uri;
        this.extradata = data;


       // System.out.println("extradata from constructor" + itemList.get(0));


//noneed means there is no need for loading listener
        if((extradata != null) && extradata.equals("randomnoneedloading")) {

            isRandom = true;
            this.murl = "getimageinfofromname.php";

        }else{
        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                   if(temp == 1) {
                       new getmaxloading().execute();
                        temp = 0;
                   }
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if(!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
                        if(onLoadMoreListener != null){
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
        }
    }

    // items would go through getitemviewtype then to oncreateviewhodler then to onbindholder in this sequence
    @Override
    public int getItemViewType(int position) {
       int i;

        i = itemList.get(position) != null ? 1 : 0;
        //i = 0;
        //if(position == 0){
         //  i = 0;
      // }
        return i;
    }
    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolders viewHolder = null;
        if(viewType == 1){
            //System.out.println("create");
            //  Random rand = new Random();
            // int n = rand.nextInt(2) + 1;
            //   if(n == 1){

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            viewHolder = new RecyclerViewHolders(layoutView);
/*
            }else{
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.random_text, parent, false);
                viewHolder = new RecyclerViewHolders(layoutView);


            }
*/
        }else{
            // System.out.println("create1");
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.emptylayout, parent, false);
            viewHolder = new RecyclerViewHolders(layoutView);
           // viewHolder = null;
        }
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
         // System.out.println("mark 3");
       // TextView textView =(TextView)((Activity)context).findViewById(R.id.title_header12344);
          //  System.out.println("extra data from on bind view:"+ extradata);
        if(holder instanceof RecyclerViewHolders){

                final String pos;
                final int positionfinal = position;
                pos = Integer.toString(positionfinal);

//load image from the internet and it has to be less than final_loading
                    if (position <= final_loading && isRandom == false) {
                        new getimage(pos, holder).execute();
                    }else if(position <= final_loading && isRandom == true){
                        new getimage(itemList.get(position                                                                                                                                                                            ), holder).execute();

                    }

                // ((RecyclerViewHolders)holder).textTitle.setText(itemList.get(position));

        }else{
          //  ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }

    }
    public void setLoad(){
        loading = false;
    }
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


// method for setting up callback function
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

 //onloadmore interface which is called back in the browsepage
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

//method for ending the progress bar indicator
    public void setEndProgressBarListener(progresscallback v){
        System.out.println("recyclerviewadapter interface called");
        this.pcb = v;

    }
    public interface progresscallback{

        void progresscall();

    }




    public void setLoaded() {
        loading = false;
    }


    public class getimage extends AsyncTask<Void,Void,Void> {

        private String mnumber;
        private RecyclerViewHolders mholder;
        private String[] response = new String[10];
        private boolean startload = true;

        public getimage(String number, RecyclerViewHolders holder){
            mnumber = number;
            mholder = holder;
/*
            if(extradata != null){

                mnumber = extradata;

            }
        */
        }
        @Override
        protected Void doInBackground(Void... voids){
            System.out.println("mark 5");
            String url;
            url = "http://192.168.0.49:81/" + murl;
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
//if this succeed then we will proceed to load image process
                startload = true;

                String a, b,c,e,f ;
                a = URLEncoder.encode("id", "UTF-8");
                b = URLEncoder.encode(mnumber, "UTF-8");
                e = URLEncoder.encode("games", "UTF-8");
               // System.out.println("the extra data is: "+ extradata);
//extradata nonnull means the requested adatper is searchclass adapter
                if(extradata == null){
                    f = URLEncoder.encode("", "UTF-8");
                }else{
                    f = URLEncoder.encode(extradata, "UTF-8");
                }
//                f = URLEncoder.encode(extradata, "UTF-8");
                c = "&" + a + "=" + b + "&" + e + "=" + f;
                byte[] postDataBytes = c.getBytes();
                //  c = "id=123";
                // String urlParameters = "id=3422";
                // byte[] postDataBytes = urlParameters.toString().getBytes();
//internet connection
                urlobj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) urlobj.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                OutputStream out = new BufferedOutputStream(con.getOutputStream());
                out.write(postDataBytes);
                out.close();
               // System.out.println("chck");
                int responseCode = con.getResponseCode();
               // System.out.println("Response Code : " + responseCode);
//response reader
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;

                int i;
                i = 0;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("from inputline within recyclerviewadapter" + inputLine);
                    response[i] = inputLine;
                    i++;
                }
                in.close();

               // System.out.println(response.toString());
                con.disconnect();





                /*

                for (Map.Entry<String,Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }

                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
*/
                //   StringBuilder tokenUri=new StringBuilder("id=");
                // tokenUri.append(URLEncoder.encode("5","UTF-8"));


                //tokenUri.append("&param2=");
                //  tokenUri.append(URLEncoder.encode("param2","UTF-8"));
                //  tokenUri.append("&param3=");
                // tokenUri.append(URLEncoder.encode("param3","UTF-8"));

                // con.setReadTimeout(10000);
                // con.setConnectTimeout(15000);
                //   con.setRequestMethod("POST");
                // con.setDoInput(true);

                // con.setRequestProperty( "Content-type", "application/x-www-form-urlencoded");
                //  con.setRequestProperty( "Accept", "*/*" );
                //   List<NameValuePair> params = new ArrayList<NameValuePair>();
                //  params.add(new BasicNameValuePair("id", "123"));
                //  params.add(new BasicNameValuePair("secondParam", "234"));
                //   params.add(new BasicNameValuePair("thirdParam", "345"));


                // con.setChunkedStreamingMode(0);





                //   String a, b,c ;
                //   a = URLEncoder.encode("id", "UTF-8");
                //  b = URLEncoder.encode("123", "UTF-8");
                //  c = "&" + a + "=" + b;
                //  c = "id=123";

                // BufferedWriter writer = new BufferedWriter(
                //        new OutputStreamWriter(out, "UTF-8"));
                //  writer.write(c);
                //  writer.flush();
                //  writer.close();
                //  byte[] abc = c.toString().getBytes();

                // con.connect();



                ////  System.out.println("\nSending 'POST' request to URL : " + url);
                // System.out.println("Post parameters : " + urlParameters);

/*
                BufferedReader rd = new BufferedReader(new InputStreamReader(lu.getInputStream()));
                String line = "";
                line = rd.readLine();
                message = line;
                rd.close();
*/
            } catch (IOException e) {
 // disable the loading feature
                startload = false;
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            //TextView textview = (TextView)findViewById(R.id.textView);
            //textview.setText(message);

            //long code need to change
            //((RecyclerViewHolders)mholder).textTitle.setText();
            String url;
            String url1;
            // String url2;


//## trying to fix bug without in without internet connection
            if (startload == true) {

    // ## bug fixed: crash app when resposne[1] is refercing to a null object
                if (response[0].equals("ResultNotFound")) {
                    System.out.println("from recyclerviewadapter doinbackground function and msg from server" + response[0]);

                } else {
                    url = "http://192.168.0.49:81/images/" + response[0];
                    Picasso.get().load(url).into(((RecyclerViewHolders) mholder).displayedImage);


                    ((RecyclerViewHolders) mholder).textTitle.setText(response[1].toString());
                    url1 = "http://192.168.0.49:81/game_images/" + response[1] + ".jpg";
                    Picasso.get().load(url1).into(((RecyclerViewHolders) mholder).gameImage);
                    ((RecyclerViewHolders) mholder).username.setText("Contributed By: " + response[2].toString());
                    ((RecyclerViewHolders) mholder).picname.setText(response[0].toString());
//sendign call back function pointer back to browsepage so that progress bar can disappear

                    System.out.println("successfully loaded from recyclerviewadapter for callback");
                    if(pcb != null) {
                        pcb.progresscall();
                    }
                }


            }else{
                System.out.println("recyclerviewadapter ops internet connection lost");

            }
        }


    }

    private class getmaxloading extends AsyncTask<Void,Void,Void> {
        private String message;
        @Override
        protected Void doInBackground(Void... voids){
            //System.out.println("mark 4");
            String url;
            String url1;
            url1 = "getmaxloading.php";
            if(extradata != null){
                url1 = "getmaxloadingsearch.php";
            }
            url = "http://192.168.0.49:81/" + url1;
            URL urlobj = null;
            String msg = "";
            //String msg = "";

            try {
                String a, b,c,e,f ;

                e = URLEncoder.encode("games", "UTF-8");
                if(extradata == null) {
                    f = URLEncoder.encode("", "UTF-8");
                }else{
                    f = URLEncoder.encode(extradata, "UTF-8");
                }
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

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
            //TextView textview = (TextView)findViewById(R.id.textView);
            //textview.setText(message);

            System.out.println("The message from recycler view is " + message);
            //long code need to change
            if(message == null) {
                final_loading = 5;
            }else if(message.equals("ResultNotFound")){
                final_loading = 0;
            }
            else {     final_loading = Integer.parseInt(message);}


        }



    }

    private void removeItem(RecyclerViewHolders holder, int position) {
        itemList.remove(holder.getAdapterPosition());
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
    }

}