package com.andrewchelladurai.simplebible.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.BookmarkDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.data.entity.Bookmark;
import com.andrewchelladurai.simplebible.data.entity.Verse;

@Database(entities = {Book.class, Verse.class, Bookmark.class},
          // epoch time in seconds : date +%s
          version = 1520969806,
          // March 13, 2018 7:36:46 PM
          exportSchema = false)
public abstract class SbDatabase
  extends RoomDatabase {

  // TODO: implement a migration strategy for both an upgrade and a downgrade
  // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929

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

  public abstract BookDao getBookDao();

  public abstract VerseDao getVerseDao();

  public abstract BookmarkDao getBookmarkDao();

}
