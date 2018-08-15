package com.andrewchelladurai.simplebible.data;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class VerseRepository
    extends AndroidViewModel
    implements RepositoryOps {

    private static final String                 TAG         = "VerseRepository";
    private static final ArrayList<Verse>       sVersesList = new ArrayList<>();
    private static final HashMap<String, Verse> sVersesMap  = new HashMap<>();
    private static       LiveData<List<Verse>>  sLiveData   = new MutableLiveData<>();
    private static VerseRepository THIS_INSTANCE;
    private static int currentBook    = 0;
    private static int currentChapter = 0;

    public VerseRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
    }

    public static VerseRepository getInstance() {
        return THIS_INSTANCE;
    }

    @Override
    public boolean populateCache(final List<?> list) {
        clearCache();
        Verse verse;
        for (final Object object : list) {
            verse = (Verse) object;
            sVersesList.add((Verse) object);
            sVersesMap.put(verse.getReference(), verse);
        }

        Log.d(TAG, "cached [" + getCacheSize() + "] records for [book="
                   + currentBook + "][chapter=" + currentChapter + "]");
        return true;
    }

    @Override
    public void clearCache() {
        sVersesList.clear();
        sVersesMap.clear();
        //  currentBook = currentChapter = 0;
    }

    @Override
    public boolean isCacheEmpty() {
        return sVersesList.isEmpty() & sVersesMap.isEmpty();
    }

    @Override
    public int getCacheSize() {
        return (sVersesMap.size() == sVersesList.size()) ? sVersesList.size() : -1;
    }

    @Override
    public Object getCachedRecordUsingKey(final Object key) {
        final String reference = (String) key;
        if (sVersesMap.containsKey(reference)) {
            return sVersesMap.get(reference);
        }
        return null;
    }

    @Override
    public Object getCachedRecordUsingValue(final Object value) {
        final String msg = "Do not look for verse using value";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public List<Verse> getCachedList() {
        return sVersesList;
    }

    @Override
    public LiveData<List<Verse>> queryDatabase() {
        if (currentBook == 0 || currentChapter == 0) {
            throw new UnsupportedOperationException("currentBook || currentChapter = 0");
        }
        sLiveData = SbDatabase.getInstance(getApplication()).getVerseDao()
                              .getChapter(currentBook, currentChapter);
        Log.d(TAG, "queried for new chapter : [currentBook = " + currentBook
                   + "][currentChapter = " + currentChapter + "]");
        return sLiveData;
    }

    @Override
    public LiveData<List<Verse>> queryDatabase(final Object... objects) {
        if (isCacheValid(objects)) {
            Log.d(TAG, "returning cached live data");
            return sLiveData;
        }

        currentBook = (int) objects[0];
        currentChapter = (int) objects[1];

        return queryDatabase();
    }

    @Override
    public boolean isCacheValid(final Object... objects) {
        if (isCacheEmpty()) {
            Log.d(TAG, "cache is empty");
            return false;
        }

        final int book = (int) objects[0];
        final int chapter = (int) objects[1];

        if (book == currentBook && chapter == currentChapter) {
            Log.d(TAG, "already cached [book=" + book + "][chapter=" + chapter + "]");
            return true;
        }
        Log.d(TAG, "invalid cache - book != currentBook || chapter != currentChapter");
        return false;
    }
}
