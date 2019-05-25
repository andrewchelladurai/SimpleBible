package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.model.ChapterScreenModel;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterScreenAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;

import java.util.HashSet;
import java.util.List;

public class ChapterScreen
    extends Fragment
    implements ChapterScreenOps {

  public static final String TAG = "ChapterScreen";
  public static final String ARG_BOOK_NUMBER = "BOOK_NUMBER";
  public static final String ARG_CHAPTER_NUMBER = "CHAPTER_NUMBER";

  private SimpleBibleScreenOps activityOps;
  private ChapterScreenModel model;
  private ChapterScreenAdapter adapter;

  private AppCompatImageButton shareButView;
  private AppCompatImageButton bookmarkButView;
  private AppCompatImageButton resetButView;
  private AppCompatImageButton chaptersButView;
  private TextView titleView;

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

    // TODO: 25/5/19 implement interaction handlers for actions
    shareButView = view.findViewById(R.id.chapter_scr_butt_share);
    shareButView.setOnClickListener(v -> handleClickActionShare());
    bookmarkButView = view.findViewById(R.id.chapter_scr_butt_bmark);
    bookmarkButView.setOnClickListener(v -> handleClickActionBookmark());
    resetButView = view.findViewById(R.id.chapter_scr_butt_clear);
    resetButView.setOnClickListener(v -> handleClickActionReset());
    chaptersButView = view.findViewById(R.id.chapter_scr_butt_chapters);
    chaptersButView.setOnClickListener(v -> handleClickActionChapters());

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
    }
    return view;
  }

  @Override
  public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedState) {
    super.onViewCreated(view, savedState);
    model.getChapterVerses().observe(this, verses -> {
      if (verses == null || verses.isEmpty()) {
        final String message = String.format(getString(R.string.chapter_src_err_empty_list),
                                             model.getChapterNumber(), model.getBookNumber());
        Log.e(TAG, "onViewCreated: " + message);
        activityOps.showErrorScreen(message, true);
        return;
      }
      // TODO: 25/5/19 - Update title
      // titleView.setText();
      adapter.refreshList(verses);
      adapter.notifyDataSetChanged();
    });
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps = null;
    model = null;
    adapter = null;
  }

  private void handleClickActionChapters() {
    // TODO: 25/5/19 implement this
  }

  private void handleClickActionReset() {
    model.cleatSelections();
    adapter.notifyDataSetChanged();
    toggleActionButtons();
  }

  private void handleClickActionBookmark() {
    // get the selected verses into an array so we can pass it
    final HashSet<Verse> selection = model.getSelection();
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

  private void handleClickActionShare() {
    // get the list of texts that are selected and prepare it for sharing
    final HashSet<String> selectedTextList = model.getSelectedText();
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
  public void toggleActionButtons() {
    final int visibilityValue = (model.isSelectionEmpty()) ? View.GONE : View.VISIBLE;
    shareButView.setVisibility(visibilityValue);
    bookmarkButView.setVisibility(visibilityValue);
    resetButView.setVisibility(visibilityValue);
    chaptersButView.setVisibility(visibilityValue);
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
    return model.isResultSelected(verse);
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

}
