package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.NonNull;

import java.util.List;

public interface RecyclerViewAdapterOps {

  void updateList(@NonNull List<?> list);

  void filterList(@NonNull String searchTerm);

  void clearList();

  interface ItemHolderOps {

    void updateView(Object object, int position);

  }

}
