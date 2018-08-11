package com.andrewchelladurai.simplebible.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.andrewchelladurai.simplebible.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;

public class MainScreen
    extends Activity {

    private static final String TAG = "MainScreen";
    BottomAppBar         mAppbar;
    FrameLayout          mFragmentHolder;
    FloatingActionButton mFab;
    static HomeFragment mHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        mAppbar = findViewById(R.id.act_main_appbar);
        mAppbar.replaceMenu(R.menu.menu_main_screen);
        mAppbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return handleMenuItemClick(item);
            }
        });
        mFab = findViewById(R.id.act_main_appbar_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                handleBottomFabClick();
            }
        });
        mFragmentHolder = findViewById(R.id.act_main_fragment_holder);

        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.act_main_fragment_holder, mHomeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void handleBottomFabClick() {
        Log.d(TAG, "handleBottomFabClick: ");
    }

    private boolean handleMenuItemClick(final MenuItem item) {
        Log.d(TAG, "handleMenuItemClick: " + item.getTitle());
        return true;
    }

}
