package com.example.new_application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public class signinorsignup extends AppCompatActivity {
    private Button signup;
    private Button signin;
    private int RC_RETURN = 1091;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinorsignup);
        signup = (Button) findViewById(R.id.signupbutton);
        signin = (Button) findViewById(R.id.Signinbutton);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupintent = new Intent(signinorsignup.this, LoginActivity.class);
                startActivityForResult(signupintent,1);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupintent = new Intent(signinorsignup.this,SignUpClass.class);
                startActivityForResult(signupintent,1);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RC_RETURN){
                Intent tempintent = new Intent();
                tempintent.putExtra("musername", data.getStringExtra("musername"));
                tempintent.putExtra("memail", data.getStringExtra("memail"));
                System.out.println("signuporsignout check " + data.getStringExtra("musername"));
                setResult(RC_RETURN,tempintent);
                finish();

            }

        }

    }

}
