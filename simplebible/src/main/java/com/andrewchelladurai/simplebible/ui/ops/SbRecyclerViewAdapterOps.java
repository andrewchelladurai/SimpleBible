package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.NonNull;
import java.util.List;

public interface SbRecyclerViewAdapterOps {

  void updateList(@NonNull List<?> list);

  void filterList(@NonNull String searchTerm);

}
