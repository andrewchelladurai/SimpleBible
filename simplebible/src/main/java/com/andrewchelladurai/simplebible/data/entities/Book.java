package com.andrewchelladurai.simplebible.data.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.Objects;

@Entity(tableName = "sb_books",
        primaryKeys = {"testament", "number", "name", "chapters", "verses"})
public class Book
    implements Parcelable {

  // TODO: 1/6/19 implement a sorting method - Comparator / Iterator

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
  @ColumnInfo(name = "testament")
  private String testament;

  @NonNull
  @ColumnInfo(name = "description")
  private String description;

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  @ColumnInfo(name = "number")
  private int number;

  @NonNull
  @ColumnInfo(name = "name")
  private String name;

  @IntRange(from = 1)
  @ColumnInfo(name = "chapters")
  private int chapters;

  @IntRange(from = 1)
  @ColumnInfo(name = "verses")
  private int verses;

  public Book(@NonNull final String testament,
              @NonNull final String description,
              @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int number,
              @NonNull final String name,
              @IntRange(from = 1) final int chapters,
              @IntRange(from = 1) final int verses) {
    this.testament = testament;
    this.description = description;
    this.number = number;
    this.name = name;
    this.chapters = chapters;
    this.verses = verses;
  }

  protected Book(Parcel in) {
    testament = in.readString();
    description = in.readString();
    number = in.readInt();
    name = in.readString();
    chapters = in.readInt();
    verses = in.readInt();
  }

  @Override
  public int hashCode() {
    return Objects.hash(testament, description, number, name, chapters, verses);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Book book = (Book) o;
    return number == book.number
           && chapters == book.chapters
           && verses == book.verses
           && testament.equals(book.testament)
           && description.equals(book.description)
           && name.equals(book.name);
  }

  @NonNull
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[" + number + ":" + name + "]";
  }

  @NonNull
  public String getTestament() {
    return testament;
  }

  public void setTestament(@NonNull final String testament) {
    this.testament = testament;
  }

  @NonNull
  public String getDescription() {
    return description;
  }

  public void setDescription(@NonNull final String description) {
    this.description = description;
  }

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  public int getNumber() {
    return number;
  }

  public void setNumber(@IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int number) {
    this.number = number;
  }

  @NonNull
  public String getName() {
    return name;
  }

  public void setName(@NonNull final String name) {
    this.name = name;
  }

  @IntRange(from = 1)
  public int getChapters() {
    return chapters;
  }

  public void setChapters(@IntRange(from = 1) final int chapters) {
    this.chapters = chapters;
  }

  @IntRange(from = 1)
  public int getVerses() {
    return verses;
  }

  public void setVerses(@IntRange(from = 1) final int verses) {
    this.verses = verses;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(testament);
    dest.writeString(description);
    dest.writeInt(number);
    dest.writeString(name);
    dest.writeInt(chapters);
    dest.writeInt(verses);
  }

}
