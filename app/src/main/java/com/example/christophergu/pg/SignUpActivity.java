package com.example.christophergu.pg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.christophergu.pg.data.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private PGInterface service;

    private EditText mPhoneNumber;
    private EditText mUserName;
    private EditText mDob;
    private Button mSignUp;
    public static final String regexStr = "^[0-9\\-]*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set local attributes to corresponding views
        mPhoneNumber = findViewById(R.id.etPhone);
        mUserName = findViewById(R.id.etUsername);
        mDob = findViewById(R.id.etDob);
        mSignUp = findViewById(R.id.btnSignUp);

        //Create retrofit instance
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://z3j1v77xu5.execute-api.us-east-1.amazonaws.com/beta/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PGInterface.class);
    }

    /**
     * This method is called when user clicks on the Sign Up button
     *
     * @param view The calling view (button)
     */
    public void signUp(View view) {
        final boolean[] isCreated = {false};
        if (!mPhoneNumber.getText().toString().matches(regexStr) ||
                mPhoneNumber.getText().length() < 10 ||
                mUserName.getText().length() == 0 ||
                mDob.getText().length() == 0) {
            Toast.makeText(this, "Please enter valid information!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Request API to create Account
        Account newAccount = new Account(mPhoneNumber.getText().toString(), mUserName.getText().toString(), mDob.getText().toString());
        Call<Account> call = service.createAccount(newAccount);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()) {
                    isCreated[0] = true;
                }
                returnToSignIn();
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {}
        });

        // Clear UI Fields
        mPhoneNumber.getText().clear();
        mUserName.getText().clear();
        mDob.getText().clear();


    }

    private void returnToSignIn() {
        Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
    }
}