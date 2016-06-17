package com.github.tonywills.xkcdviewer.api;

import android.support.annotation.Nullable;

import com.github.tonywills.xkcdviewer.api.model.Comic;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class XkcdService {

    public static final XkcdService instance = new XkcdService();

    private final XkcdApi api;
    private int maxComicNumber = -1;

    public XkcdService() {
        api = new Retrofit.Builder().baseUrl("http://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(XkcdApi.class);
    }

    public void getLatestComic(final ComicCallback callback) {
        api.getLatestComic().enqueue(new Callback<Comic>() {
            @Override public void onResponse(Call<Comic> call, Response<Comic> response) {
                maxComicNumber = response.body().getNum();
                callback.complete(response.body());
            }

            @Override public void onFailure(Call<Comic> call, Throwable t) {
                callback.complete(null);
            }
        });
    }

    public void getComicByNumber(int number, final ComicCallback callback) {
        api.getComic(number).enqueue(new Callback<Comic>() {
            @Override public void onResponse(Call<Comic> call, Response<Comic> response) {
                callback.complete(response.body());
            }

            @Override public void onFailure(Call<Comic> call, Throwable t) {
                callback.complete(null);
            }
        });
    }

    public void getRandomComic(final ComicCallback callback) {
        if (maxComicNumber > 0) {
            api.getComic((int) (Math.random() * maxComicNumber) + 1).enqueue(new Callback<Comic>() {
                @Override public void onResponse(Call<Comic> call, Response<Comic> response) {
                    callback.complete(response.body());
                }

                @Override public void onFailure(Call<Comic> call, Throwable t) {
                    callback.complete(null);
                }
            });
        } else {
            getLatestComic(new ComicCallback() {
                @Override public void complete(@Nullable Comic comic) {
                    if (comic != null) {
                        maxComicNumber = comic.getNum();
                    }
                    getRandomComic(callback);
                }
            });
        }
    }

    public interface ComicCallback {
        void complete(@Nullable Comic comic);
    }

}
