package com.andrewchelladurai.simplebible.object;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewchelladurai.simplebible.data.entity.EntityVerse;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.Objects;

public final class Verse
    implements Parcelable {

  public static final Creator<Verse> CREATOR = new Creator<Verse>() {
    @Override
    public Verse createFromParcel(Parcel in) {
      return new Verse(in);
    }

    @Override
    public Verse[] newArray(int size) {
      return new Verse[size];
    }
  };
  private static final String TAG = "Verse";
  @NonNull
  private final EntityVerse entityVerse;
  @Nullable
  private Book book;

  public Verse(@NonNull final EntityVerse entityVerse) {
    this.entityVerse = entityVerse;
  }

  protected Verse(Parcel in) {
    entityVerse = in.readParcelable(EntityVerse.class.getClassLoader());
    book = in.readParcelable(Book.class.getClassLoader());
  }

  public void addBook(final Book book) {
    this.book = book;
  }

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  public int getBookNumber() {
    return entityVerse.getBook();
  }

  @IntRange(from = 1)
  public int getChapterNumber() {
    return entityVerse.getChapter();
  }

  @IntRange(from = 1)
  public int getVerseNumber() {
    return entityVerse.getVerse();
  }

  @NonNull
  public String getText() {
    return entityVerse.getText();
  }

  @NonNull
  public String getBookName() {
    if (book != null) {
      return book.getName();
    } else {
      Log.e(TAG, "getBookName: book not initialized, returning blank string");
      return "";
    }
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(entityVerse);
    result = 31 * result + Objects.hashCode(book);
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    final Verse verse = (Verse) obj;
    return entityVerse.equals(verse.entityVerse)
           && book != null
           && book.equals(verse.book);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeParcelable(entityVerse, flags);
    dest.writeParcelable(book, flags);
  }

}
