package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.model.ChapterScreenModel;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterNumberAdapter;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterScreenAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ChapterScreen
    extends Fragment
    implements ChapterScreenOps {

  public static final String TAG = "ChapterScreen";
  public static final String ARG_BOOK_NUMBER = "BOOK_NUMBER";
  public static final String ARG_CHAPTER_NUMBER = "CHAPTER_NUMBER";
  private static boolean isChapterNavBarShown;
  private static int chapterListVisibility;
  private ChapterNumberAdapter chapterNumberAdapter;
  private SimpleBibleScreenOps activityOps;
  private ChapterScreenModel model;
  private ChapterScreenAdapter adapter;
  private TextView titleView;
  private RecyclerView chapterList;

  public ChapterScreen() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (context instanceof SimpleBibleScreenOps) {
      activityOps = (SimpleBibleScreenOps) context;
      model = ViewModelProviders.of(this).get(ChapterScreenModel.class);
      adapter = new ChapterScreenAdapter(this,
                                         getString(R.string.item_chapter_verse_content_template));
      chapterNumberAdapter = new ChapterNumberAdapter(this);
    } else {
      throw new RuntimeException(context.toString()
                                 + " must implement InteractionListener");
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    final View view = inflater.inflate(R.layout.chapter_screen, container, false);

    final RecyclerView listView = view.findViewById(R.id.chapter_scr_list);
    listView.setAdapter(adapter);

    titleView = view.findViewById(R.id.chapter_scr_title);

    chapterList = view.findViewById(R.id.chapter_scr_list_chapters);
    chapterList.setAdapter(chapterNumberAdapter);

    view.findViewById(R.id.chapter_scr_butt_chapters)
        .setOnClickListener(v -> handleClickActionChapters());

    if (savedState == null) {
      final Bundle arguments = getArguments();
      if (arguments != null
          && arguments.containsKey(ARG_BOOK_NUMBER)
          && arguments.containsKey(ARG_CHAPTER_NUMBER)) {
        model.setBookNumber(arguments.getInt(ARG_BOOK_NUMBER, 1));
        model.setChapterNumber(arguments.getInt(ARG_CHAPTER_NUMBER, 1));
        Log.d(TAG, "onCreateView: updated book[" + model.getBookNumber()
                   + "], chapter[" + model.getChapterNumber() + "]");
      } else {
        final String message = "onCreateView: "
                               + getString(R.string.chapter_scr_err_no_arguments);
        Log.e(TAG, message);
        activityOps.showErrorScreen(TAG + message, true);
      }
    } else {
      Log.d(TAG,
            "onCreateView: state already saved for book["
            + model.getBookNumber() + "], chapter[" + model.getChapterNumber() + "]");
      chapterList.setVisibility(chapterListVisibility);
    }

    toggleActionButtons();
    updateVerseListView();

    return view;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps.showMainActivityNavBar();
    isChapterNavBarShown = false;
    chapterListVisibility = (chapterList != null) ? chapterList.getVisibility() : View.GONE;
    activityOps = null;
    model = null;
    adapter = null;
  }

  private void updateVerseListView() {
    Log.d(TAG, "updateVerseListView:");
    model.getChapterVerses().observe(this, verses -> {
      if (verses == null || verses.isEmpty()) {
        final String message = String.format(getString(R.string.chapter_src_err_empty_list),
                                             model.getChapterNumber(), model.getBookNumber());
        Log.e(TAG, "onViewCreated: " + message);
        activityOps.showErrorScreen(message, true);
        return;
      }

      model.getBook().observe(this, book -> {
        if (book == null) {
          final String template = getString(R.string.chapter_src_err_book_not_found);
          final String message = ": onViewCreated: "
                                 + String.format(template, model.getBookNumber());
          Log.e(TAG, message);
          activityOps.showErrorScreen(message, true);
          return;
        }

        // update the title
        final String template = getString(R.string.chapter_scr_title_template);
        titleView.setText(String.format(template, book.getName(), model.getChapterNumber()));

        final int chapterCount = book.getChapters();
        if (chapterNumberAdapter.getItemCount() != chapterCount - 1) {
          ArrayList<String> list = new ArrayList<>();
          for (int i = 1; i <= chapterCount; i++) {
            list.add(String.valueOf(i));
          }
          chapterNumberAdapter.refreshList(list);
          chapterNumberAdapter.notifyDataSetChanged();
        }
      });

      adapter.refreshList(verses);
      adapter.notifyDataSetChanged();
    });
  }

  @Override
  public void toggleActionButtons() {
    final boolean selectionEmpty = model.isSelectionEmpty();
    if (selectionEmpty && isChapterNavBarShown) {
      activityOps.showMainActivityNavBar();
      isChapterNavBarShown = false;
    } else if (!selectionEmpty && !isChapterNavBarShown) {
      activityOps.showChapterScreenNavBar(this);
      isChapterNavBarShown = true;
    }
  }

  @Override
  public void updateCache(@NonNull final List<?> newList) {
    model.updateCache(newList);
  }

  @NonNull
  @Override
  public List<?> getCache() {
    return model.getCache();
  }

  @NonNull
  @Override
  public Object getCachedItemAt(final int position) {
    return model.getCachedItemAt(position);
  }

  @Override
  public int getCacheSize() {
    return model.getCacheSize();
  }

  @Override
  public boolean isSelected(@NonNull final Verse verse) {
    return model.isSelected(verse);
  }

  @Override
  public void addSelection(@NonNull final Verse verse) {
    model.addSelection(verse);
  }

  @Override
  public void addSelection(@NonNull final String text) {
    model.addSelection(text);
  }

  @Override
  public void removeSelection(@NonNull final Verse verse) {
    model.removeSelection(verse);
  }

  @Override
  public void removeSelection(@NonNull final String text) {
    model.removeSelection(text);
  }

  @Override
  public void handleClickActionShare() {
    // get the list of texts that are selected and prepare it for sharing
    final HashSet<String> selectedTextList = model.getSelectedTexts();
    final StringBuilder shareText = new StringBuilder();
    for (final String text : selectedTextList) {
      shareText.append(text);
      shareText.append("\n");
    }

    // now reset the selected list
    handleClickActionReset();

    // now let the activity do it's job
    activityOps.shareText(String.format(
        getString(R.string.search_scr_selection_share_template), shareText));
  }

  @Override
  public void handleClickActionBookmark() {
    // get the selected verses into an array so we can pass it
    final HashSet<Verse> selection = model.getSelectedVerses();
    final Verse[] verseArray = new Verse[selection.size()];
    selection.toArray(verseArray);

    // now clear the selection
    handleClickActionReset();

    // now pass the array to the BookmarkScreen
    Bundle bundle = new Bundle();
    bundle.putParcelableArray(BookmarkScreen.ARG_ARRAY_VERSES, verseArray);
    NavHostFragment.findNavController(this)
                   .navigate(R.id.action_chapterScreen_to_bookmarkScreen, bundle);
  }

  @Override
  public void handleClickActionReset() {
    model.cleatSelections();
    adapter.notifyDataSetChanged();
    toggleActionButtons();
  }

  @Override
  public void handleClickChapterNumber(final int newChapterNumber) {
    Log.d(TAG, "handleClickChapterNumber() called with [" + newChapterNumber + "]");
    final Bundle arguments = getArguments();
    if (arguments == null) {
      Log.e(TAG, "handleClickChapterNumber: null argument bundle, returning");
      return;
    }

    arguments.putInt(ARG_CHAPTER_NUMBER, newChapterNumber);
    model.setChapterNumber(newChapterNumber);
    arguments.putInt(ARG_BOOK_NUMBER, model.getBookNumber());

    handleClickActionReset();
    updateVerseListView();
  }

  public void handleClickActionChapters() {
    chapterList.setVisibility(
        (chapterList.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
  }

}
