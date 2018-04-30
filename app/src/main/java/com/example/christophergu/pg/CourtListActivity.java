package com.example.christophergu.pg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.christophergu.pg.data.Court;
import com.example.christophergu.pg.data.adapters.CourtArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CourtListActivity extends AppCompatActivity {

    private PGInterface service;

    private ListView mListView;
    private ArrayList<Court> courtList;
    public static String sportType;
    public static int cid = -1;

    private boolean dataRetrieved = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_court_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        sportType = intent.getStringExtra(getString(R.string.passSport));

        // Create retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://z3j1v77xu5.execute-api.us-east-1.amazonaws.com/beta/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PGInterface.class);

        mListView = findViewById(R.id.courtListView);
        courtList = new ArrayList<>();

        storeCourtInfos();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cid = courtList.get(position).getCid();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.next:
                if (sportType != null && cid != -1) {
                    Intent intent = new Intent(this, CreateGameActivity.class);
                    intent.putExtra(getString(R.string.passSport), sportType);
                    intent.putExtra(getString(R.string.passCID), cid);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please pick a court~~~",
                            Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                // The user's action was not recognized. Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void storeCourtInfos() {
        Call<List<Court>> model = service.getCourts(sportType);
        model.enqueue(new Callback<List<Court>>() {
            @Override
            public void onResponse(Call<List<Court>> call, Response<List<Court>> response) {
                for (Court court : response.body()) {
                    courtList.add(court);
                    System.out.println("Court id is: " + court.getCid());
                }
                CourtArrayAdapter adapter = new CourtArrayAdapter(getApplication(), R.layout.item_court_list, courtList);
                //adapter.setNotifyOnChange(true);
                mListView.setAdapter(adapter);
                dataRetrieved = true;
            }

            @Override
            public void onFailure(Call<List<Court>> call, Throwable t) {
                System.out.print(t.getMessage());
            }
        });
    }
}
