package com.example.christophergu.pg;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.christophergu.pg.data.Game;
import com.example.christophergu.pg.data.NewGame;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CreateGameActivity extends AppCompatActivity {

    private PGInterface service;

    private EditText mDescription, mDate, mStartTime, mEndTime, mMinAge, mMaxAge, mSkillLevel, mCapacity;
    private String mSport;
    private Calendar mCalendar;
    private int mYear, mMonth, mDay;
    private String mPhone;
    private int mCid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set local attributes to corresponding views
        mDescription = findViewById(R.id.etEventDescription);
        mDate = findViewById(R.id.etDate);
        mDate.setShowSoftInputOnFocus(false);
        mStartTime = findViewById(R.id.etStartTime);
        mStartTime.setShowSoftInputOnFocus(false);
        mEndTime = findViewById(R.id.etEndTime);
        mEndTime.setShowSoftInputOnFocus(false);
        mMinAge = findViewById(R.id.etMinAge);
        mMaxAge = findViewById(R.id.etMaxAge);
        mSkillLevel = findViewById(R.id.etMinSkillLevel);
        mCapacity = findViewById(R.id.etCapacity);

        // Retrieve Account information from SharedPreferences file
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.userInfo), Context.MODE_PRIVATE);
        mPhone = sharedPref.getString(getString(R.string.userPhone), null);

        // Retrieve values from intent
        Intent intent = getIntent();
        mSport = intent.getStringExtra(getString(R.string.passSport));
        mCid = intent.getIntExtra(getString(R.string.passCID), -1);

        // Figure out sport type
        switch(mSport) {
            case "pools":
                mSport = "Swimming";
                break;
            case "basketball-courts":
                mSport = "Basketball";
                break;
            case "tennis-courts":
                mSport = "Tennis";
                break;
            case "soccer-fields":
                mSport = "Soccer";
                break;
            default:
                mSport = null;
        }

        // Create retrofit instance
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://z3j1v77xu5.execute-api.us-east-1.amazonaws.com/beta/games/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PGInterface.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_game:
                //Check Conditions
                int skill = Integer.parseInt(mSkillLevel.getText().toString());

                if(skill < 1 || skill > 5)
                    showErrorMessage(0);
                else{

                    // Create new Game
                    NewGame newGame = new NewGame(
                        mPhone,
                        mDescription.getText().toString(),
                        mDate.getText().toString(),
                        mStartTime.getText().toString(),
                        mEndTime.getText().toString(),
                        Integer.parseInt(mMinAge.getText().toString()),
                        Integer.parseInt(mMaxAge.getText().toString()),
                        Integer.parseInt(mSkillLevel.getText().toString()),
                        Integer.parseInt(mCapacity.getText().toString()),
                        mSport,
                        mCid
                    );
                    Toast.makeText(this, newGame.toString(), Toast.LENGTH_SHORT).show();

                    // Request API to createGame
                    Call<NewGame> call = service.createGame(newGame);
                    call.enqueue(new Callback<NewGame>() {
                        @Override
                        public void onResponse(Call<NewGame> call, Response<NewGame> response) {
                            returnToMain();
                        }
                        @Override
                        public void onFailure(Call<NewGame> call, Throwable t) {
                            showErrorMessage(1);

                        }
                    });
                }
                return true;
            default:
                // The user's action was not recognized. Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void showErrorMessage(int num) {
        switch(num){
            case 0:
                Toast.makeText(this, "Skill level has to be between 1 and 5~", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "Failed... Something went wrong...", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void returnToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void selectDate(View view) {
        //Create the calender
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        showDialog(999);
    }

    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year; arg2 = month; arg3 = day
            String month = String.valueOf(arg2);
            String day = String.valueOf(arg3);
            if(arg2 < 10)
                month = "0"+String.valueOf(arg2);
            if(arg3 < 10)
                day = "0"+String.valueOf(arg3);
            mDate.setText(String.valueOf(arg1)+"-"+month+"-"+day);
        }
    };

    public void selectStartTime(View view) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setEditText(mStartTime);
        newFragment.show(this.getFragmentManager(), "timepicker");
    }

    public void selectEndTime(View view) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setEditText(mEndTime);
        newFragment.show(this.getFragmentManager(), "timepicker");
    }
}