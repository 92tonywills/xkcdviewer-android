package com.github.tonywills.xkcdviewer;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity {

    private BottomBar bottomBar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadBottomNavigation(savedInstanceState);
    }

    private void loadBottomNavigation(Bundle savedInstanceState) {
        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setMaxFixedTabs(2);
        bottomBar.setItems(R.menu.main_navigation);
        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.navigation_item_random: break;
                    case R.id.navigation_item_starred: break;
                    case R.id.navigation_item_all: break;
                }
            }

            @Override public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.navigation_item_random: break;
                    case R.id.navigation_item_starred: break;
                    case R.id.navigation_item_all: break;
                }
            }
        });

        bottomBar.setDefaultTabPosition(1);
        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        bottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.colorAccent_2));
        bottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.colorAccent_3));
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomBar.onSaveInstanceState(outState);
    }
}
