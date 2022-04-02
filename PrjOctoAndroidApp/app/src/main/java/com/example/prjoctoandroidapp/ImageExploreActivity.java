package com.example.prjoctoandroidapp;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ImageExploreActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgVwTest;
    InputImage imgToBeTested;
    ImageLabeler labeler;
    EditText edLabel;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_explore);
        initialize();

    }

    private void initialize() {
        imgVwTest = findViewById(R.id.imgVwTest);
        imgVwTest.setOnClickListener(this);
        labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        edLabel = findViewById(R.id.edLabel);
        tts = new TextToSpeech(this.getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(i == TextToSpeech.SUCCESS){
                            int res = tts.setLanguage(Locale.US);
                            switch (res){
                                case TextToSpeech.LANG_MISSING_DATA:
                                case TextToSpeech.LANG_NOT_SUPPORTED:
                                    Log.e("TTS","Language not supported");
                                    break;
                            }
                        } else{
                            Log.e("TTS","Initialization Failed");
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        prepareInputImage();
        processImage();
    }

    //  To process the image and finding all the objects inside an image

    private void processImage() {
        labeler.process(imgToBeTested)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {

                        // The objects will be labelled to their most abstract form
                        // index 0 is the most accurate one and then as you go 1,2,3...
                        // you can detect other objects.
                        // NOTE: The labeler will label 0 to the object which covers the most
                        // area in that picture.
                        edLabel.setText(labels.get(0).getText());


                        // The text to speech is not working in my laptop, try to see if it works in yours.
                        tts.setPitch(1f);
                        tts.setSpeechRate(1f);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tts.speak(labels.get(0).getText(),TextToSpeech.QUEUE_FLUSH,null,null);
                        } else {
                            tts.speak(labels.get(0).getText(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });
    }

    // Function to prepare input image from Uri and Context
    protected void prepareInputImage(){
        try {
            imgToBeTested = InputImage.fromFilePath(this, getUriToDrawable(this,R.drawable.car));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Function to convert Filepath to Uri
    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }
}