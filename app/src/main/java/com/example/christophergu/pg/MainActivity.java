package com.example.christophergu.pg;

import android.content.Intent;
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
        String phone = intent.getStringExtra(SignInActivity.strPhone);
        String username = intent.getStringExtra(SignInActivity.strUsername);
        String dob = intent.getStringExtra(SignInActivity.strDob);
        Account account = new Account(phone, username, dob);

        mUsername.setText(username);
        mDob.setText(dob);
    }
}