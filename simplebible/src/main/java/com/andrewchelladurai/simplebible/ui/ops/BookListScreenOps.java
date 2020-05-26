package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.Nullable;

import com.andrewchelladurai.simplebible.model.Book;

public interface BookListScreenOps {

  void handleBookSelection(@Nullable Book book);

}
