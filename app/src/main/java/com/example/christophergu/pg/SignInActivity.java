package com.example.christophergu.pg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.christophergu.pg.data.Account;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends Activity {

    private PGInterface service;

    private EditText mPhoneNumber;
    public static final String regexStr = "^[0-9\\-]*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mPhoneNumber = findViewById(R.id.etPhone);

        // Create retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://z3j1v77xu5.execute-api.us-east-1.amazonaws.com/beta/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PGInterface.class);
    }


    public void enterPhone(View view) {

        // Default variables
        final String[] phoneNum = {"0001112222"};
        final String[] UserName = {"default"};
        final String[] DOB = {"01-01-2000"};
        final boolean[] exists = {false};
        final int[] age = {0};

        // Check phone number
        String input = mPhoneNumber.getText().toString();
        if (input.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your phone number.",
                    Toast.LENGTH_SHORT).show();
        } else if (!input.matches(regexStr) || input.length() < 10) {
            Toast.makeText(getApplicationContext(), "Please enter valid phone number.",
                    Toast.LENGTH_SHORT).show();
        } else {

            // Create explicit intents
            final Intent login = new Intent(this, MainActivity.class);
            final Intent signUp = new Intent(this, SignUpActivity.class);

            // Request API to getAccount
            Call<List<Account>> model = service.getAccount(input);
            model.enqueue(new Callback<List<Account>>() {
                @Override
                public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                    exists[0] = true;
                    for (Account account : response.body()) {
                        System.out.println(account.getPhone().toString());
                    }

                    if (response.body().size() > 0) {
                        // Retrieve Account information
                        phoneNum[0] = response.body().get(0).getPhone();
                        UserName[0] = response.body().get(0).getUsername();
                        DOB[0] = response.body().get(0).getDob();
                        age[0] = response.body().get(0).getAge();

                        // Create SharedPreferences with Account information
                        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                                getString(R.string.userInfo), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.userName), UserName[0]);
                        editor.putString(getString(R.string.userPhone), phoneNum[0]);
                        editor.putString(getString(R.string.userDOB), DOB[0]);
                        editor.putInt(getString(R.string.age), age[0]);
                        editor.apply();

                        // Go to MainActivity

                        startActivity(login);
                    } else {
                        Toast.makeText(getApplicationContext(), "This number has not been registered yet. Please sign up~",
                                Toast.LENGTH_SHORT).show();

                        // Go to SignUpActivity
                        startActivity(signUp);
                    }
                }

                @Override
                public void onFailure(Call<List<Account>> call, Throwable t) {
                    System.out.print(t.getMessage());

                }

            });

        }
    }
}