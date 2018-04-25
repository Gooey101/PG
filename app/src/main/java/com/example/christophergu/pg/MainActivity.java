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

import com.example.christophergu.pg.data.Account;
import com.example.christophergu.pg.data.Game;
import com.example.christophergu.pg.data.QuitGame;
import com.example.christophergu.pg.data.adapters.AccountArrayAdapter;
import com.example.christophergu.pg.data.adapters.GameArrayAdapter;
import com.example.christophergu.pg.data.adapters.thing;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mUsername;
    private ListView mListView;
    private ListView playerList;
    private ArrayList<Game> gameList;
    private ArrayList<Account> accountList;
    private ArrayList<String> phoneList;
    private PGInterface service;
    private PGInterface service2;
    private String phone;
    private String username;
    private String dob;
    private GameArrayAdapter gameArrayAdapter;
    private AccountArrayAdapter accountArrayAdapter;
    private ArrayList<ArrayList<Account>> gameAccounts;
    private boolean dataReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set local attributes to corresponding views
        mUsername = findViewById(R.id.username);
        mListView = findViewById(R.id.lvUserGames);
        gameAccounts = new ArrayList<ArrayList<Account>>();



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
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("https://z3j1v77xu5.execute-api.us-east-1.amazonaws.com/beta/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PGInterface.class);
        service2 = retrofit2.create(PGInterface.class);
        storeGameInfos();








        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //phoneList = new ArrayList<>();
                //retrievePlayers(gameList.get(position).getGid());
                accountList = new ArrayList<>();
                readData(position);
                //onCreateDialog(position).show();
                gameArrayAdapter.notifyDataSetChanged();
                dataReady=false;
            }
        });



    }


    public void readData(final int position){
        phoneList = new ArrayList<>();
        final Game game = gameList.get(position);
        accountArrayAdapter = new AccountArrayAdapter(getApplication(), R.layout.item_player_list, accountList);
        accountArrayAdapter.setNotifyOnChange(true);
        Call<List<Account>> model2 = service2.getPlayers(game.getGid());
        model2.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                for(Account t: response.body()) {
                    phoneList.add(t.getPhone());
                    accountList.add(t);
                }

                //System.out.print(accountList.size());


                onCreateDialog(position).show();

                //right here you can call the other request and just give it the token

                dataReady=true;
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
        final View infoView = inflater.inflate(R.layout.game_info, null);
        TextView tvDate = infoView.findViewById(R.id.tvDate);
        TextView tvTime = infoView.findViewById(R.id.tvTime);
        TextView tvDescription = infoView.findViewById(R.id.tvDescription);
        final Game game = gameList.get(position);
        tvDate.setText(game.getGameDate().substring(0,10));
        tvTime.setText(game.getStartTime()+" - "+game.getEndTime());
        tvDescription.setText(game.getDescription());
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
            }
        });


        builder.setView(infoView);
        if( !phone.equals(game.getPhone())) {
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
        }else{
            builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
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
        }

        gameArrayAdapter.notifyDataSetChanged();
        synchronized(gameArrayAdapter){
            gameArrayAdapter.notifyAll();
        }
        return builder.create();

    }


//    private void retrievePlayers(int gid) {
//        Call<String> model = service2.getPlayers(gid);
//
//        model.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
////                for (String pho: response.body()){
////                    phoneList.add(pho);
////                }
//
////                accountArrayAdapter = new AccountArrayAdapter(getApplication(), R.layout.item_player_list, accountList);
////                accountArrayAdapter.setNotifyOnChange(true);
////                playerList.setAdapter(accountArrayAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//    }

    private void getAccountInfos(String pho) {

        Call<List<Account>> model2 = service.getAccount(pho);
        System.out.println(model2.toString());
        model2.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                for(Account account: response.body())
                    accountList.add(account);

            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                System.out.print(t.getMessage());
            }
        });
        System.out.print("Done");
    }


    private void storeGameInfos() {
        Call<List<Game>> model = service.getGames(phone);
        gameList = new ArrayList<Game>();
        model.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                for (Game game: response.body()) {
                    gameList.add(game);
                    gameAccounts.add(new ArrayList<Account>());
                }
                gameArrayAdapter = new GameArrayAdapter(getApplication(), R.layout.item_game_list, gameList);
                gameArrayAdapter.setNotifyOnChange(true);
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