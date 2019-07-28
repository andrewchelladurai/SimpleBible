package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
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
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.model.ScreenChapterModel;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ScreenChapterOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;

import java.util.ArrayList;

public class ScreenChapter
    extends Fragment
    implements ScreenChapterOps {

  public static final String ARG_BOOK = "ARG_BOOK";
  public static final String ARG_CHAPTER = "ARG_CHAPTER";

  private static final String TAG = "ScreenChapter";
  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private ScreenChapterModel model;

  public ScreenChapter() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement ScreenSimpleBibleOps");
    }
    mainOps = (ScreenSimpleBibleOps) context;
    model = ViewModelProviders.of(this).get(ScreenChapterModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_chapter_fragment, container, false);
    mainOps.hideNavigationView();
    mainOps.hideKeyboard();

    if (savedState == null) {

      final Bundle arguments = getArguments();

      if (arguments == null) {
        final String message = getString(R.string.scrChapterNoArguments);
        Log.e(TAG, "onCreateView: " + message);
        mainOps.showErrorScreen(message, true, true);
        return rootView;
      }

      final Book bookArg = arguments.getParcelable(ARG_BOOK);

      if (bookArg == null) {
        final String message = getString(R.string.scrChapterErrNoBook);
        Log.e(TAG, "onCreateView: " + message);
        mainOps.showErrorScreen(message, true, true);
        return rootView;
      }

      model.setBook(bookArg);
      final int chapter = arguments.getInt(ARG_CHAPTER);
      final int maxChapters = bookArg.getChapters();

      if (chapter < 1 || chapter > maxChapters) {
        final String message = getString(R.string.scrChapterErrChapterInvalid);
        Log.e(TAG, "onCreateView: " + message);
        mainOps.showMessage(message);
        model.setBookChapter(1);
      } else {
        model.setBookChapter(chapter);
      }

      showBookTitleAndChapter();
      updateChapterList(maxChapters);

    }

    rootView.findViewById(R.id.scrChapterActionShowChapters)
            .setOnClickListener(v -> showChapterSelector());
    rootView.findViewById(R.id.scrChapterActionHideChapters)
            .setOnClickListener(v -> showBookTitleAndChapter());
    rootView.findViewById(R.id.scrChapterActionSelectionBookmark)
            .setOnClickListener(v -> handleActionClickBookmark());
    rootView.findViewById(R.id.scrChapterActionSelectionShare)
            .setOnClickListener(v -> handleActionClickShare());
    rootView.findViewById(R.id.scrChapterActionSelectionClear)
            .setOnClickListener(v -> handleActionClickClear());

    updateScreenTitle();
    updateVerseList();

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

  private void updateVerseList() {
    Log.d(TAG, "updateVerseList() called");
    // TODO: 28/7/19 implement logic

    final RecyclerView recyclerView = rootView.findViewById(R.id.scrChapterVerseList);
  }

  private void updateChapterList(final int maxChapters) {
    final ArrayList<Integer> set = new ArrayList<>(maxChapters);
    for (int i = 1; i <= maxChapters; i++) {
      set.add(i);
    }

    final ChapterListAdapter adapter = new ChapterListAdapter(this);
    adapter.updateList(set);

    final RecyclerView recyclerView = rootView.findViewById(R.id.scrChapterChapterList);
    recyclerView.setAdapter(adapter);
  }

  private void updateScreenTitle() {
    final Book book = model.getBook();
    final String htmlText = getString(R.string.scrChapterTitleTemplate,
                                      book.getName(), model.getChapter(),
                                      book.getDescription());
    ((TextView) rootView.findViewById(R.id.scrChapterTitle))
        .setText(HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT));
  }

  private void showBookTitleAndChapter() {
    rootView.findViewById(R.id.scrChapterContainerTitle)
            .setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scrChapterContainerChapterSector)
            .setVisibility(View.GONE);
    rootView.findViewById(R.id.scrChapterContainerSelectionActions)
            .setVisibility(View.GONE);
  }

  private void showChapterSelector() {
    rootView.findViewById(R.id.scrChapterContainerTitle)
            .setVisibility(View.GONE);
    rootView.findViewById(R.id.scrChapterContainerChapterSector)
            .setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scrChapterContainerSelectionActions)
            .setVisibility(View.GONE);
  }

  private void showVerseSelectionActions() {
    rootView.findViewById(R.id.scrChapterContainerTitle)
            .setVisibility(View.GONE);
    rootView.findViewById(R.id.scrChapterContainerChapterSector)
            .setVisibility(View.GONE);
    rootView.findViewById(R.id.scrChapterContainerSelectionActions)
            .setVisibility(View.VISIBLE);
  }

  private void handleActionClickClear() {
    Log.d(TAG, "handleActionClickClear() called");
  }

  private void handleActionClickShare() {
    Log.d(TAG, "handleActionClickShare() called");
  }

  private void handleActionClickBookmark() {
    Log.d(TAG, "handleActionClickBookmark() called");
  }

  @Override public void handleClickChapter(final int chapterNumber) {
    Log.d(TAG, "handleClickChapter: chapterNumber = [" + chapterNumber + "]");
    model.setBookChapter(chapterNumber);
    updateScreenTitle();
    showBookTitleAndChapter();
    updateVerseList();
  }

}
