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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christophergu.pg.data.Account;
import com.example.christophergu.pg.data.DeleteGame;
import com.example.christophergu.pg.data.EmergencyContact;
import com.example.christophergu.pg.data.Game;
import com.example.christophergu.pg.data.NewGame;
import com.example.christophergu.pg.data.QuitGame;
import com.example.christophergu.pg.data.Team;
import com.example.christophergu.pg.data.adapters.AccountArrayAdapter;
import com.example.christophergu.pg.data.adapters.GameArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mUsername;
    private TextView mLetter;
    private TextView mUserTeam;
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
        mLetter = findViewById(R.id.tvLetter);
        mListView = findViewById(R.id.lvUserGames);
        gameAccounts = new ArrayList<ArrayList<Account>>();
        mUserTeam = findViewById(R.id.tvUserTeam);


        // Retrieve Account information from SharedPreferences file
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.userInfo), Context.MODE_PRIVATE);
        phone = sharedPref.getString(getString(R.string.userPhone), null);
        username = sharedPref.getString(getString(R.string.userName), null);
        dob = sharedPref.getString(getString(R.string.userDOB), null).substring(0,10);




        // Set account values on Profile
        mUsername.setText(username);
        mLetter.setText(username.substring(0,1).toUpperCase());

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


        Call<List<Team>> call = service.getTeam(phone);
        call.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                if(response.body().size() > 0)
                    mUserTeam.setText(response.body().get(0).gettName());
            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {

            }
        });







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





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.emergency:
                //Check Conditions
                retrieveEmergencyData();

                return true;
            case R.id.remove:
                Call<String> call = service.removeAccount(phone);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getApplicationContext(),
                                "Account has been removed!",
                                Toast.LENGTH_SHORT).show();
                        returnToSignIn();
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
                return true;
            default:
                // The user's action was not recognized. Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void retrieveEmergencyData() {
        Call<List<EmergencyContact>> model = service.getContact(phone);
        model.enqueue(new Callback<List<EmergencyContact>>() {
            @Override
            public void onResponse(Call<List<EmergencyContact>> call, Response<List<EmergencyContact>> response) {
                if(response.body().size() > 0)
                    onCreateEmergencyDialog(response.body().get(0)).show();
                else
                    onCreateEmergencyDialog(null).show();

            }

            @Override
            public void onFailure(Call<List<EmergencyContact>> call, Throwable t) {

            }
        });
    }


    private void returnToSignIn() {
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
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


    private Dialog onCreateEmergencyDialog(final EmergencyContact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        LayoutInflater inflater = getLayoutInflater();
        final View infoView = inflater.inflate(R.layout.dialog_emergency_edit, null);
        final EditText etEmergencyName = infoView.findViewById(R.id.etEmergencyName);
        final EditText etEmergencyPhone = infoView.findViewById(R.id.etEmergencyPhone);
        final EditText etRelationship = infoView.findViewById(R.id.etRelationship);

        if(contact != null){
            etEmergencyName.setText(contact.getfName());
            etEmergencyPhone.setText(contact.getEcPhone());
            etRelationship.setText(contact.getRelationship());
        }
        builder.setView(infoView);

        builder.setTitle("Edit Emergency Contact");
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EmergencyContact newContact = new EmergencyContact(phone,etEmergencyName.getText().toString(),
                        etRelationship.getText().toString(), etEmergencyPhone.getText().toString());
                Call<EmergencyContact> callEmergency;
                if(contact!=null)
                    callEmergency = service.editContact(newContact);
                else
                    callEmergency = service.createEmergencyContact((newContact));
                callEmergency.enqueue(new Callback<EmergencyContact>() {
                    @Override
                    public void onResponse(Call<EmergencyContact> call, Response<EmergencyContact> response) {
                        Toast.makeText(getApplicationContext(),
                                "Emergency Contact Updated!",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<EmergencyContact> call, Throwable t) {

                    }
                });
            }
        });
        return builder.create();
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
        tvDate.setText(game.getGameDate().substring(0,10));
        tvTime.setText(game.getStartTime()+" - "+game.getEndTime());
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

                retrieveEmergencyData(phone, username, dob);

            }
        });


        builder.setView(infoView);
        if( !(phone.equals(game.getCreator()))) {
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
                            Toast.makeText(getApplicationContext(),
                                    "You quit the game!",
                                    Toast.LENGTH_SHORT).show();

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
                    Call<String> model = service.deleteGame(game.getGid());
                    gameArrayAdapter.notifyDataSetChanged();
                    model.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            storeGameInfos();
                            gameArrayAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(),
                                    "Game deleted!",
                                    Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
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

    private void retrieveEmergencyData(final String phone, final String username, final String dob) {

        Call<List<EmergencyContact>> model = service.getContact(phone);
        model.enqueue(new Callback<List<EmergencyContact>>() {
            @Override
            public void onResponse(Call<List<EmergencyContact>> call, Response<List<EmergencyContact>> response) {
                if(response.body().size() > 0){
                    EmergencyContact contact = response.body().get(0);
                    Toast.makeText(getApplicationContext(),
                            "Username: "+username+";\n"
                                    +"Phone: "+phone+";\n"
                                    +"Date of birth: "+dob.substring(0,10)+";\n"
                            +"Emergency Contact: "+contact.getEcPhone()
                                    +" ("+contact.getfName()+")",
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Username: "+username+";\n"
                                    +"Phone: "+phone+";\n"
                                    +"Date of birth: "+dob.substring(0,10),
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<EmergencyContact>> call, Throwable t) {

            }
        });
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

    public void GameFeed(View view) {
        Intent i = new Intent(this, GameFeedActivity.class);
        startActivity(i);
    }

    public void checkTeamStats(View view) {
        Intent i = new Intent(this, teamStatsActivity.class);
        startActivity(i);
    }

}