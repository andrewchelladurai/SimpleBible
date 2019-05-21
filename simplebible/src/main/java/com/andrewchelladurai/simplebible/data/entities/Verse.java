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
        primaryKeys = {"version", "book, chapter, verse"})
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
  private final String version;

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  @ColumnInfo(name = "book")
  private final int bookNumber;

  @IntRange(from = 1)
  @ColumnInfo(name = "chapter")
  private final int chapterNumber;

  @IntRange(from = 1)
  @ColumnInfo(name = "verse")
  private final int verseNumber;

  @NonNull
  @ColumnInfo(name = "text")
  private final String text;

  private Verse(@NonNull String version,
                @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) int bookNumber,
                @IntRange(from = 1) int chapterNumber,
                @IntRange(from = 1) int verseNumber,
                @NonNull String text) {
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

  @NonNull
  private String getVersion() {
    return version;
  }

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  private int getBookNumber() {
    return bookNumber;
  }

  @IntRange(from = 1)
  private int getChapterNumber() {
    return chapterNumber;
  }

  @IntRange(from = 1)
  private int getVerseNumber() {
    return verseNumber;
  }

  @NonNull
  private String getText() {
    return text;
  }

  @NonNull
  public String getReference() {
    return VerseUtils.createReference(bookNumber, chapterNumber, verseNumber);
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
    return bookNumber == verse.bookNumber
           && chapterNumber == verse.chapterNumber
           && verseNumber == verse.verseNumber
           && version.equals(verse.version)
           && text.equals(verse.text);
  }

  @NonNull
  @Override
  public String toString() {
    return getReference();
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
