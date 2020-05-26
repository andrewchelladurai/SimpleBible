package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.Nullable;

import com.andrewchelladurai.simplebible.data.entities.EntityBook;

public interface BookListScreenOps {

  void handleBookSelection(@Nullable EntityBook book);

}
