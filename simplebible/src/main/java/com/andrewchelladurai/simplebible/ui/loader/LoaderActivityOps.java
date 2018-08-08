package com.andrewchelladurai.simplebible.ui.loader;

import com.andrewchelladurai.simplebible.common.BaseScreenOps;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 05-Aug-2018 @ 8:38 PM.
 */
interface LoaderActivityOps
    extends BaseScreenOps {

    void showLoadingScreen();

    void showMainScreen();

    void showFailedScreen();
}
