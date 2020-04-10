package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.EntityBook;
import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.model.ChapterViewModel;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterNumberAdapter;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterVerseAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.Utils;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.Chip;

import java.util.Collection;

public class ChapterScreen
    extends Fragment
    implements ChapterScreenOps {

  private static final String TAG = "ChapterScreen";

  static final String ARG_BOOK = "ARG_BOOK";

  static final String ARG_CHAPTER = "ARG_CHAPTER";

  private ChapterViewModel model;

  private ChapterVerseAdapter verseAdapter;

  private ChapterNumberAdapter chapterAdapter;

  private SimpleBibleOps ops;

  private View rootView;

  @IntRange(from = 1, to = Utils.MAX_BOOKS)
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

    final BottomAppBar bar = rootView.findViewById(R.id.scr_chapter_bottom_app_bar);
    bar.setNavigationOnClickListener(v -> handleActionChapters());
    bar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.menu_scr_chapter_action_clear:
          handleActionClear();
          return true;
        case R.id.menu_scr_chapter_action_bookmark:
          handleActionBookmark();
          return true;
        case R.id.menu_scr_chapter_action_share:
          handleActionShare();
          return true;
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
          && bundle.containsKey(ARG_BOOK)
          && bundle.containsKey(ARG_CHAPTER)) {
        book = bundle.getInt(ARG_BOOK, defaultBookNumber);
        chapter = bundle.getInt(ARG_CHAPTER, defaultChapterNumber);
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

  private void handleActionChapters() {
    Log.d(TAG, "handleActionChapters:");
    final EntityBook book = Utils.getInstance().getCachedBook(model.getCachedBookNumber());
    if (book == null) {
      Log.e(TAG, "handleActionChapters: null book, returning");
      return;
    }

    ChapterNumberDialog dialog = new ChapterNumberDialog();
    dialog.updateAdapter(chapterAdapter);
    dialog.show(getParentFragmentManager(), TAG);

  }

  private void handleActionClear() {
    Log.d(TAG, "handleActionClear:");
    model.clearSelection();
    refreshData();
  }

  private void handleActionBookmark() {
    Log.d(TAG, "handleActionBookmark:");
    final Collection<EntityVerse> list = model.getSelectedList();
    if (list == null || list.isEmpty()) {
      Log.d(TAG, "handleActionBookmark: list is null or empty");
      return;
    }

    final String reference = Utils.getInstance().createBookmarkReference(list);
    final Bundle bundle = new Bundle();
    bundle.putString(BookmarkScreen.ARG_REFERENCE, reference);
    NavHostFragment.findNavController(this)
                   .navigate(R.id.nav_from_scr_chapter_to_scr_bookmark, bundle);
  }

  private void handleActionShare() {
    Log.d(TAG, "handleActionShare:");
    final EntityBook book = Utils.getInstance().getCachedBook(model.getCachedBookNumber());
    final Collection<EntityVerse> list = model.getSelectedList();
    if (book == null || list == null || list.isEmpty()) {
      Log.e(TAG, "handleActionShare: "
                 + "book / selectedVerseList = null || selectedVerseList is empty");
      return;
    }

    final StringBuilder verseText = new StringBuilder();
    final String verseTextTemplate = getString(R.string.scr_chapter_template_verse);
    for (final EntityVerse verse : list) {
      verseText.append("\n")
               .append(String.format(verseTextTemplate, verse.getVerse(), verse.getText()));
    }

    ops.shareText(getString(R.string.scr_chapter_template_share,
                            list.size(),
                            book.getName(),
                            model.getCachedChapterNumber(),
                            verseText));
  }

  private void updateContent() {

    if (book == model.getCachedBookNumber()
        && chapter == model.getCachedChapterNumber()) {
      Log.e(TAG, "updateContent: already cached book[" + book + "], chapter[" + chapter + "]");
      return;
    }

    Log.d(TAG, "updateContent: book[" + book + "], chapter[" + chapter + "]");

    model.getChapterVerses(book, chapter).observe(getViewLifecycleOwner(), verseList -> {

      if (verseList == null || verseList.isEmpty()) {
        final String msg = getString(R.string.scr_chapter_err_empty_chapter, book, chapter);
        Log.e(TAG, "updateContent: " + msg);
        ops.showErrorScreen(msg, true, true);
        return;
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

    final EntityBook book = Utils.getInstance().getCachedBook(model.getCachedBookNumber());
    if (book == null) {
      Log.e(TAG, "refreshData: Null book, returning");
      return;
    }

    if (chapterAdapter != null) {
      chapterAdapter.updateList(book.getChapters());
    }

    final Chip chip = rootView.findViewById(R.id.scr_chapter_title);
    chip.setText(getString(R.string.scr_chapter_template_title,
                           book.getName(),
                           model.getCachedChapterNumber(),
                           model.getCachedListSize()));
    ((RecyclerView) rootView.findViewById(R.id.scr_chapter_list)).scrollToPosition(0);
  }

  @Override
  public int getCachedListSize() {
    return model.getCachedListSize();
  }

  @Override
  @Nullable
  public EntityVerse getVerseAtPosition(@IntRange(from = 0) final int position) {
    return model.getVerseAtPosition(position);
  }

  @Override
  public void updateSelectionActionsVisibility() {
    final BottomAppBar bar = rootView.findViewById(R.id.scr_chapter_bottom_app_bar);
    bar.getMenu().setGroupVisible(R.id.menu_scr_chapter_actions_selection,
                                  model.getSelectedListSize() > 0);
    rootView.findViewById(R.id.scr_chapter_title)
            .setVisibility(model.getSelectedListSize() > 0 ? View.GONE : View.VISIBLE);
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
  public boolean isVerseSelected(@NonNull final EntityVerse verse) {
    return model.isVerseSelected(verse);
  }

  @Override
  public void removeSelectedVerse(@NonNull final EntityVerse verse) {
    model.removeSelectedVerse(verse);
  }

  @Override
  public void addSelectedVerse(@NonNull final EntityVerse verse) {
    model.addSelectedVerse(verse);
  }

}
