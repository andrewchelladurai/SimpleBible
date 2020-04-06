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
import com.andrewchelladurai.simplebible.ui.adapter.ChapterVerseAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.Utils;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Collection;

public class ChapterScreen
    extends Fragment
    implements ChapterScreenOps {

  private static final String TAG = "ChapterScreen";

  public static final String ARG_BOOK = "ARG_BOOK";

  public static final String ARG_CHAPTER = "ARG_CHAPTER";

  private ChapterViewModel model;

  private ChapterVerseAdapter verseListAdapter;

  private ChapterNumberAdapter chapterListAdapter;

  private ChapterDialog chapterDialog;

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

    verseListAdapter = new ChapterVerseAdapter(this,
                                               getString(R.string.scr_chapter_template_verse));
    chapterListAdapter = new ChapterNumberAdapter(this);
    chapterDialog = new ChapterDialog(chapterListAdapter);
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
      refreshData();
    }

    ((RecyclerView) rootView.findViewById(R.id.scr_chapter_list)).setAdapter(verseListAdapter);

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

    updateContent();

    return rootView;
  }

  private void handleActionChapters() {
    Log.d(TAG, "handleActionChapters:");
    final EntityBook book = Utils.getInstance().getCachedBook(model.getCachedBookNumber());
    if (book == null) {
      Log.e(TAG, "handleActionChapters: null book, returning");
      return;
    }

    chapterDialog.show(getParentFragmentManager(), ChapterDialog.TAG);
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
      //      refreshData();
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
    verseListAdapter.notifyDataSetChanged();
    updateSelectionActionsVisibility();

    final EntityBook book = Utils.getInstance().getCachedBook(model.getCachedBookNumber());
    if (book == null) {
      Log.e(TAG, "refreshData: Null book, returning");
      return;
    }

    final Chip chip = rootView.findViewById(R.id.scr_chapter_title);
    chip.setText(getString(R.string.scr_chapter_template_title,
                           book.getName(),
                           model.getCachedChapterNumber(),
                           model.getCachedListSize()));
    chapterListAdapter.updateContent(book.getChapters());
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
    chapterDialog.dismiss();

    if (newChapter == model.getCachedChapterNumber()) {
      Log.d(TAG, "handleNewChapterSelection: not a different chapter");
      return;
    }
    chapter = newChapter;
    updateContent();
    ((RecyclerView) rootView.findViewById(R.id.scr_chapter_list)).scrollToPosition(0);
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

  public static class ChapterDialog
      extends BottomSheetDialogFragment {

    private static final String TAG = "ChapterDialog";

    private final ChapterNumberAdapter adapter;

    ChapterDialog(final ChapterNumberAdapter adapter) {
      this.adapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
      final View view = inflater.inflate(R.layout.chapter_screen_list_chapter, container, false);
      ((RecyclerView) view.findViewById(R.id.scr_chapter_chapter_list)).setAdapter(adapter);
      return view;
    }

  }

  private static class ChapterNumberAdapter
      extends RecyclerView.Adapter {

    private static final String TAG = "ChapterNumberAdapter";

    private static final ArrayList<Integer> CHAPTER_NUMBER_SET = new ArrayList<>();

    @NonNull
    private final ChapterScreenOps ops;

    ChapterNumberAdapter(@NonNull final ChapterScreenOps ops) {
      this.ops = ops;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                      final int viewType) {
      return new ChapterNumberView(
          LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chapter_screen_chapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,
                                 final int position) {
      ((ChapterNumberView) holder).updateItem(position);
    }

    private void updateContent(@IntRange(from = 1) final int chapters) {
      if (CHAPTER_NUMBER_SET.size() == chapters) {
        Log.d(TAG, "updateContent: same number of chapters, no need to refresh");
        return;
      }
      CHAPTER_NUMBER_SET.clear();
      for (int i = 0; i < chapters; i++) {
        CHAPTER_NUMBER_SET.add(i + 1);
      }
      Log.d(TAG, "updateContent: updated chapters numbers to [" + getItemCount() + "]");
    }

    @Override
    public int getItemCount() {
      return CHAPTER_NUMBER_SET.size();
    }

    private class ChapterNumberView
        extends RecyclerView.ViewHolder {

      private final Chip chapterNumberView;

      ChapterNumberView(final View view) {
        super(view);
        chapterNumberView = view.findViewById(R.id.chapter_screen_chapter_item);
      }

      private void updateItem(final int position) {
        final int chapterNumber = CHAPTER_NUMBER_SET.get(position);
        chapterNumberView.setText(String.valueOf(chapterNumber));
        chapterNumberView.setOnClickListener(v -> ops.handleNewChapterSelection(chapterNumber));
      }

    }

  }

}
