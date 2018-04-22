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
    public Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mUsername = findViewById(R.id.username);
        mDob = findViewById(R.id.dob);


        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.userInfo), Context.MODE_PRIVATE);

        String phone = sharedPref.getString(getString(R.string.userPhone), null);
        String username = sharedPref.getString(getString(R.string.userName), null);
        String dob = sharedPref.getString(getString(R.string.userDOB), null).substring(0,10);


        account = new Account(phone, username, dob);

        mUsername.setText(username);
        mDob.setText(dob);
    }

    public void createEvent(View view) {
        Intent intent = new Intent(this, SportListActivity.class);
        startActivity(intent);

    }
}