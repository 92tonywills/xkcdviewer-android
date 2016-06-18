package com.github.tonywills.xkcdviewer;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.github.tonywills.xkcdviewer.api.model.Comic;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity implements OnMenuTabClickListener,
        ViewComicFragment.ComicViewerListener, FavouriteComicsFragment.FavouriteComicsFragmentListener {

    private BottomBar bottomBar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadBottomNavigation(savedInstanceState);
    }

    private void loadBottomNavigation(Bundle savedInstanceState) {
        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItems(R.menu.main_navigation);
        bottomBar.setOnMenuTabClickListener(this);
        bottomBar.getBar().setBackgroundColor(ContextCompat.getColor(this, R.color.light));
        bottomBar.setActiveTabColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    private void switchToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.comic_quick_in, R.anim.comic_quick_out,
                        R.anim.comic_in, R.anim.comic_out)
                .replace(R.id.main_container, fragment)
                .commit();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomBar.onSaveInstanceState(outState);
    }

    @Override public void onMenuTabSelected(@IdRes int menuItemId) {
        switch (menuItemId) {
            case R.id.navigation_item_latest:
                switchToFragment(ViewComicFragment.newInstance(ViewComicFragment.MODE_LATEST));
                break;
            case R.id.navigation_item_random:
                switchToFragment(ViewComicFragment.newInstance(ViewComicFragment.MODE_RANDOM));
                break;
            case R.id.navigation_item_starred:
                switchToFragment(FavouriteComicsFragment.newInstance());
                break;
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
            case R.id.navigation_item_starred:
                switchToFragment(FavouriteComicsFragment.newInstance());
                break;
        }

    }

    @Override public void setTitleFromComicViewer(String title) {
        setTitle(title);
    }

    @Override public void setTitleFromFavouritesFragment(String title) {
        setTitle(title);
    }

    @Override public void didSelectComic(Comic comic) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.comic_in, R.anim.favourite_list_out,
                        R.anim.favourte_list_in, R.anim.comic_out)
                .replace(R.id.main_container, ViewComicFragment.newInstance(comic))
                .addToBackStack("showComic")
                .commit();
    }
}
