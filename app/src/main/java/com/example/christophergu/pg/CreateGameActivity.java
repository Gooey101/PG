package com.example.christophergu.pg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.christophergu.pg.data.Event;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CreateGameActivity extends AppCompatActivity {

    private PGInterface service;

    private Retrofit retrofit;
    private Spinner spinner;
    private EditText etDescription, etDate, etStartTime, etEndTime, etMinAge, etMaxAge, etSkillLevel, etMaxCapacity;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    private String phone;
    private int cid;
    private String sportType;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.create_game:
                Date date;
                Time startTime;
                Time endTime;
                Event newEvent = null;

                newEvent = new Event(
                    phone,
                        etDescription.getText().toString(),
                        etDate.getText().toString(),
                        etStartTime.getText().toString(),
                        etEndTime.getText().toString(),
                        Integer.parseInt(etMinAge.getText().toString()),
                        Integer.parseInt(etMaxAge.getText().toString()),
                        Integer.parseInt(etSkillLevel.getText().toString()),
                        Integer.parseInt(etMaxCapacity.getText().toString()),
                        sportType,
                        cid
                );

                Toast.makeText(this, newEvent.toString(), Toast.LENGTH_SHORT).show();
                Call<Event> call = service.createEvent(newEvent);
                call.enqueue(new Callback<Event>() {
                    @Override
                    public void onResponse(Call<Event> call, Response<Event> response) {
                        if(response.isSuccessful()){
                            System.out.println("Successful!");
                        }
                    }
                    @Override
                    public void onFailure(Call<Event> call, Throwable t) {
                        System.out.print(t.getMessage());
                    }
                });
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        etDescription = findViewById(R.id.etEventDescription);
        etDate = findViewById(R.id.etDate);
        etDate.setShowSoftInputOnFocus(false);
        etStartTime = findViewById(R.id.etStartTime);
        etStartTime.setShowSoftInputOnFocus(false);
        etEndTime = findViewById(R.id.etEndTime);
        etEndTime.setShowSoftInputOnFocus(false);
        etMinAge = findViewById(R.id.etMinAge);
        etMaxAge = findViewById(R.id.etMaxAge);
        etSkillLevel = findViewById(R.id.etMinSkillLevel);
        etMaxCapacity = findViewById(R.id.etCapacity);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.userInfo), Context.MODE_PRIVATE);
        phone = sharedPref.getString(getString(R.string.userPhone), null);

        Intent intent = getIntent();
        sportType = intent.getStringExtra(getString(R.string.passSport));
        cid = intent.getIntExtra(getString(R.string.passCID), -1);

        switch(sportType){
            case "pools":
                sportType = "Swimming";
                break;
            case "basketball-courts":
                sportType = "Basketball";
                break;
            case "tennis-courts":
                sportType = "Tennis";
                break;
            case "soccer-fields":
                sportType = "Soccer";
                break;
            default:
                sportType = null;
        }
        //Toast.makeText(this, phone, Toast.LENGTH_SHORT).show();



        retrofit= new Retrofit.Builder()
                .baseUrl("https://z3j1v77xu5.execute-api.us-east-1.amazonaws.com/beta/games/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PGInterface.class);




    }


    public void selectDate(View view) {
        //Create the calender
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDialog(999);
    }

    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    String month = String.valueOf(arg2);
                    String day = String.valueOf(arg3);
                    if(arg2 < 10)
                        month = "0"+String.valueOf(arg2);
                    if(arg3 < 10)
                        day = "0"+String.valueOf(arg3);
                    etDate.setText(String.valueOf(arg1)+"-"+month+"-"+day);
                }
            };


    public void selectStartTime(View view) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setEditText(etStartTime);
        newFragment.show(this.getFragmentManager(), "timepicker");
    }


    public void selectEndTime(View view) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setEditText(etEndTime);
        newFragment.show(this.getFragmentManager(), "timepicker");
    }


}