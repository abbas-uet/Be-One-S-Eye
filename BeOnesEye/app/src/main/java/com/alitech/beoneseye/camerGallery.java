package com.alitech.beoneseye;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.List;

public class camerGallery extends AppCompatActivity {

    private TextView myAccount,logInSignUp;
    Button cameraIntent,galleryIntent,getTextButton,getlandMarkText,getObjectText;
    ImageView imageToTextImage;
    ImageButton imageButton;
    TextRecognizer recognizer;
    ImageLabeler labeler;
    TextView aText;
    Uri fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camer_gallery);
        imageToTextImage=(ImageView)findViewById(R.id.imagetoText);
        getTextButton=(Button)findViewById(R.id.getTextButton);
        getlandMarkText=(Button)findViewById(R.id.getLandMarkText);
        getObjectText=(Button)findViewById(R.id.getObjectText);
        myAccount=(TextView)findViewById(R.id.myAccount);
        logInSignUp=(TextView)findViewById(R.id.loginSIgnUp);
        aText=(TextView)findViewById(R.id.textView);
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
            ImagePicker.Companion.with(camerGallery.this)
                    .cameraOnly()
                    .maxResultSize(1000,1000)
                    .start();
            }
        });
        galleryIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(camerGallery.this)
                        .crop()
                        .compress(400)
                        .galleryOnly()
                        .maxResultSize(1000,1000)
                        .start();
            }
        });
        getTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recognizer= TextRecognition.getClient();
                InputImage image = null;
                try {
                    image=InputImage.fromFilePath(getApplicationContext(), fileUri);
                } catch (IOException e) {
                    Toast.makeText(camerGallery.this, "Unable to Get the Image", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                Task<Text> result =
                        recognizer.process(image)
                                .addOnSuccessListener(new OnSuccessListener<Text>() {
                                    @Override
                                    public void onSuccess(Text visionText) {
                                        Toast.makeText(camerGallery.this, "Text Extracted Successfully!", Toast.LENGTH_SHORT).show();
                                        String textExtracted=visionText.getText();
                                        startActivity(new Intent(camerGallery.this,ExtractedText.class).
                                                putExtra("Text Extracted",textExtracted));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(camerGallery.this, "Error While Extracting "+e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

            }
        });
        getlandMarkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        getObjectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLabeler labeler= ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
                InputImage image=null;
                try {
                    image = InputImage.fromFilePath(getApplicationContext(), fileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                labeler.process(image)
                        .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                            @Override
                            public void onSuccess(List<ImageLabel> labels) {
                                for (ImageLabel label : labels) {
                                    String text = label.getText();
                                    float confidence = label.getConfidence();
                                    int index = label.getIndex();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(camerGallery.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }});
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri=Uri.parse(data.toURI());
            setInvisibility();
            setVisibility(fileUri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "Error Occured!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void setVisibility(Uri imageUri) {
        imageToTextImage.setVisibility(View.VISIBLE);
        imageToTextImage.setImageURI(imageUri);
        getTextButton.setVisibility(View.VISIBLE);
        getlandMarkText.setVisibility(View.VISIBLE);
        getObjectText.setVisibility(View.VISIBLE);
    }

    private void setInvisibility() {
        galleryIntent.setVisibility(View.GONE);
        cameraIntent.setVisibility(View.GONE);
        myAccount.setVisibility(View.GONE);
        imageButton.setVisibility(View.GONE);
        logInSignUp.setVisibility(View.GONE);
        aText.setVisibility(View.GONE);
    }
}