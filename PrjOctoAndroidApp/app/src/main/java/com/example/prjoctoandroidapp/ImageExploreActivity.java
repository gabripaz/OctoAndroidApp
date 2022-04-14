package com.example.prjoctoandroidapp;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ImageExploreActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgVwTest;
    InputImage imgToBeTested;
    ImageLabeler labeler;
    TextView tvLabel;
    TextToSpeech tts;
    //to take a picture
    Button btnTakePic, btnReturn;
    Bitmap imageBitmap;
    Uri captureUri;
    boolean flag=false;
    MediaPlayer mediaPlayerIEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_explore);
        initialize();

    }

    private void initialize() {
        mediaPlayerIEX = MediaPlayer.create(this,R.raw.playground_music);
        btnReturn = findViewById(R.id.btnExit);
        btnReturn.setOnClickListener(this);
        mediaPlayerIEX.setLooping(true);
        mediaPlayerIEX.start();
        imgVwTest = findViewById(R.id.imgVwTest);
        imgVwTest.setOnClickListener(this);
        labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        tvLabel = findViewById(R.id.tvLabel);
        //button to take a picture
        btnTakePic = findViewById(R.id.btnTakePic);
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=true;
                dispatchTakePictureIntent();
            }
        });

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

        if(view.getId() == R.id.btnExit){
            mediaPlayerIEX.stop();
            goToPlayground();
            finish();
        }else{
            prepareInputImage();
            processImage();
        }

    }

    private void goToPlayground() {
        /*Intent intent = new Intent(this, TransitionActivity.class);
        intent.putExtra("activityType","afterExplorer");
        startActivity(intent);*/ //if we want to go back to
        finish();
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
                        tvLabel.setText(labels.get(0).getText());


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
            //here we can get a the Uri from the picture taked by the user or we could use stored images
            if(flag){
                //if the user take a picture
                imgToBeTested = InputImage.fromFilePath(this, captureUri);
            }
            else{
                imgToBeTested = InputImage.fromFilePath(this, getUriToDrawable(this,R.drawable.octo));
            }

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


    ///Function to capture an image
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        // inside this method we are calling an implicit intent to capture an image.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // calling a start activity for result when image is captured.
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // inside on activity result method we are setting
        // our image to our image view from bitmap.
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            // on below line we are setting our
            // bitmap to our image view.
            imgVwTest.setImageBitmap(imageBitmap);
            captureUri = getImageUri(getApplicationContext(),imageBitmap);

        }
    }
    //Function to get the Uri from the picture taked by the user
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}