package com.example.christophergu.pg;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christophergu.pg.data.Account;
import com.example.christophergu.pg.data.EmergencyContact;
import com.example.christophergu.pg.data.Game;
import com.example.christophergu.pg.data.QuitGame;
import com.example.christophergu.pg.data.adapters.AccountArrayAdapter;
import com.example.christophergu.pg.data.adapters.GameArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameFeedActivity extends AppCompatActivity {

    private String phone;
    private String username;
    private String dob;
    private int age;
    private PGInterface service;
    private ListView lvGameFeed;
    private ListView playerList;
    private GameArrayAdapter gameArrayAdapter;
    private ArrayList<Game> gameList;
    private ArrayList<Account> accountList;
    private ArrayList<String> phoneList;
    private AccountArrayAdapter accountArrayAdapter;
    private ArrayList<ArrayList<Account>> gameAccounts;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_game_feed);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Retrieve Account information from SharedPreferences file
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.userInfo), Context.MODE_PRIVATE);
        phone = sharedPref.getString(getString(R.string.userPhone), null);
        username = sharedPref.getString(getString(R.string.userName), null);
        dob = sharedPref.getString(getString(R.string.userDOB), null).substring(0, 10);
        age = sharedPref.getInt(getString(R.string.age), 0);


        lvGameFeed = findViewById(R.id.lvGameFeed);
        gameList = new ArrayList<Game>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://z3j1v77xu5.execute-api.us-east-1.amazonaws.com/beta/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PGInterface.class);


        getGamesFeed();


    }

    private void getGamesFeed() {
        Call<List<Game>> model = service.getAllGames(phone);
        gameList = new ArrayList<Game>();
        model.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                for (Game game : response.body()) {
                    gameList.add(game);
                }
                gameArrayAdapter = new GameArrayAdapter(getApplication(), R.layout.item_game_list, gameList);
                gameArrayAdapter.setNotifyOnChange(true);
                setLvAdapter();


            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                System.out.print(t.getMessage());
            }
        });

    }


    private void setLvAdapter() {
        lvGameFeed.setAdapter(gameArrayAdapter);


        lvGameFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                accountList = new ArrayList<>();
                readData(position);
                gameArrayAdapter.notifyDataSetChanged();
            }
        });
    }


    public void readData(final int position) {
        phoneList = new ArrayList<>();
        final Game game = gameList.get(position);
        accountArrayAdapter = new AccountArrayAdapter(getApplication(), R.layout.item_player_list, accountList);
        accountArrayAdapter.setNotifyOnChange(true);
        Call<List<Account>> model = service.getPlayers(game.getGid());
        model.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                for (Account t : response.body()) {
                    phoneList.add(t.getPhone());
                    accountList.add(t);
                }
                onCreateDialog(position).show();
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {

            }
        });
    }


    public Dialog onCreateDialog(int position) {
        phoneList = new ArrayList<String>();
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        LayoutInflater inflater = getLayoutInflater();
        final View infoView = inflater.inflate(R.layout.dialog_game_info, null);
        TextView tvDate = infoView.findViewById(R.id.tvDate);
        TextView tvTime = infoView.findViewById(R.id.tvTime);
        TextView tvDescription = infoView.findViewById(R.id.tvDescription);
        TextView tvSkillLevel = infoView.findViewById(R.id.tvSkillLevel);
        final Game game = gameList.get(position);
        tvDate.setText(game.getGameDate().substring(0, 10));
        tvTime.setText(game.getStartTime() + " - " + game.getEndTime());
        tvDescription.setText(game.getDescription());
        tvSkillLevel.setText(String.valueOf(game.getMinSkillLevel()));
        builder.setTitle(game.getSport());

        //Player list of this game
        playerList = infoView.findViewById(R.id.lvGamePlayers);
        //retrievePlayers(game.getGid());

        accountArrayAdapter = new AccountArrayAdapter(getApplication(), R.layout.item_player_list, accountList);
        accountArrayAdapter.notifyDataSetChanged();
        playerList.setAdapter(accountArrayAdapter);

        playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Account player = accountList.get(position);
                String phone = player.getPhone();
                String username = player.getUsername();
                String dob = player.getDob();
                String age = String.valueOf(player.getAge());

                retrieveEmergencyData(phone, username, dob);

            }
        });


        builder.setView(infoView);
        if (!(phone.equals(game.getCreator()))) {
            builder.setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (age >= game.getMinAge() && age <= game.getMaxAge()) {
                        QuitGame quit = new QuitGame(1, game.getGid(), phone);
                        Call<QuitGame> model = service.quitGame(quit);
                        gameArrayAdapter.notifyDataSetChanged();
                        model.enqueue(new Callback<QuitGame>() {
                            @Override
                            public void onResponse(Call<QuitGame> call, Response<QuitGame> response) {
                                gameArrayAdapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(),
                                        "You joined the game!",
                                        Toast.LENGTH_SHORT).show();
                                returnToMain();
                            }

                            @Override
                            public void onFailure(Call<QuitGame> call, Throwable t) {
                                gameArrayAdapter.notifyDataSetChanged();

                            }

                        });
                        gameArrayAdapter.notifyDataSetChanged();
                        returnToMain();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Allowed age range: " + game.getMinAge() + " ~ " + game.getMaxAge(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        gameArrayAdapter.notifyDataSetChanged();
        synchronized (gameArrayAdapter) {
            gameArrayAdapter.notifyAll();
        }
        return builder.create();

    }


    private void returnToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void retrieveEmergencyData(final String phone, final String username, final String dob) {

        Call<List<EmergencyContact>> model = service.getContact(phone);
        model.enqueue(new Callback<List<EmergencyContact>>() {
            @Override
            public void onResponse(Call<List<EmergencyContact>> call, Response<List<EmergencyContact>> response) {

                if (response.body().size() > 0) {
                    EmergencyContact contact = response.body().get(0);
                    Toast.makeText(getApplicationContext(),
                            "Username: " + username + ";\n"
                                    + "Phone: " + phone + ";\n"
                                    + "Date of birth: " + dob.substring(0, 10) + ";\n"
                                    + "Emergency Contact: " + contact.getEcPhone()
                                    + " (" + contact.getfName() + ")",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Username: " + username + ";\n"
                                    + "Phone: " + phone + ";\n"
                                    + "Date of birth: " + dob.substring(0, 10),
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<EmergencyContact>> call, Throwable t) {

            }
        });
    }
}
