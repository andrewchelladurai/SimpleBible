package com.andrewchelladurai.simplebible.data.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;

import java.util.Objects;

@Entity(tableName = "sb_verses",
        primaryKeys = {"version", "book", "chapter", "verse"})
public class Verse
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

  @NonNull
  @ColumnInfo(name = "version")
  private String version;

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  @ColumnInfo(name = "book")
  private int bookNumber;

  @IntRange(from = 1)
  @ColumnInfo(name = "chapter")
  private int chapterNumber;

  @IntRange(from = 1)
  @ColumnInfo(name = "verse")
  private int verseNumber;

  @NonNull
  @ColumnInfo(name = "text")
  private String text;

  public Verse(@NonNull final String version,
               @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int bookNumber,
               @IntRange(from = 1) final int chapterNumber,
               @IntRange(from = 1) final int verseNumber,
               @NonNull final String text) {
    this.version = version;
    this.bookNumber = bookNumber;
    this.chapterNumber = chapterNumber;
    this.verseNumber = verseNumber;
    this.text = text;
  }

  protected Verse(Parcel in) {
    version = in.readString();
    bookNumber = in.readInt();
    chapterNumber = in.readInt();
    verseNumber = in.readInt();
    text = in.readString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, bookNumber, chapterNumber, verseNumber, text);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Verse verse = (Verse) o;
    return bookNumber == verse.bookNumber &&
           chapterNumber == verse.chapterNumber &&
           verseNumber == verse.verseNumber &&
           version.equals(verse.version) &&
           text.equals(verse.text);
  }

  @NonNull
  @Override
  public String toString() {
    return VerseUtils.createReference(bookNumber, chapterNumber, verseNumber);
  }

  @NonNull
  public String getVersion() {
    return version;
  }

  public void setVersion(@NonNull final String version) {
    this.version = version;
  }

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  public int getBookNumber() {
    return bookNumber;
  }

  public void setBookNumber(
      @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int bookNumber) {
    this.bookNumber = bookNumber;
  }

  @IntRange(from = 1)
  public int getChapterNumber() {
    return chapterNumber;
  }

  public void setChapterNumber(@IntRange(from = 1) final int chapterNumber) {
    this.chapterNumber = chapterNumber;
  }

  @IntRange(from = 1)
  public int getVerseNumber() {
    return verseNumber;
  }

  public void setVerseNumber(@IntRange(from = 1) final int verseNumber) {
    this.verseNumber = verseNumber;
  }

  @NonNull
  public String getText() {
    return text;
  }

  public void setText(@NonNull final String text) {
    this.text = text;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(version);
    dest.writeInt(bookNumber);
    dest.writeInt(chapterNumber);
    dest.writeInt(verseNumber);
    dest.writeString(text);
  }

}
