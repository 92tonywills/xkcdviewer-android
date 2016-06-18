package com.github.tonywills.xkcdviewer;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.github.tonywills.xkcdviewer.api.model.Comic;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity implements ViewComicFragment.ComicViewerListener {

    private BottomBar bottomBar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadBottomNavigation(savedInstanceState);
    }

    private void loadBottomNavigation(Bundle savedInstanceState) {
        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItems(R.menu.main_navigation);
        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.navigation_item_latest:
                        switchToFragment(ViewComicFragment.newInstance(ViewComicFragment.MODE_LATEST));
                        break;
                    case R.id.navigation_item_random:
                        switchToFragment(ViewComicFragment.newInstance(ViewComicFragment.MODE_RANDOM));
                        break;
                    case R.id.navigation_item_starred: break;
                }
            }

            @Override public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.navigation_item_latest:
                        switchToFragment(ViewComicFragment.newInstance(ViewComicFragment.MODE_LATEST));
                        break;
                    case R.id.navigation_item_random:
                        switchToFragment(ViewComicFragment.newInstance(ViewComicFragment.MODE_RANDOM));
                        break;
                    case R.id.navigation_item_starred: break;
                }
            }
        });

        bottomBar.getBar().setBackgroundColor(ContextCompat.getColor(this, R.color.light));
        bottomBar.setActiveTabColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    private void switchToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomBar.onSaveInstanceState(outState);
    }

    @Override public void comicLoaded(Comic comic) {
        setTitle(comic.getTitle());
    }

    @Override public void setComicFavourite(boolean favourite, Comic comic) {

    }
}
