package com.andrewchelladurai.simplebible.ui;

import android.app.Application;
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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Bookmark;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.model.ScreenBookListModel;
import com.andrewchelladurai.simplebible.model.ScreenBookmarkListModel;
import com.andrewchelladurai.simplebible.object.Book;
import com.andrewchelladurai.simplebible.ui.adapter.ScreenBookmarkListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ScreenBookmarkListOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import com.google.android.material.chip.Chip;

public class ScreenBookmarkList
    extends Fragment
    implements ScreenBookmarkListOps {

  private static final String TAG = "ScreenBookmarkList";

  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private ScreenBookmarkListModel model;
  private ScreenBookmarkListAdapter adapter;
  private ScreenBookListModel bookModel;

  public ScreenBookmarkList() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement InteractionListener");
    }

    final Application application = requireActivity().getApplication();
    mainOps = (ScreenSimpleBibleOps) context;

    adapter = new ScreenBookmarkListAdapter(
        this, getString(R.string.screen_bookmarks_list_msg_empty_note));
    model = new ScreenBookmarkListModel(application);
    bookModel = new ScreenBookListModel(application);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_bookmarks, container, false);

    model.getBookmarkList()
         .observe(getViewLifecycleOwner(), bookmarkList -> {
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

    mainOps.hideKeyboard();
    mainOps.showNavigationView();

    return rootView;
  }

  private void hideList() {
    rootView.findViewById(R.id.screen_bookmarks_list)
            .setVisibility(View.GONE);
  }

  private void showHelpInfo() {
    final String rawText = getString(R.string.screen_bookmarks_help_text);
    final Spanned htmlText = HtmlCompat.fromHtml(rawText, HtmlCompat.FROM_HTML_MODE_COMPACT);

    final TextView textView = rootView.findViewById(R.id.screen_bookmarks_help_text);
    textView.setText(htmlText);

    textView.setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.screen_bookmarks_help_text_container)
            .setVisibility(View.VISIBLE);
  }

  private void hideHelpInfo() {
    rootView.findViewById(R.id.screen_bookmarks_help_text)
            .setVisibility(View.GONE);

    rootView.findViewById(R.id.screen_bookmarks_help_text_container)
            .setVisibility(View.GONE);
  }

  private void showList() {
    final RecyclerView recyclerView = rootView.findViewById(R.id.screen_bookmarks_list);
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

  @Override
  public void updateVerseList(@NonNull final Chip verseCountChip,
                              @NonNull final TextView verseField,
                              @NonNull final String bookmarkReference) {

    final int templateFirstVerse = R.string.screen_bookmarks_list_item_template_first_verse;
    final Spanned htmlVerseNoneFound = HtmlCompat.fromHtml(
        getString(R.string.screen_bookmarks_list_item_msg_no_verse_found),
        HtmlCompat.FROM_HTML_MODE_COMPACT);

    model.getBookmarkedVerse(bookmarkReference).observe(this, verseList -> {

      final int verseCount = verseList.size();
      final String templateVerseCount = getResources().getQuantityString(
          R.plurals.screen_bookmarks_list_item_template_verse_count, verseCount);

      verseCountChip.setText(String.format(templateVerseCount, verseCount));

      if (verseList.isEmpty()) {
        verseCountChip.setText(htmlVerseNoneFound);
        return;
      }

      final Verse verse = verseList.get(0);

      bookModel.getBookUsingNumber(verse.getBook()).observe(this, bookEn -> {
        if (bookEn == null) {
          verseCountChip.setText(htmlVerseNoneFound);
          return;
        }

        final Book book = new Book(bookEn);

        verseField.setText(HtmlCompat.fromHtml(
            String.format(getString(templateFirstVerse),
                          book.getName(),
                          verse.getChapter(),
                          verse.getVerse(),
                          verse.getText()),
            HtmlCompat.FROM_HTML_MODE_COMPACT));
      });
    });
  }

  @Override
  public void handleBookmarkClick(@NonNull final Bookmark bookmark) {
    Log.d(TAG, "handleBookmarkClick: bookmark [" + bookmark + "]");

    final String reference = bookmark.getReference();
    model.getBookmarkedVerse(reference)
         .observe(this, verses -> {
           if (verses == null || verses.isEmpty()) {
             final String message =
                 getString(R.string.screen_bookmark_detail_msg_no_verse);
             Log.e(TAG, "handleBookmarkClick: " + message);
             return;
           }

           final Verse[] array = new Verse[verses.size()];
           for (int i = 0; i < verses.size(); i++) {
             array[i] = verses.get(i);
           }

           Log.d(TAG, "handleBookmarkClick: got [" + array.length + "] verses");

           final Bundle bundle = new Bundle();
           bundle.putParcelableArray(ScreenBookmarkDetail.ARG_VERSE_LIST, array);
           Log.d(TAG, "handleBookmarkClick: created bundle to pass to fragment");

           final int destination =
               R.id.screen_bookmark_list_to_screen_bookmark_detail;
           NavHostFragment.findNavController(this)
                          .navigate(destination, bundle);
         });

  }

}
