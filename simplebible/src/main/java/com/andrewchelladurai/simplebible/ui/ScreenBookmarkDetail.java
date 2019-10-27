package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Bookmark;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.model.BookmarkDetailModel;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarkDetailAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ScreenBookmarkDetailOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.BookmarkUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ScreenBookmarkDetail
    extends Fragment
    implements ScreenBookmarkDetailOps {

  static final String ARG_VERSE_LIST = "ARG_VERSE_LIST";
  private static final String TAG = "ScreenBookmarkDetail";
  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private BookmarkDetailModel model;
  private BookmarkDetailAdapter adapter;
  private String itemBookmarkVerseContentTemplate;

  public ScreenBookmarkDetail() {
    // TODO: 17/8/19 Fix issues on config change, saw a NPE when editing a bookmark earlier
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement ScreenSimpleBibleOps");
    }
    mainOps = (ScreenSimpleBibleOps) context;
    mainOps.hideKeyboard();
    mainOps.hideNavigationView();

    model = ViewModelProviders.of(this).get(BookmarkDetailModel.class);
    adapter = new BookmarkDetailAdapter(this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_bookmark_fragment, container, false);

    // on first load, get all the passed verses, show an error if it's empty
    if (savedState == null) {

      // have we got arguments
      final Bundle arguments = getArguments();
      if (arguments == null) {
        mainOps.showErrorScreen(getString(R.string.scrBookmarkErrNullVerseList), true, true);
        return rootView;
      }

      // does the arguments contain the key we need
      if (!arguments.containsKey(ARG_VERSE_LIST)) {
        mainOps.showErrorScreen(getString(R.string.scrBookmarkErrEmptyVerseList), true, true);
        return rootView;
      }

      // does the passed value actually hold data for our use
      final Parcelable[] parcelableArray = arguments.getParcelableArray(ARG_VERSE_LIST);
      if (parcelableArray == null || parcelableArray.length == 0) {
        mainOps.showErrorScreen(getString(R.string.scrBookmarkErrEmptyVerseList), true, true);
        return rootView;
      }

      // if yes, convert it into a type we can use
      final ArrayList<Verse> list = new ArrayList<>();
      for (final Parcelable parcelable : parcelableArray) {
        list.add((Verse) parcelable);
      }

      model.cacheList(list);

    }

    rootView.findViewById(R.id.scrBookmarkActionDelete)
            .setOnClickListener(view -> handleClickActionDelete());
    rootView.findViewById(R.id.scrBookmarkActionEdit)
            .setOnClickListener(view -> handleClickActionEdit());
    rootView.findViewById(R.id.scrBookmarkActionShare)
            .setOnClickListener(view -> handleClickActionShare());
    rootView.findViewById(R.id.scrBookmarkActionSave)
            .setOnClickListener(view -> handleClickActionSave());
    itemBookmarkVerseContentTemplate = getString(R.string.itemBookmarkVerseContentTemplate);

    updateContent();

    return rootView;
  }

  private void handleClickActionEdit() {
    Log.d(TAG, "handleClickActionEdit() called");
    toggleAction(false);
    toggleNoteFieldState(false);
  }

  private void handleClickActionSave() {
    Log.d(TAG, "handleClickActionSave() called");
    final BookmarkUtils bookmarkUtils = BookmarkUtils.getInstance();

    final ArrayList<Verse> verseList = model.getCachedList();
    final String bookmarkReference = bookmarkUtils.createReference(verseList);
    final String noteText = getNoteText();

    model.saveBookmark(bookmarkReference, noteText).observe(this, saved -> {
      if (saved) {
        final String message = getString(R.string.scrBookmarkDetailSaveSuccess);
        mainOps.showMessage(message);

        toggleAction(true);
        toggleNoteFieldState(true);
        return;
      }

      final String message = getString(R.string.scrBookmarkDetailSaveFail);
      mainOps.showMessage(message);
    });
  }

  private void handleClickActionShare() {
    Log.d(TAG, "handleClickActionShare() called");
    final StringBuilder verseText = new StringBuilder();
    final String noteText = getNoteText();

    final RecyclerView recyclerView = rootView.findViewById(R.id.scrBookmarkListList);
    final int childCount = recyclerView.getChildCount();

    for (int i = 0; i < childCount; i++) {
      final TextView view = (TextView) recyclerView.getChildAt(i);
      verseText.append(view.getText()).append("\n");
    }

    final String shareTemplate = getString(R.string.scrBookmarkDetailShareTemplate);
    final String formattedShareText = String.format(shareTemplate, verseText, noteText);

    mainOps.shareText(formattedShareText);
  }

  private void handleClickActionDelete() {
    Log.d(TAG, "handleClickActionDelete() called");
    final BookmarkUtils bookmarkUtils = BookmarkUtils.getInstance();

    final ArrayList<Verse> verseList = model.getCachedList();
    final String bookmarkReference = bookmarkUtils.createReference(verseList);
    final String noteText = getNoteText();

    model.deleteBookmark(bookmarkReference, noteText).observe(this, deleted -> {
      if (deleted) {
        NavHostFragment.findNavController(this)
                       .navigate(R.id.action_screenBookmark_pop);
        final String message = getString(R.string.scrBookmarkDetailDeleteSuccess);
        mainOps.showMessage(message);
        return;
      }

      final String message = getString(R.string.scrBookmarkDetailDeleteFail);
      mainOps.showMessage(message);
    });
  }

  private void updateContent() {
    final BookmarkUtils bookmarkUtils = BookmarkUtils.getInstance();
    final ArrayList<Verse> list = model.getCachedList();
    final String reference = bookmarkUtils.createReference(list);

    adapter.updateList(list);

    final RecyclerView recyclerView = rootView.findViewById(R.id.scrBookmarkListList);
    recyclerView.setAdapter(adapter);

    model.getBookmark(reference).observe(this, bookmarks -> {
      final boolean bookmarkExists = (bookmarks != null && !bookmarks.isEmpty());
      toggleAction(bookmarkExists);
      toggleNoteFieldState(bookmarkExists);

      if (bookmarkExists) {
        final Bookmark bookmark = bookmarks.get(0);
        setNoteText(bookmark.getNote());
      } else {
        setNoteText("");
      }
    });

  }

  private void toggleNoteFieldState(final boolean bookmarkExists) {
    Log.d(TAG, "toggleNoteFieldState: bookmarkExists = [" + bookmarkExists + "]");
    rootView.findViewById(R.id.scrBookmarkNote).setEnabled(!bookmarkExists);
  }

  private void toggleAction(final boolean bookmarkExists) {
    Log.d(TAG, "toggleAction: bookmarkExists = [" + bookmarkExists + "]");
    rootView.findViewById(R.id.scrBookmarkActionDelete)
            .setVisibility((bookmarkExists) ? View.VISIBLE : View.GONE);
    rootView.findViewById(R.id.scrBookmarkActionEdit)
            .setVisibility((bookmarkExists) ? View.VISIBLE : View.GONE);
    rootView.findViewById(R.id.scrBookmarkActionShare)
            .setVisibility((bookmarkExists) ? View.VISIBLE : View.GONE);

    rootView.findViewById(R.id.scrBookmarkActionSave)
            .setVisibility((!bookmarkExists) ? View.VISIBLE : View.GONE);
  }

  private String getNoteText() {
    TextInputEditText editText = rootView.findViewById(R.id.scrBookmarkNote);
    final Editable text = editText.getText();

    return (text == null) ? "" : text.toString();
  }

  private void setNoteText(@NonNull final String note) {
    TextInputEditText editText = rootView.findViewById(R.id.scrBookmarkNote);
    editText.setText(note);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps.showNavigationView();
    mainOps = null;
  }

  @Override
  public void updateBookmarkVerseView(@NonNull final Verse verse,
                                      @NonNull final TextView textView) {
    model.getBook(verse.getBook()).observe(this, book -> {
      if (book == null) {
        Log.e(TAG, "updateSearchResultView: book not found for verse [" + verse + "]");
        return;
      }

      textView.setText(HtmlCompat.fromHtml(
          String.format(itemBookmarkVerseContentTemplate,
                        book.getName(),
                        verse.getChapter(),
                        verse.getVerse(),
                        verse.getText()),
          HtmlCompat.FROM_HTML_MODE_LEGACY));
    });

  }

}
