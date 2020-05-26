package com.andrewchelladurai.simplebible.model;

import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.db.entities.EntityBookmark;

public class Bookmark {

  private static final String TAG = "Bookmark";

  @NonNull
  private final String reference;

  @NonNull
  private final String note;

  public Bookmark(@NonNull EntityBookmark entityBookmark) {
    reference = entityBookmark.getReference();
    note = entityBookmark.getNote();
  }

  @NonNull
  public String getReference() {
    return reference;
  }

  @NonNull
  public String getNote() {
    return note;
  }

}
