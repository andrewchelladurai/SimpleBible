package com.andrewchelladurai.simplebible.ui.ops;

public interface SimpleBibleOps {

  void hideKeyboard();

  void showNavigationView();

  void hideNavigationView();

  void showErrorScreen(String message, boolean shareLogs, boolean exitApp);

  void applyThemeSelectedInPreference();

  void restartApp();

}
