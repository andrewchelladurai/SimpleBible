package com.andrewchelladurai.simplebible.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sb_bookmarks")
public class EntityBookmark {

  // TODO: 22/4/20 save created date and modified date too

  @PrimaryKey
  @ColumnInfo(name = "reference")
  @NonNull
  private final String reference;

  @ColumnInfo(name = "note")
  @NonNull
  private final String note;

  public EntityBookmark(@NonNull final String reference, @NonNull final String note) {
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
