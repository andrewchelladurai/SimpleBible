package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.andrewchelladurai.simplebible.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class MainScreen
    extends AppCompatActivity {

    private static final String TAG = "MainScreen";
    BottomAppBar         mAppbar;
    FrameLayout          mFragmentHolder;
    FloatingActionButton mFab;
    static String       sCurrentFragment = "";
    static BookFragment sBookFragment    = BookFragment.newInstance();

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

        mFragmentHolder = findViewById(R.id.act_main_container_fragment);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            transaction.replace(R.id.act_main_container_fragment, sBookFragment);
            sCurrentFragment = sBookFragment.getClass().getName();
        } else {
            if (sCurrentFragment.equalsIgnoreCase(sBookFragment.getClass().getName())) {
                transaction.replace(R.id.act_main_container_fragment, sBookFragment);
                sCurrentFragment = sBookFragment.getClass().getName();
            }
        }
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
