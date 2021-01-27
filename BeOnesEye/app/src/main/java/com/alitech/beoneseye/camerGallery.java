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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class camerGallery extends AppCompatActivity {

    private TextView myAccount,logInSignUp;
    Button cameraIntent,galleryIntent,getTextButton,getlandMarkText,getObjectText;
    ImageView imageToTextImage;
    ImageButton imageButton;
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
                TextRecognizer recognizer= TextRecognition.getClient();
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
                                                putExtra("Text Extracted",textExtracted).putExtra("action","Text Extraction").putExtra("filepath",fileUri.getPath()));
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
                InputImage image = null;
                try {
                    image = InputImage.fromFilePath(getApplicationContext(), fileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BarcodeScannerOptions options =
                        new BarcodeScannerOptions.Builder()
                                .setBarcodeFormats(
                                        Barcode.FORMAT_QR_CODE,
                                        Barcode.FORMAT_AZTEC)
                                .build();
                BarcodeScanner scanner = BarcodeScanning.getClient();
                Task<List<Barcode>> result = scanner.process(image)
                        .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                            @Override
                            public void onSuccess(List<Barcode> barcodes) {
                                Toast.makeText(camerGallery.this, "Text Extracted Successfully!", Toast.LENGTH_SHORT).show();
                                String textExtracted="";
                                for (Barcode barcode: barcodes) {
                                    Rect bounds = barcode.getBoundingBox();
                                    Point[] corners = barcode.getCornerPoints();

                                    String rawValue = barcode.getRawValue();

                                    int valueType = barcode.getValueType();

                                    // See API reference for complete list of supported types
                                    switch (valueType) {
                                        case Barcode.TYPE_WIFI:
                                            String ssid = barcode.getWifi().getSsid();
                                            String password = barcode.getWifi().getPassword();
                                            int type = barcode.getWifi().getEncryptionType();
                                            textExtracted=textExtracted+" A wifi with Id:"+ssid+" and password:"+password;
                                            break;
                                        case Barcode.TYPE_URL:
                                            String title = barcode.getUrl().getTitle();
                                            String url = barcode.getUrl().getUrl();
                                            textExtracted=textExtracted+" A Url with Title :"+title+" and Url:"+url;
                                            break;
                                        case Barcode.TYPE_SMS:
                                            textExtracted=textExtracted+" A SMS with Phone Number :"+barcode.getSms().getMessage()+" and Message:"+barcode.getSms().getMessage();
                                            break;
                                        case Barcode.TYPE_TEXT:
                                            textExtracted=textExtracted+" A Text with value :"+barcode.getRawValue();
                                            break;
                                        case Barcode.TYPE_CONTACT_INFO:
                                            textExtracted=textExtracted+" A Contact Info with :"+barcode.getContactInfo().toString();
                                            break;
                                        case Barcode.TYPE_DRIVER_LICENSE:
                                            textExtracted=textExtracted+" A wifi with Id :"+barcode.getDriverLicense().toString();
                                            break;
                                        case Barcode.TYPE_GEO:
                                            textExtracted=textExtracted+" A Geo Location with :"+barcode.getGeoPoint().toString();
                                            break;
                                        case Barcode.TYPE_PRODUCT:
                                            textExtracted=textExtracted+" A Product with credentials :"+barcode.getPhone().toString();
                                            break;
                                        case Barcode.TYPE_CALENDAR_EVENT:
                                            textExtracted=textExtracted+" A Calender Event Info with :"+barcode.getCalendarEvent().toString();
                                            break;
                                        case Barcode.TYPE_ISBN:
                                            textExtracted=textExtracted+" A wifi with Id :"+barcode.getDisplayValue();
                                            break;
                                    }
                                }

                                startActivity(new Intent(camerGallery.this,ExtractedText.class).
                                        putExtra("Text Extracted",textExtracted).
                                        putExtra("action","Bar Code Scanning").
                                        putExtra("filepath",fileUri.getPath()));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(camerGallery.this, "Error While Scanning "+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

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
                                String textExtracted="";
                                for (ImageLabel label : labels) {
                                    String text = label.getText();
                                    float confidence = label.getConfidence();
                                    int index = label.getIndex();
                                    textExtracted=textExtracted+" "+text;
                                }
                                startActivity(new Intent(camerGallery.this,ExtractedText.class).
                                        putExtra("Text Extracted",textExtracted).
                                        putExtra("action","Object Detection").
                                        putExtra("filepath",fileUri.getPath()));
                                finish();
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
    @Override
    protected void onStart() {
        super.onStart();
    }
}