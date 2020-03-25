package com.andrewchelladurai.simplebible.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sb_bookmarks")
class EntityBookmark {

  @PrimaryKey
  @ColumnInfo(name = "reference")
  @NonNull
  private final String reference;

  @ColumnInfo(name = "note")
  @NonNull
  private final String note;

  EntityBookmark(@NonNull final String reference, @NonNull final String note) {
    this.reference = reference;
    this.note = note;
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
