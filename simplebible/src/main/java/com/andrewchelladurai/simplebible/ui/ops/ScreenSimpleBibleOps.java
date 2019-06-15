package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface ScreenSimpleBibleOps {

  void hideNavigationView();

  void showNavigationView();

  void shareText(@NonNull final String text);

  void showMessage(@NonNull final String message);

  void hideKeyboard();

  void showErrorScreen(@Nullable String message, boolean informDev, boolean exitApp);

}
