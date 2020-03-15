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

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Bookmark;
import com.andrewchelladurai.simplebible.data.entity.VerseEntity;
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
  private String contentTemplate;
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
    model = new BookmarkDetailModel(requireActivity().getApplication());
    adapter = new BookmarkDetailAdapter(this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_bookmark_detail, container, false);

    final String[] message = new String[1];

    // on first load, get all the passed verses, show an error if it's empty
    if (savedState == null) {

      // have we got arguments
      final Bundle arguments = getArguments();
      if (arguments == null) {
        message[0] = getString(R.string.screen_bookmark_detail_msg_no_args);
        mainOps.showErrorScreen(message[0], true, true);
        return rootView;
      }

      // does the arguments contain the key we need
      if (!arguments.containsKey(ARG_VERSE_LIST)) {
        message[0] = getString(R.string.screen_bookmark_detail_msg_no_verse);
        mainOps.showErrorScreen(message[0], true, true);
        return rootView;
      }

      // does the passed value actually hold data for our use
      final Parcelable[] parcelableArray = arguments.getParcelableArray(ARG_VERSE_LIST);
      if (parcelableArray == null || parcelableArray.length == 0) {
        message[0] = getString(R.string.screen_bookmark_detail_msg_no_verse);
        mainOps.showErrorScreen(message[0], true, true);
        return rootView;
      }

      // if yes, convert it into a type we can use
      final ArrayList<VerseEntity> list = new ArrayList<>();
      for (final Parcelable parcelable : parcelableArray) {
        list.add((VerseEntity) parcelable);
      }

      model.cacheList(list);

    }

    final BottomAppBar bAppBar = rootView.findViewById(R.id.screen_bookmark_detail_bottom_appbar);
    bAppBar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.screen_bookmark_detail_menu_delete:
          handleClickActionDelete();
          return true;
        case R.id.screen_bookmark_detail_menu_edit:
          handleClickActionEdit();
          return true;
        case R.id.screen_bookmark_detail_menu_share:
          handleClickActionShare();
          return true;
        case R.id.screen_bookmark_detail_menu_save:
          handleClickActionSave();
          return true;
        default:
          Log.e(TAG, "onMenuItemClick: Unknown Menu ["
                     + item.getItemId() + " - " + item.getTitle() + "]");
          return false;

      }
    });

    contentTemplate = getString(R.string.screen_bookmark_detail_list_item_content_template);

    updateContent();

    mainOps.hideKeyboard();
    mainOps.hideNavigationView();

    return rootView;
  }

  private void handleClickActionDelete() {
    Log.d(TAG, "handleClickActionDelete() called");

    final ArrayList<VerseEntity> verseList = model.getCachedList();
    final String bookmarkReference = BookmarkUtils.createReference(verseList);
    final String noteText = getNoteText();

    model.deleteBookmark(bookmarkReference, noteText)
         .observe(getViewLifecycleOwner(), deleted -> {
           if (deleted) {
             NavHostFragment.findNavController(this)
                            .navigate(R.id.screen_bookmark_detail_pop);
             final String message = getString(R.string.screen_bookmark_detail_msg_deleted);
             mainOps.showMessage(message);
             return;
           }

           final String message = getString(R.string.screen_bookmark_detail_msg_delete_fail);
           mainOps.showMessage(message);
         });
  }

  private void handleClickActionEdit() {
    Log.d(TAG, "handleClickActionEdit() called");
    toggleAction(false);
    toggleNoteFieldState(false);
  }

  private void handleClickActionShare() {
    Log.d(TAG, "handleClickActionShare() called");
    final StringBuilder verseText = new StringBuilder();
    final String noteText = getNoteText();

    final RecyclerView recyclerView = rootView.findViewById(R.id.screen_bookmark_detail_list);
    final int childCount = recyclerView.getChildCount();

    for (int i = 0; i < childCount; i++) {
      final TextView view = (TextView) recyclerView.getChildAt(i);
      verseText.append(view.getText())
               .append("\n");
    }

    final String shareTemplate = getString(R.string.screen_bookmark_detail_template_share);
    final String formattedShareText = String.format(shareTemplate, verseText, noteText);

    mainOps.shareText(formattedShareText);
  }

  private void handleClickActionSave() {
    Log.d(TAG, "handleClickActionSave() called");

    final ArrayList<VerseEntity> verseList = model.getCachedList();
    final String bookmarkReference = BookmarkUtils.createReference(verseList);
    final String noteText = getNoteText();

    model.saveBookmark(bookmarkReference, noteText)
         .observe(getViewLifecycleOwner(), saved -> {
           if (saved) {
             final String message = getString(R.string.screen_bookmark_detail_msg_saved);
             mainOps.showMessage(message);

             toggleAction(true);
             toggleNoteFieldState(true);
             return;
           }

           final String message = getString(R.string.screen_bookmark_detail_msg_save_fail);
           mainOps.showMessage(message);
         });
  }

  private void updateContent() {
    final ArrayList<VerseEntity> list = model.getCachedList();
    final String reference = BookmarkUtils.createReference(list);

    adapter.updateList(list);

    final RecyclerView recyclerView = rootView.findViewById(R.id.screen_bookmark_detail_list);
    recyclerView.setAdapter(adapter);

    model.getBookmark(reference)
         .observe(getViewLifecycleOwner(), bookmarks -> {
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
           final String titleTemplate = getString(R.string.screen_bookmark_detail_template_title);

           final String headerTxt = getString(bookmarkExists
                                              ? R.string.screen_bookmark_detail_title_saved
                                              : R.string.screen_bookmark_detail_title_unsaved);

           final String footerTemplate = getResources().getQuantityString(
               R.plurals.screen_bookmark_detail_title_template_verse_count, recordCount);
           final String footerTxt = String.format(footerTemplate, recordCount);

           final String titleTxt = String.format(titleTemplate, headerTxt, footerTxt);
           ((TextView) rootView.findViewById(R.id.screen_bookmark_detail_title))
               .setText(HtmlCompat.fromHtml(titleTxt, HtmlCompat.FROM_HTML_MODE_COMPACT));
         });
  }

  @NonNull
  private String getNoteText() {
    TextInputEditText editText = rootView.findViewById(R.id.screen_bookmark_detail_note);
    final Editable text = editText.getText();
    final String note = (text == null) ? "" : text.toString();

    return (note.equalsIgnoreCase(getString(
        R.string.screen_bookmark_detail_msg_note_empty)) ? "" : note);
  }

  private void toggleAction(final boolean bookmarkExists) {
    Log.d(TAG, "toggleAction: bookmarkExists = [" + bookmarkExists + "]");
    final BottomAppBar bAppBar = rootView.findViewById(R.id.screen_bookmark_detail_bottom_appbar);
    final Menu menu = bAppBar.getMenu();
    if (bookmarkExists) {
      menu.setGroupVisible(R.id.screen_bookmark_detail_menu_container_saved, true);
      menu.setGroupVisible(R.id.screen_bookmark_detail_menu_container_unsaved, false);
    } else {
      menu.setGroupVisible(R.id.screen_bookmark_detail_menu_container_saved, false);
      menu.setGroupVisible(R.id.screen_bookmark_detail_menu_container_unsaved, true);
    }
  }

  private void toggleNoteFieldState(final boolean bookmarkExists) {
    Log.d(TAG, "toggleNoteFieldState: bookmarkExists = [" + bookmarkExists + "]");
    rootView.findViewById(R.id.screen_bookmark_detail_note)
            .setEnabled(!bookmarkExists);
  }

  private void setNoteText(@NonNull final String note) {
    TextInputEditText editText = rootView.findViewById(R.id.screen_bookmark_detail_note);
    if (note.isEmpty()) {
      editText.setText(R.string.screen_bookmark_detail_msg_note_empty);
    } else {
      editText.setText(note);
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

  @Override
  public void updateBookmarkVerseView(@NonNull final VerseEntity verse,
                                      @NonNull final TextView textView) {
    model.getBook(verse.getBook()).observe(this, bookEn -> {
      if (bookEn == null) {
        Log.e(TAG, "updateSearchResultView: book not found for verse [" + verse + "]");
        return;
      }

      textView.setText(HtmlCompat.fromHtml(
          String.format(contentTemplate,
                        bookEn.getName(),
                        verse.getChapter(),
                        verse.getVerse(),
                        verse.getText()),
          HtmlCompat.FROM_HTML_MODE_LEGACY));
    });

  }

}
