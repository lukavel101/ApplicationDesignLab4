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


    public final static String TAG ="SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        TextView welcomeTextView = findViewById(R.id.textView2);
        ImageView profileImage = findViewById(R.id.imageView);
        Intent fromPrevious = getIntent();

        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        welcomeTextView.setText("Welcome back " + emailAddress);
        int age = fromPrevious.getIntExtra("Age", 0); // 0 is the default value if "Age" is not found
        String name = fromPrevious.getStringExtra("Name");
        String postalCode = fromPrevious.getStringExtra("PostalCode");

        Button callButton = findViewById(R.id.button2);
        EditText phoneNumberText = findViewById(R.id.editTextPhone);
        callButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String phoneNumber = phoneNumberText.getText().toString();

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));

                startActivity(callIntent);

            };});


        Button camButton = findViewById(R.id.button3);
        Intent cameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data =result.getData();
                            if (data != null) {
                                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                                profileImage.setImageBitmap(thumbnail);
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

        SharedPreferences prefs = getSharedPreferences("MyData", MODE_PRIVATE);
        String savedPhoneNumber = prefs.getString("PhoneNumber", "");

        phoneNumberText.setText(savedPhoneNumber);

        String filename = "Picture.png";
        File file = new File(getFilesDir(), filename);
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());

            profileImage.setImageBitmap(theImage);
        }

        camButton.setOnClickListener(view -> {
            cameraResult.launch(cameraIntent);
        });


    }

    @Override
    protected void onPause() {
        super.onPause();

        EditText phoneNumberText = findViewById(R.id.editTextPhone);
        String phoneNumber = phoneNumberText.getText().toString();

        SharedPreferences prefs = getSharedPreferences("MyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", phoneNumber);
        editor.apply();
    }
}