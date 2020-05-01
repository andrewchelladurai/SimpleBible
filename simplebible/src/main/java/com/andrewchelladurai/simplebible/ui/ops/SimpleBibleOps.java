package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface SimpleBibleOps {

  void hideKeyboard();

  void showNavigationView();

  void hideNavigationView();

  void showErrorScreen(@Nullable String message, boolean shareLogs, boolean exitApp);

  void applyThemeSelectedInPreference();

  void restartApp();

  void shareText(@NonNull String text);

  void showMessage(@NonNull String string, @IdRes int anchorViewId);

  void updateReminderTime(boolean enableReminder,
                          @IntRange(from = 0, to = 23) final int hour,
                          @IntRange(from = 0, to = 59) final int min);

}
