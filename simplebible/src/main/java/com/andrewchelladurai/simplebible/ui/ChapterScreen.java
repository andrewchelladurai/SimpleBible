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

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.ChapterViewModel;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.Utils;

public class ChapterScreen
    extends Fragment
    implements ChapterScreenOps {

  private static final String TAG = "ChapterScreen";

  public static final String ARG_BOOK = "ARG_BOOK";

  public static final String ARG_CHAPTER = "ARG_CHAPTER";

  private ChapterViewModel model;

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
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedState) {
    Log.d(TAG, "onCreateView:");
    ops.hideKeyboard();
    ops.hideNavigationView();
    rootView = inflater.inflate(R.layout.chapter_screen, container, false);

    if (savedState == null) {
      final Bundle bundle = getArguments();
      if (bundle != null
          && bundle.containsKey(ARG_BOOK)
          && bundle.containsKey(ARG_CHAPTER)) {
        book = bundle.getInt(ARG_BOOK, 1);
        chapter = bundle.getInt(ARG_CHAPTER, 29);
      } else {
        book = 1;
        chapter = 29;
      }
    } else {
      book = model.getCurrentBook();
      chapter = model.getCurrentChapter();
    }

    updateContent();

    return rootView;
  }

  private void updateContent() {
    Log.d(TAG, "updateContent:");

    if (book == model.getCurrentBook()
        && chapter == model.getCurrentChapter()) {
      Log.e(TAG, "updateContent: already cached book[" + book + "], chapter[" + chapter + "]");
      refreshData();
      return;
    }

    model.setCurrentBook(book);
    model.setCurrentChapter(chapter);

    refreshData();

  }

  private void refreshData() {
    Log.d(TAG, "refreshData:");
  }

}
