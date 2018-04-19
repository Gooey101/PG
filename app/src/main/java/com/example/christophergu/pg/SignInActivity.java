package com.example.christophergu.pg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class SignInActivity extends Activity {

    private Retrofit retrofit;
    private APIInterface service;

    private EditText mPhoneNumber;
    public static final String regexStr = "^[0-9\\-]*$";
    public static final String strUsername = "username";
    public static final String strDob = "dob";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mPhoneNumber = findViewById(R.id.etPhone);

        retrofit= new Retrofit.Builder()
                .baseUrl("https://z3j1v77xu5.execute-api.us-east-1.amazonaws.com/beta/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(APIInterface.class);
    }


    public void enterPhone(View view) {

        // Default variables
        final String[] phoneNum = {"0001112222"};
        final String[] UserName = {"default"};
        final String[] DOB = {"01-01-2000"};

        String input = mPhoneNumber.getText().toString();
        if (input.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your phone number.",
                    Toast.LENGTH_SHORT).show();
        }
        else if (!input.matches(regexStr) || input.length()<10) {
            Toast.makeText(getApplicationContext(), "Please enter valid phone number.",
                    Toast.LENGTH_SHORT).show();
        }
        else {

            //Request for API
            Call<List<Account>> model = service.getAccount(input);
            final Intent login = new Intent(this, MainActivity.class);
            final Intent signUp = new Intent(this, SignUpActivity.class);
            model.enqueue(new Callback<List<Account>>() {
                @Override
                public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                    for (Account account: response.body()) {
                        System.out.println(account.getPhone().toString());
                    }

                    if (response.body().size() > 0) {
                        UserName[0] = response.body().get(0).getUsername();
                        phoneNum[0] = response.body().get(0).getPhone();
                        DOB[0] = response.body().get(0).getDob();

                        login.putExtra(strUsername, UserName[0]);
                        login.putExtra(strDob, DOB[0].substring(0, 10));
                        startActivity(login);
                    } else {
                        Toast.makeText(getApplicationContext(), "This number has not been registered yet. Please sign up~",
                                Toast.LENGTH_SHORT).show();
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

    public interface APIInterface {
        @GET("account")
        Call<List<Account>> getAccount(@Query("phone") String phone);
    }

    public class Account{
        private String username;
        private String phone;
        private String dob;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }
    }
}