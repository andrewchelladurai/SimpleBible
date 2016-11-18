/*
 *
 * This file 'SplashActivity.java' is part of SimpleBible :
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

import com.stephentuso.welcome.WelcomeScreenBuilder;
import com.stephentuso.welcome.ui.WelcomeActivity;
import com.stephentuso.welcome.util.WelcomeScreenConfiguration;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 18-Nov-2016 @ 8:41 PM
 */

public class SplashActivity
        extends WelcomeActivity {

    @Override protected WelcomeScreenConfiguration configuration() {
        return new WelcomeScreenBuilder(this)
                .theme(R.style.WelcomeScreenTheme_Light)
                .defaultBackgroundColor(R.color.windowBackground)
                .titlePage(R.drawable.splash_app_logo,
                           getString(R.string.application_name),
                           R.color.splash_title_page)
                .basicPage(R.drawable.splash_bookmark,
                           "Bookmarks + Notes",
                           "You can now Export them so easy sharing",
                           R.color.splash_bookmark_page)
                .titlePage(R.drawable.splash_thank_you,
                           "For using this application.",
                           R.color.splash_thank_you_page)
                .swipeToDismiss(true)
                .canSkip(false)
                .build();
    }
}
