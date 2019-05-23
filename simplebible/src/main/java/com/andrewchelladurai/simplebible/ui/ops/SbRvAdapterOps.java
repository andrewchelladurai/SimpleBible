package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.NonNull;

import java.util.List;

public interface SbRvAdapterOps {

  void refreshList(@NonNull List<?> newList);

  @NonNull
  List<?> getList();

}
