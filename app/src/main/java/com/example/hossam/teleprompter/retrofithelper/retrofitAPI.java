package com.example.hossam.teleprompter.retrofithelper;

import com.example.hossam.teleprompter.helper.Response;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Mina William on 5/18/17.
 * Xdigital Group company
 */

public interface retrofitAPI {

    @GET("getposts.php")
   Call<Response>getPosts();

}
