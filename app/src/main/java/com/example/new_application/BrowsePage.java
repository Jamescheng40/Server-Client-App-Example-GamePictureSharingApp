package com.example.new_application;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.apache.http.protocol.HTTP.USER_AGENT;

/** this is the main page as soon as user open into the app **/

public class BrowsePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    private Handler handler;
    private String message;
    private int final_loading;
    private loading_algorithm loading;
    private BottomNavigationView mBottomNavigationView;
    private ViewPager viewPager;
//search information
    private String msearchdata;
    private int mserach_loading;

//random imagenames
    private ArrayList<String> randomimagename;

    private  Adapter adapter;
    private Adapter adapter1;
    private int RC_RETURN = 1091;
    private TextView header_title_username;
    private TextView header_title_email;
    private int RC_RETURN_YANL = 1314;
    private View mProgressView;
    private View include;

    private SearchPageClassAdapter hi123;

//user information
    private String loginedusername;
    private String logineduseremail;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.R.color.transparent);
        }



        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_browse_page);

        /*
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
               //  Create an Intent that will start the Menu-Activity.
                Intent mainIntent = new Intent(BrowsePage.this,youAreNotLoggedIn.class);
                startActivity(mainIntent);
                finish();
            }
        }, 10);

*/

        System.out.println("system background running check");
//setting up progress bar and the view of the entire appbarlayout
            include = findViewById(R.id.appbar);
            mProgressView = findViewById(R.id.BP_loginpage);

//Viewpage adapter set up for loading view on each tabs
         viewPager = (ViewPager) findViewById(R.id.viewpager);

//tablayout set up
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

//starting preparing for the random tab
        new getrandomimage().execute();


//set up progress bar and callback funtion(listener from another class when loading is finished)
    //progress bar
       showProgress(true);
    //call back function


//set current toolbar to support toobar
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // setActionBar(toolbar);

//nav_header set up
        header_title_email = findViewById(R.id.nav_head_title_email);
        header_title_username = findViewById(R.id.nav_head_title_username);


      //  handler = n
        //
        // ew Handler();
      //  linearLayoutManager = new LinearLayoutManager(BrowsePage.this);
        // return the data object
       // adapterData = getFirstData();


     //   recyclerView = (RecyclerView)findViewById(R.id.recycler_view); // this is only for fiexed recyclerview/ this is replaced by pageview adapter below


      //  recyclerView.setLayoutManager(linearLayoutManager);
       // recyclerViewAdapter = new RecyclerViewAdapter(recyclerView.getContext(), adapterData, recyclerView);
       // recyclerView.setAdapter(recyclerViewAdapter);

/*
        recyclerViewAdapter.setOnLoadMoreListener(new RecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapterData.add(null);
                recyclerViewAdapter.notifyItemInserted(adapterData.size() - 1);
                final int loadnumber;
                loadnumber = loading.getIncrement();
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


/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

//drawer configuration
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

//navigation view set up
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mBottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if(id == R.id.navigation_upload){
                    System.out.println("botom upload here");

                    if(loginedusername != null) {
//using intent here instead of replacing fragments are easier to control and maintain
                        Intent i = new Intent(BrowsePage.this, SearchPage.class);
                        i.putExtra("username", loginedusername);
                        startActivityForResult(i,1 );

//reset browse page tab to its original so that it is visually convincable
                        Handler temphandler;
                        temphandler = new Handler();
                        Runnable r = new Runnable() {
                            public void run() {
                                mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
                            }
                        };
                        temphandler.postDelayed(r, 100);


//


                    }else{
//message to warn user that they are not logged in
                        System.out.println("not logged in");
                        View view = findViewById(android.R.id.content);
                        Snackbar.make(view, "You Are Not Logged In", Snackbar.LENGTH_LONG)
                                .setAction("Close", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                }).setActionTextColor(getResources().getColor(R.color.pure_red)).show();

//setting tabs back to browsepage visually
                        //navigationView.getMenu().performIdentifierAction(R.id.my_download,0);
                      //  Intent i = new Intent(BrowsePage.this,youAreNotLoggedIn.class);
                       // startActivityForResult(i,1);
    // new thread for handling select tab acton
                        Handler temphandler;
                        temphandler = new Handler();
                        Runnable r = new Runnable() {
                            public void run() {
                                mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
                            }
                        };
                        temphandler.postDelayed(r, 100);

                    }
//

                }else if(id == R.id.navigation_home){


                }

                return true;
            }
        });




       // showProgress(true);
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
/*
           include.setVisibility(show ? View.GONE : View.VISIBLE);
            include.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                   include.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
*/
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
          //  include.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



