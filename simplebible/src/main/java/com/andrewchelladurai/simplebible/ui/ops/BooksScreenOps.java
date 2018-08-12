package com.andrewchelladurai.simplebible.ui.ops;

import android.content.Context;
import android.text.TextWatcher;
import android.view.View;

import com.andrewchelladurai.simplebible.data.entities.Book;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 11-Aug-2018 @ 7:16 PM.
 */
public interface BooksScreenOps
    extends Observer<List<Book>>, View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    void onListFragmentInteraction(Book book);

    @NonNull
    Context getSystemContext();

    @NonNull
    String getFormattedBookListHeader(@NonNull Book book);

    @NonNull
    String getFormattedBookListDetails(@NonNull Book book);
}
