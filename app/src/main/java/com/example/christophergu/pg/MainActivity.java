package com.example.christophergu.pg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.christophergu.pg.data.Account;

public class MainActivity extends AppCompatActivity {

    private TextView mUsername, mDob;
    public Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set local attributes to corresponding views
        mUsername = findViewById(R.id.username);
        mDob = findViewById(R.id.dob);

        // Retrieve Account information from SharedPreferences file
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.userInfo), Context.MODE_PRIVATE);
        String phone = sharedPref.getString(getString(R.string.userPhone), null);
        String username = sharedPref.getString(getString(R.string.userName), null);
        String dob = sharedPref.getString(getString(R.string.userDOB), null).substring(0,10);

        mAccount = new Account(phone, username, dob);

        // Set account values on Profile
        mUsername.setText(username);
        mDob.setText(dob);
    }

    public void createGame(View view) {
        Intent intent = new Intent(this, SportListActivity.class);
        startActivity(intent);
    }
}