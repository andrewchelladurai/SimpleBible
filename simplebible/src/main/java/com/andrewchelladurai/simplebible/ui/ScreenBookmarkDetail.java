package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.bottomappbar.BottomAppBar;
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
  private boolean showVerseList = true;

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
    model = ViewModelProviders.of(this).get(BookmarkDetailModel.class);
    adapter = new BookmarkDetailAdapter(this);
    Toast
        .makeText(context, getString(R.string.scr_bmark_menu_toggle_view_hint), Toast.LENGTH_LONG)
        .show();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_bookmark_detail, container, false);

    // on first load, get all the passed verses, show an error if it's empty
    if (savedState == null) {

      // have we got arguments
      final Bundle arguments = getArguments();
      if (arguments == null) {
        mainOps.showErrorScreen(getString(R.string.scr_bmark_msg_no_args), true, true);
        return rootView;
      }

      // does the arguments contain the key we need
      if (!arguments.containsKey(ARG_VERSE_LIST)) {
        mainOps.showErrorScreen(getString(R.string.scr_bmark_msg_no_verse), true, true);
        return rootView;
      }

      // does the passed value actually hold data for our use
      final Parcelable[] parcelableArray = arguments.getParcelableArray(ARG_VERSE_LIST);
      if (parcelableArray == null || parcelableArray.length == 0) {
        mainOps.showErrorScreen(getString(R.string.scr_bmark_msg_no_verse), true, true);
        return rootView;
      }

      // if yes, convert it into a type we can use
      final ArrayList<Verse> list = new ArrayList<>();
      for (final Parcelable parcelable : parcelableArray) {
        list.add((Verse) parcelable);
      }

      model.cacheList(list);

    }

    final BottomAppBar bAppBar = rootView.findViewById(R.id.scr_bmark_menu);
    bAppBar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.scr_bmark_menu_toggle_view:
          showVerseList = !showVerseList;
          rootView.findViewById(R.id.scr_bmark_list)
                  .setVisibility((showVerseList ? View.VISIBLE : View.GONE));
          rootView.findViewById(R.id.scr_bmark_container_note)
                  .setVisibility((showVerseList ? View.GONE : View.VISIBLE));
          return true;
        case R.id.scr_bmark_menu_delete:
          handleClickActionDelete();
          return true;
        case R.id.scr_bmark_menu_edit:
          handleClickActionEdit();
          return true;
        case R.id.scr_bmark_menu_share:
          handleClickActionShare();
          return true;
        case R.id.scr_bmark_menu_save:
          handleClickActionSave();
          return true;
        default:
          Log.e(TAG, "onMenuItemClick: Unknown Menu ["
                     + item.getItemId() + " - " + item.getTitle() + "]");
          return false;

      }
    });

    itemBookmarkVerseContentTemplate = getString(R.string.itm_bmark_verse_content_template);

    updateContent();

    mainOps.hideKeyboard();
    mainOps.hideNavigationView();

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
        final String message = getString(R.string.scr_bmark_msg_save_success);
        mainOps.showMessage(message);

        toggleAction(true);
        toggleNoteFieldState(true);
        return;
      }

      final String message = getString(R.string.scr_bmark_msg_save_fail);
      mainOps.showMessage(message);
    });
  }

  private void handleClickActionShare() {
    Log.d(TAG, "handleClickActionShare() called");
    final StringBuilder verseText = new StringBuilder();
    final String noteText = getNoteText();

    final RecyclerView recyclerView = rootView.findViewById(R.id.scr_bmark_list);
    final int childCount = recyclerView.getChildCount();

    for (int i = 0; i < childCount; i++) {
      final TextView view = (TextView) recyclerView.getChildAt(i);
      verseText.append(view.getText()).append("\n");
    }

    final String shareTemplate = getString(R.string.itm_bmark_template_share);
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
        final String message = getString(R.string.scr_bmark_msg_delete_success);
        mainOps.showMessage(message);
        return;
      }

      final String message = getString(R.string.scr_bmark_msg_delete_fail);
      mainOps.showMessage(message);
    });
  }

  private void updateContent() {
    final BookmarkUtils bookmarkUtils = BookmarkUtils.getInstance();
    final ArrayList<Verse> list = model.getCachedList();
    final String reference = bookmarkUtils.createReference(list);

    adapter.updateList(list);

    final RecyclerView recyclerView = rootView.findViewById(R.id.scr_bmark_list);
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

      final int recordCount = list.size();
      final String titleTemplate = getString(R.string.scr_bmark_title_template);

      final String headerTxt = getString(bookmarkExists
                                         ? R.string.scr_bmark_title_template_saved
                                         : R.string.scr_bmark_title_template_unsaved);

      final String footerTemplate = getResources().getQuantityString(
          R.plurals.scr_bmark_title_template_verse_count, recordCount);
      final String footerTxt = String.format(footerTemplate, recordCount);

      final String titleTxt = String.format(titleTemplate, headerTxt, footerTxt);
      ((TextView) rootView.findViewById(R.id.scr_bmark_title))
          .setText(HtmlCompat.fromHtml(titleTxt, HtmlCompat.FROM_HTML_MODE_COMPACT));
    });
  }

  private void toggleNoteFieldState(final boolean bookmarkExists) {
    Log.d(TAG, "toggleNoteFieldState: bookmarkExists = [" + bookmarkExists + "]");
    rootView.findViewById(R.id.scr_bmark_note).setEnabled(!bookmarkExists);
  }

  private void toggleAction(final boolean bookmarkExists) {
    Log.d(TAG, "toggleAction: bookmarkExists = [" + bookmarkExists + "]");
    final BottomAppBar bAppBar = rootView.findViewById(R.id.scr_bmark_menu);
    final Menu menu = bAppBar.getMenu();
    if (bookmarkExists) {
      menu.setGroupVisible(R.id.scr_bmark_menu_container_saved, true);
      menu.setGroupVisible(R.id.scr_bmark_menu_container_unsaved, false);
    } else {
      menu.setGroupVisible(R.id.scr_bmark_menu_container_saved, false);
      menu.setGroupVisible(R.id.scr_bmark_menu_container_unsaved, true);
    }
  }

  @NonNull
  private String getNoteText() {
    TextInputEditText editText = rootView.findViewById(R.id.scr_bmark_note);
    final Editable text = editText.getText();

    return (text == null) ? "" : text.toString();
  }

  private void setNoteText(@NonNull final String note) {
    TextInputEditText editText = rootView.findViewById(R.id.scr_bmark_note);
    editText.setText(note);
  }

  @Override
  public void onDetach() {
    super.onDetach();
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
