package com.alitech.beoneseye;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class ExtractedText extends AppCompatActivity {
    TextView ExtractedtextView;
    TextToSpeech textToSpeech;
    Button listenButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extracted_text);
        ExtractedtextView=(TextView)findViewById(R.id.extractedTextView);
        listenButton=(Button)findViewById(R.id.listenAudio);
        Intent extractIntent=this.getIntent();
        String textExtracted=extractIntent.getStringExtra("Text Extracted");
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
}