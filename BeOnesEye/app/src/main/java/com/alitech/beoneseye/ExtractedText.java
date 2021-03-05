package com.alitech.beoneseye;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExtractedText extends AppCompatActivity {
    TextView ExtractedtextView;
    TextToSpeech textToSpeech;
    Button listenButton;
    Button giveFeedback;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String textExtracted;
    String filePath;
    String action;
    TextView feebackText;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extracted_text);
        mAuth=FirebaseAuth.getInstance();
        dbHelper=new DBHelper(this);
        ExtractedtextView=(TextView)findViewById(R.id.extractedTextView);
        listenButton=(Button)findViewById(R.id.listenAudio);
        feebackText=(TextView)findViewById(R.id.feedbackText);
        giveFeedback=(Button)findViewById(R.id.feedBackButton);
        Intent extractIntent=this.getIntent();
        textExtracted=extractIntent.getStringExtra("Text Extracted");
        filePath=extractIntent.getStringExtra("filepath");
        action=extractIntent.getStringExtra("action");
        ExtractedtextView.setText(textExtracted);
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(textExtracted, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        giveFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser!=null){
                    dbHelper.insertHistory(currentUser.getUid(),action,textExtracted,getCurrentTimeStamp(),filePath,feebackText.getText().toString());
                    Log.i("QUerry Done","yes Done");
                    feebackText.setText("");
                    feebackText.setEnabled(false);
                    giveFeedback.setEnabled(false);
                }else{
                    new CustomToast().Show_Toast(ExtractedText.this, v,
                            "Please SIGN UP/LOGIN First.");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser=mAuth.getCurrentUser();
    }

    @Override
    protected void onPause() {
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onBackPressed();

        startActivity(new Intent(ExtractedText.this,camerGallery.class));
        finish();
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }
}