//view pager needs an adapter for its own fragment; it needs a class that contains fragment subclass in it in order to function
    private void setupViewPager(Boolean search, String data, int mfinalloading) {
       if(search == false){
        adapter = new Adapter(getSupportFragmentManager());
        browsePageAdapter hi = new browsePageAdapter();

//implemneting callback from the browsepageadapter fragment
        hi.SetProgressDoneListener(new browsePageAdapter.Done() {
            @Override
            public void Done() {
                System.out.println("job done from browsepage callback function succedd");
                showProgress(false);
            }
        });


        trendingClassAdapter hi1 = new trendingClassAdapter();
        EmptyAdapter emptyAdapter = new EmptyAdapter();
//sending arraylist to trendingclassadatper
           Bundle b = new Bundle();
           b.putStringArrayList("adapterdata", randomimagename);
           hi1.setArguments(b);

       // browsePageAdapter hi2 = new browsePageAdapter();


        adapter.addFragment(hi, "Recent");
        adapter.addFragment(hi1, "Random Feed");
        adapter.addFragment(emptyAdapter,"Search Result");
       // adapter.addFragment(hi2, "Category 3");
        viewPager.setAdapter(adapter);

       }else{
//method 1(scrapped): creating new adatper and removing past adapter
/*
          adapter1 = new Adapter(getSupportFragmentManager());
           browsePageAdapter hi = new browsePageAdapter();



           trendingClassAdapter hi1 = new trendingClassAdapter();
           browsePageAdapter hi2 = new browsePageAdapter();


    //passing string data to the fragment class
           SearchPageClassAdapter hi123 = new SearchPageClassAdapter();
           Bundle bundle = new Bundle();
           bundle.putString("game", data);
           hi123.setArguments(bundle);


           adapter1.addFragment(hi, "Recent");
           System.out.println("definitely wento through here ");

           adapter1.addFragment(hi123, "Search Result");

           // adapter.addFragment(hi2, "Category 3");

    //should implement loading screen here

        //   viewPager.setAdapter(null);


    //caution: thread would freeze  here also if it is not completely finish loading
        viewPager.setAdapter(adapter1);
        viewPager.setCurrentItem(1);
*/
//method 2: changign original adatper and refreshing it
/*
           SearchPageClassAdapter hi123 = new SearchPageClassAdapter();
           Bundle bundle = new Bundle();
           bundle.putString("game", data);
           hi123.setArguments(bundle);

*/

    //creating new class adapter
           hi123 = new SearchPageClassAdapter();
           System.out.println("definitely went through here");

    //method for adding loading screen and it is deprecated due to rules. It is now replaced by Searchpage loading screen instead.
    // Update#1: it is now changed to responding the completion of adatper and change to second tab according

          hi123.OnSetCompletedListener(new SearchPageClassAdapter.oncompleted() {
               @Override
               public void oncomplete() {
                   System.out.println("browsepage hi123 listener called ");

               }
           });
     //passing string data and loading int to the fragment class
           Bundle bundle = new Bundle();
           bundle.putString("game", data);
           bundle.putInt("finalloading",mfinalloading);
           hi123.setArguments(bundle);


           adapter.mFragmentTitles.set(2,"Search Result");
           adapter.mFragments.set(2,hi123);
            System.out.println("see if it is stucked before notifydatachange");
            adapter.notifyDataSetChanged();

//reset viewpager tab to its finished loading page and needs a delay in case of app crashing
           Handler temphandler;
           temphandler = new Handler();
           Runnable r = new Runnable() {
               public void run() {
                   viewPager.setCurrentItem(2);
               }
           };
           temphandler.postDelayed(r, 200);

    //changeing fragment and fragment titles


//hi123 adatper listerner



       }

    }

    static class Adapter extends FragmentStatePagerAdapter {
        public final List<Fragment> mFragments = new ArrayList<>();
        public final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            System.out.println("get items position called");

            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {


                System.out.println("adapter getitem called");
              return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            System.out.println("adapter getpagetitle called");
            return mFragmentTitles.get(position);
        }
        public void clearAll() //Clear all page
        {
//            mFragmentTitles.remove(mFragments.get(0));
 //          mFragmentTitles.remove(mFragments.get(1));
            mFragmentTitles.clear();
            mFragments.clear();
        }
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);





        return true;
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

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.SignUpOrSignIn) {

            Intent newintent = new Intent(BrowsePage.this,signinorsignup.class);
           // System.out.println("signin or signup");
            startActivityForResult(newintent, 1);

            //
            // Handle the camera action
        }
      //  else if (id == R.id.my_download) {


     //   }
        else if (id == R.id.my_upload) {
// new intent to launch uploaded page activity

            if(loginedusername != null) {
                Intent tmpintent = new Intent(BrowsePage.this, MyUploadPage.class);
                tmpintent.putExtra("username", loginedusername);
                startActivity(tmpintent);
            }else{
                View view = findViewById(android.R.id.content);
                Snackbar.make(view, "You Are Not Logged In", Snackbar.LENGTH_LONG)
                        .setAction("Close", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setActionTextColor(getResources().getColor(R.color.pure_red)).show();

            }


        }else if(id == R.id.nav_logout){
//clearing up local data and view for logging out
            loginedusername = null;
            logineduseremail = null;
            header_title_email = findViewById(R.id.nav_head_title_email);
            header_title_username = findViewById(R.id.nav_head_title_username);
            header_title_email.setText(getResources().getString(R.string.nav_header_subtitle));
            header_title_username.setText(getResources().getString(R.string.nav_header_subtitle));
//open drawer for view
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_search){
//search action intent
            System.out.println("hello");
            Intent searchintent = new Intent(BrowsePage.this,SearchPage.class);
//!!! reset item to prevent app crash
            viewPager.setCurrentItem(0);
            startActivityForResult(searchintent,1);

            return true;
        }

        //noinspection SimplifiableIfStatement
      //  if (id == R.id.action_settings) {
            //System.out.println("asdfasfsafa");
        //    return true;
       // }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == 312) {
               // System.out.println("your data is" + data.getStringExtra("game"));

//get finalloading interger and games name string from the searchpage
                msearchdata = data.getStringExtra("game");
                mserach_loading = data.getIntExtra("finalloading",-1);
//change adapter and swtich
                createNewAdapter();

            }else if(resultCode == RC_RETURN){
//return from signing in/ signing up cass
                logineduseremail = data.getStringExtra("memail");
                loginedusername = data.getStringExtra("musername");
                System.out.println("back to browse page data check" + loginedusername);
//set up navigation view
                setupDrawerNavi(loginedusername,logineduseremail);

//open up drawer to show username that they are logged in
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);

            }
            /*  wasted method trying to achieve select tab programmatically
            else if(resultCode == RC_RETURN_YANL){


            }
            */
        }
    }


    private void setupDrawerNavi(String loginedusername, String logineduseremail) {

        header_title_email = findViewById(R.id.nav_head_title_email);
        header_title_username = findViewById(R.id.nav_head_title_username);
        header_title_username.setText(loginedusername);
        header_title_email.setText(logineduseremail);

    }

    private void createNewAdapter() {
//setup viewpager with data and loading number
        setupViewPager(true,msearchdata, mserach_loading);
        //TextView textView = findViewById(R.id.title_header12344);
        //textView.setText(data);


    }


//requesting random name from server
    /** setup viewpager after this is done because this takes longer **/
    private class getrandomimage extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//clear the arraylist and ten initialize it
            randomimagename = null;
            randomimagename = new ArrayList<String>();


        }

        @Override
        protected Void doInBackground(Void... voids){
            String url;
            url = "http://192.168.0.49:81/getRandomImage.php";
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
                        randomimagename.add(line);

                    //message[count] = line;
                    count++;
                }
                rd.close();

                for(int i = 0;i < count;i++){
                    System.out.println("getrandomimage background thread:" + randomimagename.get(i));
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

           /*
            //long code need to change
            String url;
            String url2;
            url = "http://192.168.0.49:81/images/" + message[0];
            url2 = "http://192.168.0.49:81/images/" + message[1];
            ImageView imageview1 = (ImageView) findViewById(R.id.imageView2);
            ImageView imageview2 = (ImageView) findViewById(R.id.imageView3);
            Picasso.get().load(url).into(imageview1);
            Picasso.get().load(url2).into(imageview2);
            */

//initializing adapter in the setupviewpager
            if (viewPager != null) {
                setupViewPager(false, null,-1);
            }


        }

    }

}
