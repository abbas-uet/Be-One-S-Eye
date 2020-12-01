package com.alitech.beoneseye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(currentUser==null){
                    Intent mainIntent = new Intent(MainActivity.this,LoginSignUpPage.class);
                    startActivity(mainIntent);
                    finish();
                }else{
                    startActivity(new Intent(MainActivity.this,camerGallery.class));
                    finish();
                }
            }
        }, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser= mAuth.getCurrentUser();
    }
}