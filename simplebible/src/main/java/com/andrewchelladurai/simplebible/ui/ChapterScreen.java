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
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.model.ChapterViewModel;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterVerseAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.Utils;
import com.google.android.material.bottomappbar.BottomAppBar;

public class ChapterScreen
    extends Fragment
    implements ChapterScreenOps {

  private static final String TAG = "ChapterScreen";

  public static final String ARG_BOOK = "ARG_BOOK";

  public static final String ARG_CHAPTER = "ARG_CHAPTER";

  private ChapterViewModel model;

  private ChapterVerseAdapter adapter;

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

    adapter = new ChapterVerseAdapter(this, getString(R.string.scr_chapter_template_verse));
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedState) {
    Log.d(TAG, "onCreateView:");
    ops.hideKeyboard();
    ops.hideNavigationView();
    rootView = inflater.inflate(R.layout.chapter_screen, container, false);

    if (savedState == null) {
      Log.d(TAG, "onCreateView: new State");
      final int defaultBookNumber = getResources().getInteger(R.integer.default_book_number);
      final int defaultChapterNumber = getResources().getInteger(R.integer.default_chapter_number);
      final Bundle bundle = getArguments();
      if (bundle != null
          && bundle.containsKey(ARG_BOOK)
          && bundle.containsKey(ARG_CHAPTER)) {
        book = bundle.getInt(ARG_BOOK, defaultBookNumber);
        chapter = bundle.getInt(ARG_CHAPTER, defaultChapterNumber);
        Log.d(TAG, "onCreateView: book[" + book + "], chapter[" + chapter + "]");
      } else {
        book = defaultBookNumber;
        chapter = defaultChapterNumber;
        Log.d(TAG, "onCreateView: using default book[" + book + "], chapter[" + chapter + "]");
      }
    } else {
      book = model.getCachedBookNumber();
      chapter = model.getCachedChapterNumber();
      Log.d(TAG, "onCreateView: already savedState, using cached book["
                 + book + "], chapter[" + chapter + "]");
    }

    ((RecyclerView) rootView.findViewById(R.id.scr_chapter_list)).setAdapter(adapter);

    final BottomAppBar bar = rootView.findViewById(R.id.scr_chapter_bottom_app_bar);
    bar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.menu_scr_chapter_action_chapters:
          handleActionChapters();
          return true;
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

    updateContent();

    return rootView;
  }

  private void handleActionChapters() {
    Log.d(TAG, "handleActionChapters:");
  }

  private void handleActionClear() {
    Log.d(TAG, "handleActionClear:");
  }

  private void handleActionBookmark() {
    Log.d(TAG, "handleActionBookmark:");
  }

  private void handleActionShare() {
    Log.d(TAG, "handleActionShare:");
  }

  private void updateContent() {

    if (book == model.getCachedBookNumber()
        && chapter == model.getCachedChapterNumber()) {
      Log.e(TAG, "updateContent: already cached book[" + book + "], chapter[" + chapter + "]");
      refreshData();
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
    adapter.notifyDataSetChanged();
    updateSelectionActionsVisibility();
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
