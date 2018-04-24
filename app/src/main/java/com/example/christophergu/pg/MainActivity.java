package com.example.christophergu.pg;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christophergu.pg.data.Account;
import com.example.christophergu.pg.data.Game;
import com.example.christophergu.pg.data.QuitGame;
import com.example.christophergu.pg.data.adaptors.GameArrayAdapter;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mUsername;
    private ListView mListView;
    private ArrayList<Game> gameList;
    private PGInterface service;
    private String phone;
    private String username;
    private String dob;
    private GameArrayAdapter gameArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set local attributes to corresponding views
        mUsername = findViewById(R.id.username);
        mListView = findViewById(R.id.lvUserGames);



        // Retrieve Account information from SharedPreferences file
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.userInfo), Context.MODE_PRIVATE);
        phone = sharedPref.getString(getString(R.string.userPhone), null);
        username = sharedPref.getString(getString(R.string.userName), null);
        dob = sharedPref.getString(getString(R.string.userDOB), null).substring(0,10);


        // Set account values on Profile
        mUsername.setText(username);

        gameList = new ArrayList<Game>();


        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://z3j1v77xu5.execute-api.us-east-1.amazonaws.com/beta/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PGInterface.class);
        storeGameInfos();


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onCreateDialog(position).show();
                gameArrayAdapter.notifyDataSetChanged();

            }
        });



    }



    public Dialog onCreateDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        LayoutInflater inflater = getLayoutInflater();
        View infoView = inflater.inflate(R.layout.game_info, null);
        TextView tvDate = infoView.findViewById(R.id.tvDate);
        TextView tvTime = infoView.findViewById(R.id.tvTime);
        TextView tvDescription = infoView.findViewById(R.id.tvDescription);
        final Game game = gameList.get(position);
        tvDate.setText(game.getGameDate().substring(0,10));
        tvTime.setText(game.getStartTime()+" - "+game.getEndTime());
        tvDescription.setText(game.getDescription());
        builder.setTitle(game.getSport());
        builder.setView(infoView);
        builder.setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                QuitGame quit = new QuitGame(0, game.getGid(), phone);
                Call<QuitGame> model = service.quitGame(quit);
                gameArrayAdapter.notifyDataSetChanged();
                model.enqueue(new Callback<QuitGame>() {
                    @Override
                    public void onResponse(Call<QuitGame> call, Response<QuitGame> response) {
                        gameArrayAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<QuitGame> call, Throwable t) {
                        gameArrayAdapter.notifyDataSetChanged();

                    }

                });
                storeGameInfos();
                gameArrayAdapter.notifyDataSetChanged();


            }
        });
        gameArrayAdapter.notifyDataSetChanged();

        return builder.create();
    }



    private void storeGameInfos() {
        Call<List<Game>> model = service.getGames(phone);
        gameList = new ArrayList<Game>();
        model.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                for (Game game: response.body()) {
                    gameList.add(game);
                }
                gameArrayAdapter = new GameArrayAdapter(getApplication(), R.layout.item_game_list, gameList);
                gameArrayAdapter.setNotifyOnChange(true);
                gameArrayAdapter.notifyDataSetChanged();
                mListView.setAdapter(gameArrayAdapter);


            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                System.out.print(t.getMessage());
            }
        });
    }

    public void createGame(View view) {
        Intent intent = new Intent(this, SportListActivity.class);
        startActivity(intent);
    }
}