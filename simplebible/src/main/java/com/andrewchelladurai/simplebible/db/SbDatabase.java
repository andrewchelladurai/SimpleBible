package com.andrewchelladurai.simplebible.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 9:26 PM.
 */

@Database(entities = {Verse.class, Book.class, Bookmark.class},
          version = 20180116)
public abstract class SbDatabase
    extends RoomDatabase {

    public abstract VerseDao getVerseDao();

    public abstract BookDao getBookDao();

    public abstract BookmarkDao getBookmarkDao();

}
