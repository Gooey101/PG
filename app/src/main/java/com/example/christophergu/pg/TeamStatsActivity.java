package com.example.christophergu.pg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.christophergu.pg.data.Team;
import com.example.christophergu.pg.data.adapters.TeamArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TeamStatsActivity extends AppCompatActivity {

    private PGInterface service;
    private ListView lvAllTeams;
    private ArrayList<Team> allTeams;
    private TextView tvTeamName;
    private TextView tvTeamNum;
    private TextView tvTeamDescription;
    private TeamArrayAdapter teamArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_team_stats);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvAllTeams = findViewById(R.id.lvAllTeams);
        tvTeamName = findViewById(R.id.tvTeamName);
        tvTeamNum = findViewById(R.id.tvTeamNum);
        tvTeamDescription = findViewById(R.id.tvTeamDescription);

        allTeams = new ArrayList<>();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://z3j1v77xu5.execute-api.us-east-1.amazonaws.com/beta/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PGInterface.class);

        // Retrieve all teams data
        Call<List<Team>> call = service.getAllTeams(0);
        call.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                for (Team t : response.body()) {
                    allTeams.add(t);
                }
                teamArrayAdapter = new TeamArrayAdapter(getApplicationContext(), R.layout.item_team_list, allTeams);
                lvAllTeams.setAdapter(teamArrayAdapter);

            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {

            }
        });


    }


    // Button to show all team informations
    public void viewAll(View view) {
        allTeams = new ArrayList<>();
        Call<List<Team>> call = service.getAllTeams(0);
        call.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                for (Team t : response.body()) {
                    allTeams.add(t);
                }
                teamArrayAdapter = new TeamArrayAdapter(getApplicationContext(), R.layout.item_team_list, allTeams);
                lvAllTeams.setAdapter(teamArrayAdapter);
                teamArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {

            }
        });

    }

    // Button to show the teams with maximum amount of members
    public void viewMax(View view) {
        allTeams = new ArrayList<>();
        Call<List<Team>> call = service.getAllTeams(1);
        call.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                for (Team t : response.body()) {
                    allTeams.add(t);
                }
                teamArrayAdapter = new TeamArrayAdapter(getApplicationContext(), R.layout.item_team_list, allTeams);
                lvAllTeams.setAdapter(teamArrayAdapter);
                teamArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {

            }
        });
    }

    // Button to show the teams with minimum amount of members
    public void viewMin(View view) {
        allTeams = new ArrayList<>();
        Call<List<Team>> call = service.getAllTeams(2);
        call.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                for (Team t : response.body()) {
                    allTeams.add(t);
                }
                teamArrayAdapter = new TeamArrayAdapter(getApplicationContext(), R.layout.item_team_list, allTeams);
                lvAllTeams.setAdapter(teamArrayAdapter);
                teamArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {

            }
        });
    }
}
