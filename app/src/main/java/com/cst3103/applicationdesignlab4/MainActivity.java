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
        Log.w( "MainActivity", "In onCreate() - Loading Widgets");


        Button loginButton = findViewById(R.id.button);
        EditText emailEditText = findViewById(R.id.editTextText);

        Intent next = new Intent( MainActivity.this, SecondActivity.class);

        next.putExtra("EmailAddress", emailEditText.getText().toString());
        next.putExtra("Age", 21);
        next.putExtra("Name", "Luka Veljovic");
        next.putExtra("PostalCode", "K2S2M7");

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = emailEditText.getText().toString();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("LoginName", emailAddress);
                editor.putInt("Age", 20);  // Fixed the age value
                editor.apply();

                Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
                nextPage.putExtra("EmailAddress", emailAddress);  // Fixed the Intent line
                startActivity(nextPage);
            }
        });


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