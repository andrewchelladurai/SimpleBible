package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.ui.ops.SbRecyclerViewAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.SbViewHolderOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenChapterOps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.andrewchelladurai.simplebible.utils.BookUtils.EXPECTED_COUNT;

public class ScrChapterVerseAdapter
    extends RecyclerView.Adapter
    implements SbRecyclerViewAdapterOps {

  private static final String TAG = "ScrChapterVerseAdapter";
  private static ArrayList<Verse> LIST = new ArrayList<>();
  private static HashMap<Verse, String> SELECTED_LIST = new HashMap<>();

  @IntRange(from = 1, to = EXPECTED_COUNT)
  private static int cachedBookNumber = 1;

  @IntRange(from = 1)
  private static int cachedChapterNumber = 1;

  private final ScreenChapterOps ops;
  private String bookDetails;

  public ScrChapterVerseAdapter(final ScreenChapterOps ops) {
    this.ops = ops;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new ChapterVerseView(LayoutInflater.from(parent.getContext())
                                              .inflate(R.layout.item_chapter_verse, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((ChapterVerseView) holder).updateView(LIST.get(position), position);
  }

  public int getSelectedItemCount() {
    return SELECTED_LIST.size();
  }

  @Override
  public int getItemCount() {
    return LIST.size();
  }

  @Override
  public void updateList(@NonNull final List<?> list) {
    for (final Object o : list) {
      LIST.add((Verse) o);
    }
    Log.d(TAG, "updateList: updated [" + getItemCount() + "] records");
  }

  @Override
  public void filterList(@NonNull final String searchTerm) {
    Log.e(TAG, "filterList: NOT IMPLEMENTED");
  }

  @Override
  public void clearList() {
    LIST.clear();
    SELECTED_LIST.clear();
    notifyDataSetChanged();
  }

  public void clearSelection() {
    SELECTED_LIST.clear();
    notifyDataSetChanged();
  }

  @NonNull
  public HashMap<Verse, String> getSelectedVerses() {
    return SELECTED_LIST;
  }

  @NonNull
  public String getBookDetails() {
    return bookDetails;
  }

  public void setBookDetails(@NonNull final String bookDetails) {
    if (bookDetails.isEmpty()) {
      Log.e(TAG, "setBookDetails: Empty Book Details Sent");
      return;
    }

    this.bookDetails = bookDetails;
  }

  @IntRange(from = 1)
  public int getCachedBookNumber() {
    return cachedBookNumber;
  }

  public void setCachedBookNumber(@IntRange(from = 1,
                                            to = EXPECTED_COUNT) int cachedBookNumber) {
    ScrChapterVerseAdapter.cachedBookNumber = cachedBookNumber;
  }

  @IntRange(from = 1)
  public int getCachedChapterNumber() {
    return cachedChapterNumber;
  }

  public void setCachedChapterNumber(@IntRange(from = 1) int cachedChapterNumber) {
    ScrChapterVerseAdapter.cachedChapterNumber = cachedChapterNumber;
  }

  private class ChapterVerseView
      extends RecyclerView.ViewHolder
      implements SbViewHolderOps {

    private final TextView textView;
    private Verse verse;

    ChapterVerseView(@NonNull final View view) {
      super(view);
      textView = view.findViewById(R.id.itemChapterVerse);
      textView.setOnClickListener(view1 -> {
        if (SELECTED_LIST.containsKey(verse)) {
          SELECTED_LIST.remove(verse);
        } else {
          SELECTED_LIST.put(verse, textView.getText().toString());
        }
        textView.setSelected(SELECTED_LIST.containsKey(verse));
        ops.handleClickVerse();
      });
    }

    @Override
    public void updateView(final Object object, final int position) {
      verse = (Verse) object;

      ops.updateVerseView(verse, textView);
      textView.setSelected(SELECTED_LIST.containsKey(verse));
    }

  }

}
