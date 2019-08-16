package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Bookmark;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.model.ScreenBookListModel;
import com.andrewchelladurai.simplebible.model.ScreenBookmarkListModel;
import com.andrewchelladurai.simplebible.ui.adapter.ScreenBookmarkListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ScreenBookmarkListOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;

public class ScreenBookmarkList
    extends Fragment
    implements ScreenBookmarkListOps {

  private static final String TAG = "ScreenBookmarkList";

  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private ScreenBookmarkListModel bookmarkListModel;
  private ScreenBookmarkListAdapter adapter;
  private ScreenBookListModel bookModel;

  public ScreenBookmarkList() {
    // TODO: 17/8/19 Use a better view component to show multi-line notes
    // TODO: 17/8/19 Clicking on a entry must load the ScreenBookmarkDetail
    // TODO: 17/8/19 implement the action buttons
    // TODO: 17/8/19 Beautify the Help Info display
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement InteractionListener");
    }
    mainOps = (ScreenSimpleBibleOps) context;
    mainOps.hideKeyboard();

    final String templateEmptyNoteContent = getString(R.string.scrBookmarkListTemplateEmptyNote);

    adapter = new ScreenBookmarkListAdapter(this, templateEmptyNoteContent);
    bookmarkListModel = ViewModelProviders.of(this).get(ScreenBookmarkListModel.class);
    bookModel = ViewModelProviders.of(this).get(ScreenBookListModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_bookmark_list_fragment, container, false);

    bookmarkListModel.getBookmarkList().observe(this, bookmarkList -> {
      if (bookmarkList == null || bookmarkList.isEmpty()) {
        hideList();
        showHelpInfo();
      } else {
        hideHelpInfo();
        adapter.clearList();
        adapter.updateList(bookmarkList);
        showList();
      }
    });

    return rootView;
  }

  private void showList() {
    final RecyclerView recyclerView = rootView.findViewById(R.id.scrBookmarkListList);
    recyclerView.setAdapter(adapter);
  }

  private void hideList() {
    rootView.findViewById(R.id.scrBookmarkListList).setVisibility(View.GONE);
  }

  private void showHelpInfo() {
    final String rawText = getString(R.string.scrBookmarkListInfoHelpText);
    final Spanned htmlText = HtmlCompat.fromHtml(rawText, HtmlCompat.FROM_HTML_MODE_COMPACT);

    final TextView textView = rootView.findViewById(R.id.scrBookmarkListInfoHelpText);
    textView.setText(htmlText);

    textView.setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scrBookmarkListInfoScrollView).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scrBookmarkListInfoImage).setVisibility(View.VISIBLE);
  }

  private void hideHelpInfo() {
    rootView.findViewById(R.id.scrBookmarkListInfoImage).setVisibility(View.GONE);
    rootView.findViewById(R.id.scrBookmarkListInfoHelpText).setVisibility(View.GONE);
    rootView.findViewById(R.id.scrBookmarkListInfoScrollView).setVisibility(View.GONE);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

  @Override
  public void handleActionClickDelete(@NonNull final Bookmark bookmark) {
    Log.d(TAG, "handleActionClickDelete: bookmark = [" + bookmark + "]");
    throw new UnsupportedOperationException();
  }

  @Override
  public void handleActionClickEdit(@NonNull final Bookmark bookmark) {
    Log.d(TAG, "handleActionClickEdit: bookmark = [" + bookmark + "]");
    throw new UnsupportedOperationException();
  }

  @Override
  public void handleActionClickShare(@NonNull final Bookmark bookmark) {
    Log.d(TAG, "handleActionClickShare: bookmark = [" + bookmark + "]");
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateVerseList(@NonNull final TextView verseField,
                              @NonNull final String bookmarkReference) {
    bookmarkListModel.getBookmarkedVerse(bookmarkReference).observe(this, verseList -> {
      if (verseList == null || verseList.isEmpty()) {
        final String template = getResources().getQuantityString(
            R.plurals.scrBookmarkListTemplateVerse, 0);
        verseField.setText(HtmlCompat.fromHtml(template, HtmlCompat.FROM_HTML_MODE_COMPACT));
        return;
      }

      final Verse verse = verseList.get(0);

      bookModel.getBookUsingNumber(verse.getBook()).observe(this, book -> {
        if (book == null) {
          final String template = getResources().getQuantityString(
              R.plurals.scrBookmarkListTemplateVerse, 0);
          verseField.setText(HtmlCompat.fromHtml(template, HtmlCompat.FROM_HTML_MODE_COMPACT));
          return;
        }

        if (verseList.size() == 1) {
          // <b>%s %d:%d -</b> %s
          final String template = getResources().getQuantityString(
              R.plurals.scrBookmarkListTemplateVerse, 1);
          final String formattedString = String.format(template, book.getName(), verse.getChapter(),
                                                       verse.getVerse(), verse.getText());
          final Spanned htmlText = HtmlCompat.fromHtml(formattedString,
                                                       HtmlCompat.FROM_HTML_MODE_COMPACT);
          verseField.setText(htmlText);

        } else {
          // <b>%s %d:%d -</b> %s\nand %d more verses exist in this Bookmark entry
          final String template = getResources().getQuantityString(
              R.plurals.scrBookmarkListTemplateVerse, 2);
          final String formattedString = String.format(template, book.getName(), verse.getChapter(),
                                                       verse.getVerse(), verse.getText(),
                                                       verseList.size() - 1);
          final Spanned htmlText = HtmlCompat.fromHtml(formattedString,
                                                       HtmlCompat.FROM_HTML_MODE_COMPACT);
          verseField.setText(htmlText);
        }
      });
    });
  }

}
