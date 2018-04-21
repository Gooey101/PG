package com.example.christophergu.pg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.christophergu.pg.data.Account;

public class MainActivity extends AppCompatActivity {

    private TextView mUsername, mDob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = findViewById(R.id.username);
        mDob = findViewById(R.id.dob);

        Intent intent = getIntent();
        String intentPhone = intent.getStringExtra(SignInActivity.strPhone);
        String intentUsername = intent.getStringExtra(SignInActivity.strUsername);
        String intentDob = intent.getStringExtra(SignInActivity.strDob);

        // Set account information
        mUsername.setText(intentUsername);
        mDob.setText(intentDob);

        // Write to shared preferences
        SharedPreferences writeSharedPref = this.getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = writeSharedPref.edit();
        editor.putString("accountPhone", intentPhone);
        editor.putString("accountUsername", intentUsername);
        editor.putString("accountDob", intentDob);
        editor.apply();

        // Read from shared preferences
        /*
        SharedPreferences readSharedPref = getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
        String phone = readSharedPref.getString("accountPhone", null);
        String username = readSharedPref.getString("accountUsername", null);
        String dob = readSharedPref.getString("accountDob", null);
        */
    }
}