package com.andrewchelladurai.simplebible.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.BookmarkDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.entities.Verse;

import static com.andrewchelladurai.simplebible.common.Utilities.DATABASE_NAME;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 9:26 PM.
 */

@Database(entities = {Verse.class, Book.class, Bookmark.class},
          // epoch time in seconds : date +%s
          version = 1520969806, // March 13, 2018 7:36:46 PM
          exportSchema = false)
public abstract class SbDatabase
    extends RoomDatabase {

    private static final String     TAG          = "SbDatabase";
    private static       SbDatabase thisInstance = null;

    public static SbDatabase getInstance(@NonNull final Context context) {
        synchronized (SbDatabase.class) {
            if (thisInstance == null) {
                thisInstance = Room.databaseBuilder(context.getApplicationContext(),
                                                    SbDatabase.class,
                                                    DATABASE_NAME)
                                   .fallbackToDestructiveMigration()
                                   .build();
                Log.d(TAG, "getInstance: Database created");
            }
        }
        return thisInstance;
    }

    public abstract VerseDao getVerseDao();

    public abstract BookDao getBookDao();

    public abstract BookmarkDao getBookmarkDao();

}
