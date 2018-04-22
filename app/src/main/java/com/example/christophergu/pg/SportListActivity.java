package com.example.christophergu.pg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SportListActivity extends AppCompatActivity {

    private ListView mListView;
    private String sportType;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.next:
                if(sportType != null) {
                    Intent intent = new Intent(this, CourtsListActivity.class);
                    intent.putExtra(getString(R.string.passSport), sportType);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Please pick a sport type~~~",
                            Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = findViewById(R.id.sportListView);
        final String[] sportTypes = {"Swimming", "Basketball", "Tennis", "Soccer"};

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.sport_row, R.id.tvSportType, sportTypes);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(sportTypes[position]){
                    case "Swimming":
                        sportType = "pools";
                        break;
                    case "Basketball":
                        sportType = "basketball-courts";
                        break;
                    case "Tennis":
                        sportType = "tennis-courts";
                        break;
                    case "Soccer":
                        sportType = "soccer-fields";
                        break;
                    default:
                        sportType = null;
                }
            }
        });

    }

}
