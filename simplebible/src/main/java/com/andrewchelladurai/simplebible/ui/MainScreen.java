package com.andrewchelladurai.simplebible.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.andrewchelladurai.simplebible.R;
import com.google.android.material.bottomappbar.BottomAppBar;

import androidx.appcompat.widget.Toolbar;

public class MainScreen
    extends Activity {

    BottomAppBar mAppbar;

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
    }

    private boolean handleMenuItemClick(final MenuItem item) {
        return true;
    }

}
