package com.example.christophergu.pg;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateGameActivity extends Activity {

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        retrofit= new Retrofit.Builder()
                .baseUrl("https://z3j1v77xu5.execute-api.us-east-1.amazonaws.com/beta/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //service = retrofit.create(APIInterface.class);

    }

    public void selectCourt(View view) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Choose a court");
    }
}