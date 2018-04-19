package com.example.christophergu.pg;

import com.example.christophergu.pg.data.Account;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PGInterface {
    @GET("account")
    Call<List<Account>> getAccount(@Query("phone") String phone);

    @POST("account")
    Call<Account> createAccount(@Body Account body);
}
