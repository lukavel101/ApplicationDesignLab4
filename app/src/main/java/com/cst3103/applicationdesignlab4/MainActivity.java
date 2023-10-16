package com.cst3103.applicationdesignlab4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w("MainActivity", "In onCreate() - Loading Widgets");

        // Find and reference the login button and email edit text from the XML layout
        Button loginButton = findViewById(R.id.button);
        EditText emailEditText = findViewById(R.id.editTextText);

        // Create an intent to navigate to the SecondActivity
        Intent next = new Intent(MainActivity.this, SecondActivity.class);

        // Get the text from the email edit text and add it to the intent
        next.putExtra("EmailAddress", emailEditText.getText().toString());

        // Access the SharedPreferences for storing data
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        // Set an onClickListener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the entered email address
                String emailAddress = emailEditText.getText().toString();

                // Edit the SharedPreferences to store the email address
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("LoginName", emailAddress);
                editor.apply(); // Apply the changes

                // Create an intent to navigate to SecondActivity and pass the email address
                Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
                nextPage.putExtra("EmailAddress", emailAddress);
                startActivity(nextPage); // Start SecondActivity
            }
        });

        // Retrieve the saved email address from SharedPreferences and set it in the email edit text
        String savedEmailAddress = prefs.getString("LoginName", "");
        emailEditText.setText(savedEmailAddress);
    }

    @Override
    protected void onStart() {
        Log.w("MainActivity", "The application is now visible on screen.");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.w("MainActivity", "The application is now responding to the user input.");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.w("MainActivity", "The application has now stopped running.");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.w("MainActivity", "The application is now paused.");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.w("MainActivity", "The application is now destroyed.");
        super.onDestroy();
    }
}

