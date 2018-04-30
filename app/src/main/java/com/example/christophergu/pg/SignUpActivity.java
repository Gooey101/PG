package com.example.christophergu.pg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christophergu.pg.data.Account;
import com.example.christophergu.pg.data.EmergencyContact;
import com.example.christophergu.pg.data.joinTeam;

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
    private EditText mEmergencyPhone;
    private EditText mEmergencyName;
    private EditText mEmergencyRelationship;
    private TextView tvTeamDescription;
    private Spinner spinner;
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
        mEmergencyPhone = findViewById(R.id.etEmergencyPhone);
        mEmergencyName = findViewById(R.id.etEmergencyName);
        mEmergencyRelationship = findViewById(R.id.etEmergencyRelationship);
        mSignUp = findViewById(R.id.btnSignUp);
        spinner = (Spinner) findViewById(R.id.spTeams);
        tvTeamDescription = findViewById(R.id.tvTeamDescription);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.teamPick, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Select the team
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinner.getSelectedItem().toString()) {
                    case "Google":
                        tvTeamDescription.setText(R.string.googleDescription);
                        break;
                    case "Apple":
                        tvTeamDescription.setText(R.string.appleDescription);
                        break;
                    case "Amazon":
                        tvTeamDescription.setText(R.string.amazonDescription);
                        break;
                    case "Facebook":
                        tvTeamDescription.setText(R.string.facebookDescription);
                        break;
                    default:
                        tvTeamDescription.setText("No information");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Create retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
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
        // Validate the information of the sign up information
        if (!mPhoneNumber.getText().toString().matches(regexStr) ||
                mPhoneNumber.getText().length() < 10 ||
                mUserName.getText().length() == 0 ||
                mDob.getText().length() == 0 ||
                mEmergencyPhone.getText().length() == 0 ||
                mEmergencyName.getText().length() == 0 ||
                mEmergencyRelationship.getText().length() == 0) {
            Toast.makeText(this, "Please enter valid information!", Toast.LENGTH_SHORT).show();
            return;
        }


        String item = spinner.getSelectedItem().toString();
        // Request API to create Account
        Account newAccount = new Account(mPhoneNumber.getText().toString(), mUserName.getText().toString(), mDob.getText().toString());
        Call<Account> call = service.createAccount(newAccount);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    isCreated[0] = true;
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
            }
        });


        String ecName = mEmergencyName.getText().toString();
        String ecPhone = mEmergencyPhone.getText().toString();
        String ecRelationship = mEmergencyRelationship.getText().toString();

        // Create new emergency contact
        EmergencyContact newContact = new EmergencyContact(mPhoneNumber.getText().toString(), ecName, ecRelationship, ecPhone);
        Call<EmergencyContact> callEmergency = service.createEmergencyContact(newContact);
        callEmergency.enqueue(new Callback<EmergencyContact>() {
            @Override
            public void onResponse(Call<EmergencyContact> call, Response<EmergencyContact> response) {

                returnToSignIn();
            }

            @Override
            public void onFailure(Call<EmergencyContact> call, Throwable t) {

            }
        });


        // Get the user's selection for team
        int tid = (int) spinner.getSelectedItemId();
        System.out.println("TID IS :  " + tid);
        switch (spinner.getSelectedItem().toString()) {
            case "Google":
                tid = 1;
                break;
            case "Apple":
                tid = 2;
                break;
            case "Amazon":
                tid = 3;
                break;
            case "Facebook":
                tid = 4;
                break;
            default:
                tid = 1;
                break;

        }

        //Joins the user with the selected team
        joinTeam join = new joinTeam(mPhoneNumber.getText().toString(), tid);
        Call<joinTeam> model = service.joinTeam(join);
        model.enqueue(new Callback<joinTeam>() {
            @Override
            public void onResponse(Call<joinTeam> call, Response<joinTeam> response) {
                mPhoneNumber.getText().clear();
                mUserName.getText().clear();
                mDob.getText().clear();
                mEmergencyPhone.getText().clear();
                mEmergencyName.getText().clear();
                mEmergencyRelationship.getText().clear();

            }

            @Override
            public void onFailure(Call<joinTeam> call, Throwable t) {

            }
        });


    }

    private void returnToSignIn() {
        // Return to signin Activity after signing up successfully
        Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
    }
}