package com.example.christophergu.pg;

import com.example.christophergu.pg.data.Account;
import com.example.christophergu.pg.data.Court;
import com.example.christophergu.pg.data.Game;
import com.example.christophergu.pg.data.QuitGame;
import com.example.christophergu.pg.data.adapters.thing;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PGInterface {
    @GET("games/game/accounts")
    Call<List<Account>> getPlayers(@Query("gid") int gid);

    @GET("account")
    Call<List<Account>> getAccount(@Query("phone") String phone);

    @GET("courts/{sportType}")
    Call<List<Court>> getCourts(@Path("sportType") String sportType);

    @POST("account")
    Call<Account> createAccount(@Body Account body);

    @POST("game")
    Call<Game> createGame(@Body Game body);

    @GET("account/games")
    Call<List<Game>> getGames(@Query("phone") String phone);

    @PATCH("games/game")
    Call<QuitGame> quitGame(@Body QuitGame body);


}
