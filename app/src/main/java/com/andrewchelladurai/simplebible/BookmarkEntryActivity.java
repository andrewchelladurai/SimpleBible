/*
 *
 * This file 'BookmarkEntryActivity.java' is part of SimpleBible :
 *
 * Copyright (c) 2016.
 *
 * This Application is available at below locations
 * Binary : https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 * Source : https://github.com/andrewchelladurai/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * OR <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 */

package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class BookmarkEntryActivity
    extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_bookmark_toolbar);
        setSupportActionBar(toolbar);

        // Is the UP / Back in the ActionBar required?
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<String> items = new ArrayList<>(7);
        for (int i = 1; i <= 17; i++) {
            items.add("Entry " + i);
        }

        ListViewCompat listViewCompat = (ListViewCompat) findViewById(R.id.activity_bookmark_list);
        listViewCompat.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                                                     R.layout.content_activity_bookmark_list,
                                                     items));
    }

}
