package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.db.entities.EntityVerse;
import com.andrewchelladurai.simplebible.model.Book;
import com.andrewchelladurai.simplebible.model.Bookmark;
import com.andrewchelladurai.simplebible.model.Verse;
import com.andrewchelladurai.simplebible.model.view.ChapterViewModel;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterNumberAdapter;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterVerseAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;

public class ChapterScreen
  extends Fragment
  implements ChapterScreenOps {

  private static final String TAG = "ChapterScreen";

  static final String ARG_INT_BOOK = "ARG_INT_BOOK";

  static final String ARG_INT_CHAPTER = "ARG_INT_CHAPTER";

  private ChapterViewModel model;

  private ChapterVerseAdapter verseAdapter;

  private ChapterNumberAdapter chapterAdapter;

  private SimpleBibleOps ops;

  private View rootView;

  @IntRange(from = 1,
            to = Book.MAX_BOOKS)
  private int book;

  @IntRange(from = 1)
  private int chapter;

  @Override
  public void onAttach(@NonNull final Context context) {
    Log.d(TAG, "onAttach:");
    super.onAttach(context);

    if (context instanceof SimpleBibleOps) {
      ops = (SimpleBibleOps) context;
    } else {
      throw new ClassCastException(TAG + " onAttach: [Context] must implement [SimpleBibleOps]");
    }

    model = ViewModelProvider.AndroidViewModelFactory
      .getInstance(requireActivity().getApplication())
      .create(ChapterViewModel.class);

    verseAdapter = new ChapterVerseAdapter(this, getString(R.string.scr_chapter_template_verse));
    chapterAdapter = new ChapterNumberAdapter(this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedState) {

    ops.hideKeyboard();
    ops.hideNavigationView();

    rootView = inflater.inflate(R.layout.chapter_screen, container, false);

    ((RecyclerView) rootView.findViewById(R.id.scr_chapter_list)).setAdapter(verseAdapter);

    final BottomAppBar bar = rootView.findViewById(R.id.scr_chapter_bapp_bar);
    bar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.scr_chapter_menu_bookmark:
          return handleActionBookmark();
        case R.id.scr_chapter_menu_share:
          return handleActionShare();
        case R.id.scr_chapter_menu_previous:
          return handleActionChapterPrevious();
        case R.id.scr_chapter_menu_next:
          return handleActionChaptersNext();
        default:
          Log.e(TAG, "onMenuItemClick: unknown menu item");
      }
      return false;
    });

    if (savedState == null) {
      final int defaultBookNumber = getResources().getInteger(R.integer.default_book_number);
      final int defaultChapterNumber = getResources().getInteger(R.integer.default_chapter_number);
      final Bundle bundle = getArguments();
      if (bundle != null
          && bundle.containsKey(ARG_INT_BOOK)
          && bundle.containsKey(ARG_INT_CHAPTER)) {
        book = bundle.getInt(ARG_INT_BOOK, defaultBookNumber);
        chapter = bundle.getInt(ARG_INT_CHAPTER, defaultChapterNumber);
        Log.d(TAG, "onCreateView: newState: book[" + book + "], chapter[" + chapter + "]");
      } else {
        book = defaultBookNumber;
        chapter = defaultChapterNumber;
        Log.d(TAG, "onCreateView: newState: default book[" + book + "], chapter[" + chapter + "]");
      }

      updateContent();

    } else {

      book = model.getCachedBookNumber();
      chapter = model.getCachedChapterNumber();
      Log.d(TAG, "onCreateView: savedState: cached book[" + book + "], chapter[" + chapter + "]");

      // if we have an open dialog, close it
      ChapterNumberDialog dialog =
        (ChapterNumberDialog) getParentFragmentManager().findFragmentByTag(TAG);
      if (dialog != null) {
        dialog.dismiss();
      }

      refreshData();

    }

    return rootView;
  }

  private void handleActionClear() {
    Log.d(TAG, "handleActionClear:");
    model.clearSelection();
    refreshData();
  }

  private boolean handleActionBookmark() {
    Log.d(TAG, "handleActionBookmark:");
    final Collection<Verse> list = model.getSelectedList();
    if (list == null || list.isEmpty()) {
      Log.d(TAG, "handleActionBookmark: list is null or empty");
      return true;
    }

    final String reference = Bookmark.createBookmarkReference(list);
    final Bundle bundle = new Bundle();
    bundle.putString(BookmarkScreen.ARG_STR_REFERENCE, reference);
    NavHostFragment.findNavController(this)
                   .navigate(R.id.nav_from_scr_chapter_to_scr_bookmark, bundle);
    return true;
  }

  private boolean handleActionShare() {
    Log.d(TAG, "handleActionShare:");
    final Book book = Book.getCachedBook(model.getCachedBookNumber());
    final Collection<Verse> list = model.getSelectedList();
    if (book == null || list == null || list.isEmpty()) {
      Log.e(TAG, "handleActionShare: "
                 + "book / selectedVerseList = null || selectedVerseList is empty");
      return true;
    }

    final StringBuilder verseText = new StringBuilder();
    final String verseTextTemplate = getString(R.string.scr_chapter_template_verse);
    for (final Verse verse : list) {
      verseText.append("\n")
               .append(verse.getFormattedContentForShareChapterVerse(verseTextTemplate));
    }

    ops.shareText(getString(R.string.scr_chapter_template_share,
                            list.size(),
                            book.getName(),
                            model.getCachedChapterNumber(),
                            verseText));

    return true;
  }

  private boolean handleActionChapterPrevious() {
    Log.d(TAG, "handleActionChapterPrevious:");

    final int currentBook = model.getCachedBookNumber();
    final int currentChapter = model.getCachedChapterNumber();

    if (currentBook == 1) {
      if (currentChapter == 1) {
        ops.showMessage(getString(R.string.scr_chapter_msg_navigate_at_start),
                        R.id.scr_chapter_holder_bapp_bar);
        return true;
      } else {
        chapter = currentChapter - 1;
      }
    } else {
      if (currentChapter == 1) {
        book = currentBook - 1;
        //noinspection ConstantConditions
        chapter = Book.getCachedBook(book).getChapters();
      } else {
        chapter = currentChapter - 1;
      }
    }

    updateContent();

    return true;
  }

  private void updateContent() {

    if (book == model.getCachedBookNumber()
        && chapter == model.getCachedChapterNumber()) {
      Log.e(TAG, "updateContent: already cached book[" + book + "], chapter[" + chapter + "]");
      refreshData();
      return;
    }

    Log.d(TAG, "updateContent: book[" + book + "], chapter[" + chapter + "]");

    model.getChapterVerses(book, chapter).observe(getViewLifecycleOwner(), list -> {

      if (list == null || list.isEmpty()) {
        final String msg = getString(R.string.scr_chapter_err_empty_chapter, book, chapter);
        Log.e(TAG, "updateContent: " + msg);
        ops.showErrorScreen(msg, true, true);
        return;
      }

      final Book cachedBook = Book.getCachedBook(book);
      if (cachedBook == null) {
        final String msg = "No book found at position [" + book + "]";
        Log.e(TAG, "updateContent: " + msg);
        ops.showErrorScreen(msg, true, true);
        return;
      }

      final ArrayList<Verse> verseList = new ArrayList<>(list.size());
      for (final EntityVerse verse : list) {
        verseList.add(new Verse(verse, cachedBook));
      }

      model.clearCache();
      model.clearSelection();
      model.setCachedBookNumber(book);
      model.setCachedChapterNumber(chapter);
      model.setCacheList(verseList);

      refreshData();

    });

  }

  private void refreshData() {
    Log.d(TAG, "refreshData:");

    verseAdapter.notifyDataSetChanged();
    updateSelectionActionsVisibility();

    final Book book = Book.getCachedBook(model.getCachedBookNumber());
    if (book == null) {
      Log.e(TAG, "refreshData: Null book, returning");
      return;
    }

    if (chapterAdapter != null) {
      chapterAdapter.updateList(book.getChapters());
    }

    final TextView title = rootView.findViewById(R.id.scr_chapter_title);
    title.setText(getString(R.string.scr_chapter_template_title,
                            book.getName(),
                            model.getCachedChapterNumber(),
                            model.getCachedListSize()));
    ((RecyclerView) rootView.findViewById(R.id.scr_chapter_list)).scrollToPosition(0);
  }

  private boolean handleActionChaptersNext() {
    Log.d(TAG, "handleActionChaptersNext:");

    final int currentBook = model.getCachedBookNumber();
    final int currentChapter = model.getCachedChapterNumber();

    if (currentBook == Book.MAX_BOOKS) {
      //noinspection ConstantConditions
      if (currentChapter == Book.getCachedBook(Book.MAX_BOOKS).getChapters()) {
        ops.showMessage(getString(R.string.scr_chapter_msg_navigate_at_end),
                        R.id.scr_chapter_holder_bapp_bar);
      } else {
        chapter = currentChapter + 1;
      }
    } else {
      //noinspection ConstantConditions
      if (currentChapter == Book.getCachedBook(currentBook).getChapters()) {
        book = currentBook + 1;
        chapter = 1;
      } else {
        chapter = currentChapter + 1;
      }
    }

    updateContent();

    return true;
  }

  private void handleActionChapterList() {
    Log.d(TAG, "handleActionChapters:");
    final Book book = Book.getCachedBook(model.getCachedBookNumber());
    if (book == null) {
      Log.e(TAG, "handleActionChapters: null book, returning");
      return;
    }

    ChapterNumberDialog dialog = new ChapterNumberDialog();
    dialog.updateAdapter(chapterAdapter);
    dialog.show(getParentFragmentManager(), TAG);
  }

  @Override
  public int getCachedListSize() {
    return model.getCachedListSize();
  }

  @Override
  @Nullable
  public Verse getVerseAtPosition(@IntRange(from = 0) final int position) {
    return model.getVerseAtPosition(position);
  }

  @Override
  public void updateSelectionActionsVisibility() {
    boolean isAnySelected = model.getSelectedListSize() > 0;

    final BottomAppBar bar = rootView.findViewById(R.id.scr_chapter_bapp_bar);
    bar.getMenu().setGroupVisible(R.id.scr_chapter_menu_group_selected, isAnySelected);
    bar.getMenu().setGroupVisible(R.id.scr_chapter_menu_group_navigation, !isAnySelected);

    final ExtendedFloatingActionButton
      extendedFab = rootView.findViewById(R.id.scr_chapter_bapp_bar_fab);

    if (isAnySelected) {
      extendedFab.setText(R.string.clear);
      extendedFab.setIcon(getResources().getDrawable(R.drawable.ic_clear,
                                                     requireContext().getTheme()));
      extendedFab.setOnClickListener(view -> handleActionClear());
    } else {
      extendedFab.setText(R.string.chapters);
      extendedFab.setIcon(getResources().getDrawable(R.drawable.ic_list,
                                                     requireContext().getTheme()));
      extendedFab.setOnClickListener(view -> handleActionChapterList());
    }
  }

  @Override
  public void handleNewChapterSelection(@IntRange(from = 1) final int newChapter) {
    ChapterNumberDialog dialog =
      (ChapterNumberDialog) getParentFragmentManager().findFragmentByTag(TAG);
    if (dialog != null) {
      dialog.dismiss();
    }

    if (newChapter == model.getCachedChapterNumber()) {
      Log.d(TAG, "handleNewChapterSelection: not a different chapter");
      return;
    }

    chapter = newChapter;
    updateContent();
  }

  @Override
  public boolean isVerseSelected(@NonNull final Verse verse) {
    return model.isVerseSelected(verse);
  }

  @Override
  public void removeSelectedVerse(@NonNull final Verse verse) {
    model.removeSelectedVerse(verse);
  }

  @Override
  public void addSelectedVerse(@NonNull final Verse verse) {
    model.addSelectedVerse(verse);
  }

}
