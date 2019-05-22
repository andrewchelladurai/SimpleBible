package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface SimpleBibleScreenOps {

  void showNavigationComponent();

  void hideNavigationComponent();

  void showErrorScreen(@Nullable final String message, final boolean emailDev);

  void showErrorMessage(@NonNull final String message);

  void shareText(@NonNull final String text);

  void hideKeyboard();

}
