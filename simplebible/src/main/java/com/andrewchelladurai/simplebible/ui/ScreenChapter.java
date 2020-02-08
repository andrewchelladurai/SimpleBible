package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.model.ScreenChapterModel;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterNumberAdapter;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterVerseAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ScreenChapterOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ScreenChapter
    extends Fragment
    implements ScreenChapterOps {

  static final String ARG_BOOK = "ARG_BOOK";
  static final String ARG_CHAPTER = "ARG_CHAPTER";
  static final String ARG_VERSE = "ARG_VERSE";

  private static final String TAG = "ScreenChapter";
  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private ScreenChapterModel model;
  private ChapterVerseAdapter adapter;
  private ChapterSelectionFragment chapterSelectionDialog;

  public ScreenChapter() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement ScreenSimpleBibleOps");
    }

    mainOps = (ScreenSimpleBibleOps) context;
    model = new ScreenChapterModel(requireActivity().getApplication());
    adapter = new ChapterVerseAdapter(this, getString(R.string.itm_chapter_verse_content_template));
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_chapter, container, false);

    final BottomAppBar bottomAppBar = rootView.findViewById(R.id.scr_chapter_menu);
    bottomAppBar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.scr_chapter_menu_bookmark:
          handleActionClickBookmark();
          return true;
        case R.id.scr_chapter_menu_share:
          handleActionClickShare();
          return true;
        case R.id.scr_chapter_menu_clear:
          handleActionClickClear();
          return true;
        case R.id.scr_chapter_menu_list_dialog:
          showChapterSelector();
          return true;
        default:
          Log.e(TAG, "onCreateView: Unknown Menu Item in BottomAppBar ["
                     + item.getTitle() + "]");
          return false;
      }
    });

    if (savedState == null) {

      final Bundle arguments = getArguments();

      if (arguments == null) {
        final String message = getString(R.string.scr_chapter_msg_no_args);
        Log.e(TAG, "onCreateView: " + message);
        mainOps.showErrorScreen(message, true, true);
        return rootView;
      }

      final int bookNumber = arguments.getInt(ARG_BOOK);
      final int chapterNumber = arguments.getInt(ARG_CHAPTER);
      final int verseNumber = arguments.getInt(ARG_VERSE);

      if (bookNumber < 1 || bookNumber > BookUtils.EXPECTED_COUNT) {
        final String message = getString(R.string.scr_chapter_msg_book_invalid);
        Log.e(TAG, "onCreateView: " + message);
        mainOps.showErrorScreen(message, true, true);
        return rootView;
      }

      model.getBook(bookNumber)
           .observe(getViewLifecycleOwner(), book -> {

             if (book != null) {

               // book is good, cache it and use it
               model.setCachedBook(book);

               if (chapterNumber < 1 || chapterNumber > book.getChapters()) {
                 // chapter is not good, show first chapter from the book
                 final String message = getString(R.string.scr_chapter_msg_chapter_invalid);
                 Log.e(TAG, "onCreateView: " + message);
                 mainOps.showMessage(message);
                 model.setCachedChapterNumber(1);
               } else {
                 // chapter is good, cache & use it
                 model.setCachedChapterNumber(chapterNumber);
               }

               if (verseNumber < 1) {
                 // verse is not good, show first verse from the chapter
                 final String message = getString(R.string.scr_chapter_msg_verse_invalid);
                 Log.e(TAG, "onCreateView: " + message);
                 mainOps.showMessage(message);
                 model.setCachedVerseNumber(1);
               } else {
                 // verse is good, cache & use it
                 model.setCachedVerseNumber(verseNumber);
               }

               updateScreenTitle();
               updateVerseList();

             } else {
               final String message = getString(R.string.scr_chapter_msg_book_invalid);
               Log.e(TAG, "onCreateView: " + message);
               mainOps.showErrorScreen(message, true, true);
             }
           });
    } else {
      updateScreenTitle();
      updateVerseList();

      updateVerseSelectionActionsVisibility();
    }

    mainOps.hideNavigationView();
    mainOps.hideKeyboard();

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

  private void handleActionClickClear() {
    adapter.clearSelection();
    updateVerseSelectionActionsVisibility();
  }

  private void handleActionClickBookmark() {
    // get the list of all verses that are selected and sort it
    final ArrayList<Verse> list = new ArrayList<>(adapter.getSelectedVerses()
                                                         .keySet());
    //noinspection unchecked
    Collections.sort(list);

    // now clear the selection, since our work is done.
    handleActionClickClear();

    // convert the list into an array
    final Verse[] array = new Verse[list.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = list.get(i);
    }

    // now create a bundle of the verses to pass to the Bookmark Screen
    final Bundle bundle = new Bundle();
    bundle.putParcelableArray(ScreenBookmarkDetail.ARG_VERSE_LIST, array);

    NavHostFragment.findNavController(this)
                   .navigate(R.id.nav_act_scr_chapter_to_scr_bookmark, bundle);
  }

  private void handleActionClickShare() {
    final StringBuilder shareText = new StringBuilder();
    shareText.append(adapter.getBookDetails())
             .append("\n\n");

    // get the list of all verses that are selected and sort it
    final HashMap<Verse, String> versesMap = adapter.getSelectedVerses();
    final ArrayList<Verse> keySet = new ArrayList<>(versesMap.keySet());
    //noinspection unchecked
    Collections.sort(keySet);

    // now get the text from the selected verses
    for (final Verse verse : keySet) {
      shareText.append(versesMap.get(verse))
               .append("\n");
    }

    mainOps.shareText(String.format(getString(R.string.scr_search_template_share), shareText));
  }

  private void showChapterSelector() {
    final int chapterCount = model.getCachedBook()
                                  .getChapters();
    chapterSelectionDialog = ChapterSelectionFragment.createInstance(this, chapterCount);
    chapterSelectionDialog.show(getParentFragmentManager(), "ChapterSelectionFragment");
  }

  private void updateScreenTitle() {
    final Book book = model.getCachedBook();
    final String htmlText = getString(R.string.scr_chapter_title_template,
                                      book.getName(), model.getCachedChapterNumber(),
                                      book.getDescription());
    final String titleText = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT)
                                       .toString();
    adapter.setBookDetails(titleText);

    final TextView title = rootView.findViewById(R.id.scr_chapter_title);
    title.setText(titleText);
  }

  private void updateVerseList() {
    model.getChapterVerseList()
         .observe(getViewLifecycleOwner(), list -> {
           final int bookNumber = model.getCachedBook()
                                       .getNumber();
           final int chapterNumber = model.getCachedChapterNumber();

           if (list == null || list.isEmpty()) {
             final Bundle bundle = new Bundle();
             final String message =
                 String.format(getString(R.string.scr_chapter_msg_no_verse_found),
                               chapterNumber, model.getCachedBook()
                                                   .getName());
             bundle.putString(ScreenError.ARG_MESSAGE, message);
             bundle.putBoolean(ScreenError.ARG_EXIT_APP, true);
             bundle.putBoolean(ScreenError.ARG_INFORM_DEV, true);

             NavHostFragment.findNavController(this)
                            .navigate(R.id.nav_act_global_scr_error, bundle);
             return;
           }

           final int cachedBookNumber = adapter.getCachedBookNumber();
           final int cachedChapterNumber = adapter.getCachedChapterNumber();

           if (bookNumber != cachedBookNumber
               || chapterNumber != cachedChapterNumber
               || adapter.getItemCount() < 1) {
             adapter.clearList();

             adapter.setCachedBookNumber(bookNumber);
             adapter.setCachedChapterNumber(chapterNumber);

             Log.d(TAG, "updateVerseList: bookNumber[" + bookNumber
                        + "], chapterNumber[" + chapterNumber + "]");

             adapter.updateList(list);
           }

           final RecyclerView recyclerView = rootView.findViewById(R.id.scr_chapter_list);
           recyclerView.setAdapter(adapter);
         });
  }

  @Override
  public void handleClickVerse() {
    updateVerseSelectionActionsVisibility();
  }

  private void updateVerseSelectionActionsVisibility() {
    final boolean isVerseSelected = adapter.getSelectedItemCount() > 0;
    final BottomAppBar bottomAppBar = rootView.findViewById(R.id.scr_chapter_menu);
    final Menu menu = bottomAppBar.getMenu();
    menu.setGroupVisible(R.id.scr_chapter_menu_container_selected, isVerseSelected);
    menu.setGroupVisible(R.id.scr_chapter_menu_container_not_selected, !isVerseSelected);
    rootView.findViewById(R.id.scr_chapter_container_title)
            .setVisibility((isVerseSelected) ? View.GONE : View.VISIBLE);
  }

  @Override
  public void handleClickChapter(final int chapterNumber) {
    Log.d(TAG, "handleClickChapter: chapterNumber = [" + chapterNumber + "]");
    model.setCachedChapterNumber(chapterNumber);
    updateScreenTitle();
    updateVerseList();
    if (chapterSelectionDialog != null) {
      chapterSelectionDialog.dismiss();
    }
  }

  public static class ChapterSelectionFragment
      extends BottomSheetDialogFragment {

    private static ChapterNumberAdapter chapterNumberAdapter;

    public ChapterSelectionFragment() {
    }

    @NonNull
    private static ChapterSelectionFragment createInstance(
        @NonNull final ScreenChapterOps screenChapterOps,
        final int maxChapters) {
      final ArrayList<Integer> set = new ArrayList<>(maxChapters);
      for (int i = 1; i <= maxChapters; i++) {
        set.add(i);
      }

      chapterNumberAdapter = new ChapterNumberAdapter(screenChapterOps);
      chapterNumberAdapter.updateList(set);

      return new ChapterSelectionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
      final View view = inflater
                            .inflate(R.layout.screen_chapter_dialog, container, false);

      ((RecyclerView) view.findViewById(R.id.scr_chapter_bottom_sheet_list))
          .setAdapter(chapterNumberAdapter);

      return view;
    }

  }

}
