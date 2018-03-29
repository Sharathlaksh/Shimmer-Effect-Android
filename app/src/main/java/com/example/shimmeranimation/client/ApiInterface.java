package com.example.shimmeranimation.client;

import com.example.shimmeranimation.model.Album;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("photos")
    Call<List<Album>> getAlbum();
}
