package com.example.christophergu.pg;

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
import retrofit2.http.Body;
import retrofit2.http.POST;

public class SignUpActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private SignUpAPIInterface service;

    private EditText mPhoneNumber;
    private EditText mUserName;
    private EditText mDob;
    private Button mSignUp;
    public static final String regexStr = "^[0-9\\-]*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Set local attributes to corresponding views
        mPhoneNumber = findViewById(R.id.etPhone);
        mUserName = findViewById(R.id.etUsername);
        mDob = findViewById(R.id.etDob);
        mSignUp = findViewById(R.id.btnSignUp);

        //Create retrofit instance
        retrofit= new Retrofit.Builder()
                .baseUrl("https://z3j1v77xu5.execute-api.us-east-1.amazonaws.com/beta/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(SignUpAPIInterface.class);
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

        Account newAccount = new Account(mPhoneNumber.getText().toString(), mUserName.getText().toString(), mDob.getText().toString());
        Call<Account> call = service.createAccount(newAccount);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()){
                    isCreated[0] = true;
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {}
        });

        // Clear UI Fields
        mPhoneNumber.getText().clear();
        mUserName.getText().clear();
        mDob.getText().clear();
        if (isCreated[0])
            Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show();
    }

    public interface SignUpAPIInterface {
        @POST("account")
        Call<Account> createAccount(@Body Account body);
    }
}