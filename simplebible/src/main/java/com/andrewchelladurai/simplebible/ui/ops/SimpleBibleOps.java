package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.Nullable;

public interface SimpleBibleOps {

  void hideKeyboard();

  void showNavigationView();

  void hideNavigationView();

  void showErrorScreen(@Nullable String message, boolean shareLogs, boolean exitApp);

  void applyThemeSelectedInPreference();

  void restartApp();

}
