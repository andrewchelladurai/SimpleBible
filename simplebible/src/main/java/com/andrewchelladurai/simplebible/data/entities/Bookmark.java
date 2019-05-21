package com.andrewchelladurai.simplebible.data.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.Objects;

@Entity(tableName = "sb_bookmarks",
        primaryKeys = {"references", "note"})
public class Bookmark
    implements Parcelable {

  public static final Creator<Bookmark> CREATOR = new Creator<Bookmark>() {
    @Override
    public Bookmark createFromParcel(Parcel in) {
      return new Bookmark(in);
    }

    @Override
    public Bookmark[] newArray(int size) {
      return new Bookmark[size];
    }
  };

  @NonNull
  @ColumnInfo(name = "references")
  private final String references;

  @NonNull
  @ColumnInfo(name = "note")
  private final String note;

  public Bookmark(@NonNull final String references, @NonNull final String note) {
    this.references = references;
    this.note = note;
  }

  protected Bookmark(Parcel in) {
    references = in.readString();
    note = in.readString();
  }

  @NonNull
  private String getReferences() {
    return references;
  }

  @NonNull
  private String getNote() {
    return note;
  }

  @Override
  public int hashCode() {
    return Objects.hash(references, note);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Bookmark bookmark = (Bookmark) o;
    return references.equals(bookmark.references) &&
           note.equals(bookmark.note);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(references);
    dest.writeString(note);
  }

}
