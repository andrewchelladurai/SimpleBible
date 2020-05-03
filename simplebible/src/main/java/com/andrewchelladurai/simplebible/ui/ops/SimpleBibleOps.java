package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface SimpleBibleOps {

  void hideKeyboard();

  void showNavigationView();

  void hideNavigationView();

  void showErrorScreen(@Nullable String message, boolean shareLogs, boolean exitApp);

  void updateApplicationTheme();

  void restartApp();

  void shareText(@NonNull String text);

  void showMessage(@NonNull String string, @IdRes int anchorViewId);

  void updateDailyVerseReminderState();

  void updateDailyVerseReminderTime();

}
