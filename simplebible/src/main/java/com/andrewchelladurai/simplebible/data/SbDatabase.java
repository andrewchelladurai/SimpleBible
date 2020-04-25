package com.andrewchelladurai.simplebible.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {EntityBook.class, EntityVerse.class, EntityBookmark.class},
          // epoch time in seconds : date +%s
          version = 1520969806,
          // March 13, 2018 7:36:46 PM
          exportSchema = false)
public abstract class SbDatabase
    extends RoomDatabase {

  private static final String TAG = "SbDatabase";

  private static final String DATABASE_NAME = "SimpleBible.db";

  private static SbDatabase THIS_INSTANCE;

  public static SbDatabase getDatabase(final Context context) {
    if (THIS_INSTANCE == null) {
      synchronized (SbDatabase.class) {
        if (THIS_INSTANCE == null) {
          THIS_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                               SbDatabase.class,
                                               DATABASE_NAME)
                              .fallbackToDestructiveMigration()
                              .build();
          Log.d(TAG, "getInstance: Database created");
        }
      }
    }
    return THIS_INSTANCE;
  }

  public abstract SbDao getDao();

  public abstract BookDao getDaoBook();

  public abstract VerseDao getDaoVerse();

  public abstract BookmarkDao getDaoBookmark();

}
