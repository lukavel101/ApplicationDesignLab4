package com.cst3103.applicationdesignlab4;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    // Define a constant tag for logging
    public final static String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Initialize views and retrieve data from the previous activity's intent
        TextView welcomeTextView = findViewById(R.id.textView2);
        ImageView profileImage = findViewById(R.id.imageView);
        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        welcomeTextView.setText("Welcome back " + emailAddress);

        // Retrieve additional data from the previous activity's intent
        int age = fromPrevious.getIntExtra("Age", 0); // 0 is the default value if "Age" is not found
        String name = fromPrevious.getStringExtra("Name");
        String postalCode = fromPrevious.getStringExtra("PostalCode");

        // Set up the "Call" button for making phone calls
        Button callButton = findViewById(R.id.button2);
        EditText phoneNumberText = findViewById(R.id.editTextPhone);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phoneNumberText.getText().toString();

                // Create an intent to initiate a phone call
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        });

        // Set up the "Camera" button for taking pictures
        Button camButton = findViewById(R.id.button3);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Initialize the ActivityResultLauncher for handling camera results
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                // Retrieve the captured image as a thumbnail
                                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                                // Set the thumbnail image in the ImageView
                                profileImage.setImageBitmap(thumbnail);

                                // Save the image to a file
                                FileOutputStream fOut = null;
                                try {
                                    fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                    fOut.flush();
                                    fOut.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
        );

        // Retrieve and display the saved phone number from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyData", MODE_PRIVATE);
        String savedPhoneNumber = prefs.getString("PhoneNumber", "");
        phoneNumberText.setText(savedPhoneNumber);

        // Load and display a saved image from the device's storage
        String filename = "Picture.png";
        File file = new File(getFilesDir(), filename);
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            profileImage.setImageBitmap(theImage);
        }

        // Set up the Camera button to launch the camera intent
        camButton.setOnClickListener(view -> {
            cameraResult.launch(cameraIntent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save the entered phone number to SharedPreferences when the activity is paused
        EditText phoneNumberText = findViewById(R.id.editTextPhone);
        String phoneNumber = phoneNumberText.getText().toString();

        SharedPreferences prefs = getSharedPreferences("MyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", phoneNumber);
        editor.apply();
    }
}
