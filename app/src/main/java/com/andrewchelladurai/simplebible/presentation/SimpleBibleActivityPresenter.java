package com.andrewchelladurai.simplebible.presentation;

import android.content.Context;
import android.util.Log;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.interaction.DBUtilityOperations;
import com.andrewchelladurai.simplebible.interaction.SimpleBibleActivityOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.utilities.DBUtility;
import com.andrewchelladurai.simplebible.utilities.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 05-Sep-2016 @ 4:54 PM
 */
public class SimpleBibleActivityPresenter {

    private static final String TAG = "SB_SBA_Presenter";
    private SimpleBibleActivityOperations mOperations;
    private DBUtilityOperations           dbUtility;

    /**
     * This must be called in the onCreate of the Activity and also before any of the views is
     * created. Reason being, this constructor will init the DB connection etc...
     */
    public SimpleBibleActivityPresenter(SimpleBibleActivityOperations aInterface) {
        mOperations = aInterface;
    }

    /**
     * Actual init work is done here.
     */
    public void init() {
        Log.d(TAG, "init: called");
        String array[] = mOperations.getBookNameChapterCountArray();
        boolean populated = BooksList.populateBooksList(array);
        if (populated) {
            Log.d(TAG, "init: book list populated");
        } else {
            Log.d(TAG, "init: book list NOT populated");
        }

        if (dbUtility == null) {
            Context context = mOperations.getThisApplicationContext();
            if (context == null) {
                String message = "mOperations.getThisApplicationContext() returned NULL";
                Log.wtf(TAG, "init: ", new NullPointerException(message));
                return;
            }
            dbUtility = DBUtility.getInstance(mOperations);
        } else {
            Log.d(TAG, "init: dbUtility is already initialized, since it is != null");
        }
    }

    public InputStreamReader getMainScript() {
        Log.d(TAG, "getMainScript() called");
        InputStreamReader reader = null;
        try {
            Context context = mOperations.getThisApplicationContext();
            reader = new InputStreamReader(context.getAssets().open("mainSteps.sql"));
        } catch (IOException ioe) {
            Log.wtf(TAG, "init: Error preparing for DB setup : mainSteps.sql ", ioe);
            ioe.printStackTrace();
        }
        return reader;
    }

    public InputStreamReader getUpgradeScript() {
        Log.d(TAG, "getUpgradeScript() called");
        InputStreamReader reader = null;
        try {
            Context context = mOperations.getThisApplicationContext();
            reader = new InputStreamReader(context.getAssets().open("upgradeSteps.sql"));
        } catch (IOException ioe) {
            Log.wtf(TAG, "init: Error preparing for DB setup : upgradeSteps.sql ", ioe);
            ioe.printStackTrace();
        }
        return reader;
    }

    public InputStreamReader getDowngradeScript() {
        Log.d(TAG, "getDowngradeScript() called");
        InputStreamReader reader = null;
        try {
            Context context = mOperations.getThisApplicationContext();
            reader = new InputStreamReader(context.getAssets().open("downgradeSteps.sql"));
        } catch (IOException ioe) {
            Log.wtf(TAG, "init: Error preparing for DB setup : downgradeSteps.sql ", ioe);
            ioe.printStackTrace();
        }
        return reader;
    }

    public boolean exportBookmarks() {
        Log.d(TAG, "exportBookmarks() called");

        DBUtilityOperations dbu = DBUtility.getInstance();
        ArrayList<String[]> items = dbu.getAllBookmarks();
        if (items.isEmpty()) {
            Log.d(TAG, "exportBookmarks: Empty Bookmarks List");
            return false;
        }
        String[] itemParts;
        String line, verseText, verseTemplate =
                mOperations.getResourceString(R.string.share_verse_template);
        String shareBookmarkTemplate =
                mOperations.getResourceString(R.string.share_bookmark_template);

        Calendar calendar = Calendar.getInstance();
        String fileName = mOperations.getResourceString(R.string.application_name) + " "
                          + "Bookmarks Export "
                          + calendar.get(Calendar.DAY_OF_MONTH) + "-"
                          + calendar.get(Calendar.MONTH) + "-"
                          + calendar.get(Calendar.YEAR) + " "
                          + calendar.get(Calendar.HOUR) + "-"
                          + calendar.get(Calendar.MINUTE) + "-"
                          + calendar.get(Calendar.SECOND) + ".TXT";
        fileName = fileName.replaceAll(" ", "_");

        File location = mOperations.getBookmarkFileLocation();
        if (null == location) {
            Log.e(TAG, "exportBookmarks: returned location was null");
            return false;
        }
        Log.d(TAG, "exportBookmarks: File : " + location.getPath());

        File file = new File(location, fileName);
        boolean created;
//        Log.d(TAG, "exportBookmarks: Directory Created : " + created);
        try {
            created = file.createNewFile();
            Log.d(TAG, "exportBookmarks: File creation result : " + created);
            if (!created) {
                Log.e(TAG, "exportBookmarks: " + fileName + " could not be created");
                return false;
            }
        } catch (IOException ex) {
            Log.e(TAG, "exportBookmarks: File Not Created", ex);
            ex.printStackTrace();
            return false;
        }

        Log.d(TAG, "exportBookmarks: " + file.getPath() + " created");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            final int count = items.size();
            for (int i = 0; i < count; i++) {
                itemParts = items.get(i);
                verseText = Utilities.getShareableTextForReferences(itemParts[0], verseTemplate);
                String note = itemParts[1];
                note = (note.isEmpty()) ? "Empty" : note;
                line = String.format(shareBookmarkTemplate, verseText, note);
                fos.write(line.getBytes());
                line = "\n##################\n";
                fos.write(line.getBytes());
            }
            fos.close();
            Log.d(TAG, "exportBookmarks: Bookmarks File Populated : " + location.getPath());
            return true;
        } catch (IOException ex) {
            Log.e(TAG, "exportBookmarks: Error creating Bookmarks File", ex);
            return false;
        }
    }
}
