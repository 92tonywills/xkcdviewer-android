package com.github.tonywills.xkcdviewer.api;

import com.github.tonywills.xkcdviewer.api.model.Comic;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface XkcdApi {

    @GET("info.0.json") Call<Comic> getLatestComic();
    @GET("{number}/info.0.json") Call<Comic> getComic(@Path("number") int number);

}
