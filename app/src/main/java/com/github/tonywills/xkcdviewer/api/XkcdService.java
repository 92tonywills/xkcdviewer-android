package com.github.tonywills.xkcdviewer.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.github.tonywills.xkcdviewer.api.model.Comic;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class XkcdService {

    private static XkcdService instance;
    private Context context;

    public static XkcdService getInstance(Context context) {
        if (instance == null) { instance = new XkcdService(context); }
        return instance;
    }

    private static final String PREF_FILE = "xkcdprefs";
    private static final String PREF_KEY_FAVOURITES = "favourites";

    private final XkcdApi api;
    private final SharedPreferences xkcdprefs;
    private final Set<String> favouriteComics;
    private int maxComicNumber = -1;

    public XkcdService(Context context) {
        this.context = context;
        xkcdprefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        favouriteComics = xkcdprefs.getStringSet(PREF_KEY_FAVOURITES, new HashSet<String>());
        api = new Retrofit.Builder().baseUrl("http://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(XkcdApi.class);
    }

    public void getLatestComic(final ComicCallback callback) {
        api.getLatestComic().enqueue(new Callback<Comic>() {
            @Override public void onResponse(Call<Comic> call, Response<Comic> response) {
                maxComicNumber = response.body().getNum();
                callCompletion(response, callback);
            }

            @Override public void onFailure(Call<Comic> call, Throwable t) {
                callback.complete(null);
            }
        });
    }

    public void getComicByNumber(int number, final ComicCallback callback) {
        api.getComic(number).enqueue(new Callback<Comic>() {
            @Override public void onResponse(Call<Comic> call, Response<Comic> response) {
                callCompletion(response, callback);
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
                    callCompletion(response, callback);
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

    private void callCompletion(Response<Comic> response, ComicCallback callback) {
        Comic comic = response.body();
        String comicJson = new Gson().toJson(comic);
        comic.setFavourite(favouriteComics.contains(comicJson));
        callback.complete(comic);
    }

    public void getFavouriteComics(final ComicListCallback callback) {
        new AsyncTask<Void, Void, List<Comic>>() {
            @Override protected List<Comic> doInBackground(Void[] params) {
                List<Comic> comics = new ArrayList<>();
                for (String comicJson : favouriteComics) {
                    comics.add(new Gson().fromJson(comicJson, Comic.class));
                }
                return comics;
            }

            @Override protected void onPostExecute(List<Comic> comics) {
                callback.complete(comics);
            }
        }.execute();
    }

    public void setComicFavourite(Comic comic, boolean favourite) {
        comic.setFavourite(favourite);
        String comicJson = new Gson().toJson(comic);
        if (favourite) {
            saveImageForComic(comic);
            favouriteComics.add(comicJson);
        } else {
            favouriteComics.remove(comicJson);
        }
        xkcdprefs.edit().putStringSet(PREF_KEY_FAVOURITES, favouriteComics).apply();
    }

    private void saveImageForComic(final Comic comic) {
        new AsyncTask<Void, Void, File>() {
            @Override protected File doInBackground(Void... params) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                comic.getLocalCopy().compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File file = new File(context.getFilesDir(), Uri.parse(comic.getImg()).getLastPathSegment());
                try {
                    boolean success = file.createNewFile();
                    if (!success) throw new IOException("Could not create file");
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return file;
            }

            @Override protected void onPostExecute(File file) {
                comic.setLocalCopyPath(file.getPath());
            }
        }.execute();
    }

    public interface ComicCallback {
        void complete(@Nullable Comic comic);
    }

    public interface ComicListCallback {
        void complete(List<Comic> comics);
    }

}
