package com.andrewchelladurai.simplebible.model;

import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.db.entities.EntityBookmark;

import java.util.ArrayList;
import java.util.List;

public class Bookmark {

  private static final String TAG = "Bookmark";

  @NonNull
  private final String reference;

  @NonNull
  private final String note;

  @NonNull
  private final ArrayList<Verse> verseList = new ArrayList<>(0);

  public Bookmark(@NonNull EntityBookmark entityBookmark,
                  @NonNull final ArrayList<Verse> verseList) {
    reference = entityBookmark.getReference();
    note = entityBookmark.getNote();

    this.verseList.clear();
    this.verseList.addAll(verseList);
  }

  @NonNull
  public String getReference() {
    return reference;
  }

  @NonNull
  public String getNote() {
    return note;
  }

  @NonNull
  public List<Verse> getVerseList() {
    return verseList.subList(0, verseList.size());
  }

}
