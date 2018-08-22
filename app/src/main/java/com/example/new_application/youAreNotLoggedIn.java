package com.example.new_application;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class youAreNotLoggedIn extends AppCompatActivity {
    private ActionBar actionbar;
    private int RC_RETURN_YANL = 1314;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_are_not_logged_in);
        //action bar setting
        SetUpActionBar(actionbar);

    }
    private void SetUpActionBar(ActionBar actionbar) {
        actionbar = getSupportActionBar();

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);

    }
    //action bar
    public boolean onOptionsItemSelected(MenuItem item){

        Intent i = new Intent();
        setResult(RC_RETURN_YANL,i);
        finish();



        return true;

    }

}
