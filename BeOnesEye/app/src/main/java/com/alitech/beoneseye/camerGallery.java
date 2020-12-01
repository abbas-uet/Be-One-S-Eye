package com.alitech.beoneseye;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

public class camerGallery extends AppCompatActivity {

    private TextView myAccount,logInSignUp;
    Button cameraIntent,galleryIntent;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camer_gallery);
        myAccount=(TextView)findViewById(R.id.myAccount);
        logInSignUp=(TextView)findViewById(R.id.loginSIgnUp);
        imageButton=(ImageButton)findViewById(R.id.myAccountImage);
        cameraIntent=(Button) findViewById(R.id.camera);
        galleryIntent=(Button)findViewById(R.id.gallery);

        logInSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(camerGallery.this,LoginSignUpPage.class));
                finish();
            }
        });
        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(camerGallery.this,myAccountActvity.class));
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(camerGallery.this,myAccountActvity.class));
            }
        });
        cameraIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ImagePicker.Companion.with(camerGallery.this).crop()
                    .cameraOnly()
                    .compress(400).
                    maxResultSize(1000,1000)
                    .start();
            }
        });
        galleryIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(camerGallery.this).crop()
                        .galleryOnly()
                        .compress(400).
                        maxResultSize(1000,1000)
                        .start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            String fileUri = data.toURI();

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "Error Occured!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}