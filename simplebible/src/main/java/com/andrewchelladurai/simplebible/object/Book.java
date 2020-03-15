package com.andrewchelladurai.simplebible.object;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewchelladurai.simplebible.data.entity.EntityBook;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.Objects;

public final class Book
    implements Comparable, Parcelable {

  public static final Creator<Book> CREATOR = new Creator<Book>() {
    @Override
    public Book createFromParcel(Parcel in) {
      return new Book(in);
    }

    @Override
    public Book[] newArray(int size) {
      return new Book[size];
    }
  };
  @NonNull
  private EntityBook bookEntity;

  public Book(@NonNull final EntityBook entityBook) {
    this.bookEntity = entityBook;
  }

  protected Book(Parcel in) {
    bookEntity = in.readParcelable(EntityBook.class.getClassLoader());
  }

  @NonNull
  public String getName() {
    return bookEntity.getName();
  }

  @IntRange(from = 1)
  public int getChapters() {
    return bookEntity.getChapters();
  }

  @NonNull
  public String getDescription() {
    return bookEntity.getDescription();
  }

  @IntRange(from = 1)
  public int getVerses() {
    return bookEntity.getVerses();
  }

  @Override
  public int compareTo(@NonNull final Object obj) {
    return this.getNumber() - ((Book) obj).getNumber();
  }

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  public int getNumber() {
    return bookEntity.getNumber();
  }

  @Override
  public int hashCode() {
    return Objects.hash(bookEntity);
  }

  @Override
  public boolean equals(@Nullable final Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    return bookEntity.equals(((Book) obj).bookEntity);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeParcelable(bookEntity, flags);
  }

}
