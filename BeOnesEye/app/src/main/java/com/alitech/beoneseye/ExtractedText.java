package com.alitech.beoneseye;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ExtractedText extends AppCompatActivity {
    TextView ExtractedtextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extracted_text);
        ExtractedtextView=(TextView)findViewById(R.id.extractedTextView);
        Intent extractIntent=this.getIntent();
        String textExtracted=extractIntent.getStringExtra("Text Extracted");
        ExtractedtextView.setText(textExtracted);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ExtractedText.this,camerGallery.class));
        finish();
    }
}