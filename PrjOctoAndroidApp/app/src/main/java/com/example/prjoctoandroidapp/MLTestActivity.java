package com.example.prjoctoandroidapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.util.ArrayList;
import java.util.List;

public class MLTestActivity extends AppCompatActivity {

    // variables for our image view, image bitmap,
    // buttons, recycler view, adapter and array list.
    private ImageView img;
    private Button snap, labelBtn;
    private Bitmap imageBitmap;
    private RecyclerView resultRV;
    private resultRVAdapter resultRvAdapter;
    private ArrayList<DataModal> dataModalArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing all our variables for views
        img = (ImageView) findViewById(R.id.image);
        snap = (Button) findViewById(R.id.snapbtn);
        labelBtn = findViewById(R.id.labelBtn);
        resultRV = findViewById(R.id.idRVResults);

        // initializing our array list
        dataModalArrayList = new ArrayList<>();

        // initializing our adapter class.
        resultRvAdapter = new resultRVAdapter(dataModalArrayList, MLTestActivity.this);

        // layout manager for our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(MLTestActivity.this);

        // on below line we are setting layout manager
        // and adapter to our recycler view.
        resultRV.setLayoutManager(manager);
        resultRV.setAdapter(resultRvAdapter);

        // adding on click listener for our label button.
        labelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method
                // to label images.
                labelImage();
            }
        });
        // adding on click listener for our snap button.
        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method
                // to capture an image.
                dispatchTakePictureIntent();
            }
        });
    }

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
            img.setImageBitmap(imageBitmap);
        }
    }

    private void labelImage() {

        // inside the label image method we are calling a
        // firebase vision image and passing our image bitmap to it.
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

        // on below line we are creating a labeler for our image bitmap
        // and creating a variable for our firebase vision image labeler.
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();

        // calling a method to process an image and adding on success listener method to it.
        labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {

                // inside on success method we are running a loop to get the data from our list.
                for (FirebaseVisionImageLabel label : firebaseVisionImageLabels) {

                    // on below line we are getting text from our list.
                    String text = label.getText();

                    // on below line we are getting its entity id
                    String entityId = label.getEntityId();

                    // on below line we are getting the
                    // confidence level of our modal.
                    float confidence = label.getConfidence();

                    // after getting all data we are passing it to our array list.
                    dataModalArrayList.add(new DataModal(text, confidence));

                    // after adding a new data we are notifying
                    // our adapter that data has been updated.
                    resultRvAdapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // error handling for on failure method
                Toast.makeText(MLTestActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
