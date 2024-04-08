package com.example.qrconnect;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.Call;

public interface FcmApi {

    @POST("/send")
    Call<Void> sendMessage(
            @Body SendMessageData body
    );

    @POST("/broadcast")
    Call<Void> broadcast(
            @Body SendMessageData body
    );
}