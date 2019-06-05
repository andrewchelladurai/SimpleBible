package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.BookListScreenModel;
import com.andrewchelladurai.simplebible.ui.adapter.BookListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookListScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import java.util.List;

public class BookListScreen
    extends Fragment
    implements BookListScreenOps {

  private static final String TAG = "BookListScreen";
  private BookListAdapter listAdapter;
  private SimpleBibleScreenOps activityOps;
  private BookListScreenModel model;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof SimpleBibleScreenOps)) {
      throw new RuntimeException(context.toString() + " must implement SimpleBibleScreenOps");
    }
    model = ViewModelProviders.of(this).get(BookListScreenModel.class);
    activityOps = (SimpleBibleScreenOps) context;
    listAdapter = new BookListAdapter(this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    final View view = inflater.inflate(R.layout.booklist_screen, container, false);

    final SearchView searchView = view.findViewById(R.id.book_list_scr_query);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(final String query) {
        return listAdapter.filterList(query);
      }

      @Override
      public boolean onQueryTextChange(final String newText) {
        return listAdapter.filterList(newText);
      }
    });

    final RecyclerView recyclerView = view.findViewById(R.id.book_list_scr_list);
    recyclerView.setAdapter(listAdapter);

    if (savedState == null) {
      model.getAllBooks().observe(this, bookList -> {
        if (bookList == null || bookList.isEmpty() || bookList.size() != BookUtils.EXPECTED_COUNT) {
          final String message = getString(R.string.book_list_scr_err_book_count);
          Log.e(TAG, "onCreateView: " + message);
          activityOps.showErrorScreen(message, true);
          return;
        }
        listAdapter.refreshList(bookList);
        listAdapter.notifyDataSetChanged();
      });
    }

    return view;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps = null;
    listAdapter = null;
  }

  @Override
  public void handleClickBookSelection(@NonNull final String bookName) {
    Log.d(TAG, "handleClickBookSelection(): bookName = [" + bookName + "]");
    activityOps.hideKeyboard();
    if (bookName.isEmpty()) {
      Log.e(TAG, "handleClickBookSelection: how did I get an empty bookName, ignoring it");
      return;
    }

    final int bookNumber = model.getBookNumber(bookName, listAdapter.getList());
    if (bookNumber == 0) {
      String message = getString(R.string.book_list_scr_err_book_name);
      activityOps.showErrorMessage(message);
      return;
    }

    Bundle args = new Bundle();
    args.putInt(ChapterScreen.ARG_BOOK_NUMBER, bookNumber);
    args.putInt(ChapterScreen.ARG_CHAPTER_NUMBER, 1);
    NavHostFragment.findNavController(this)
                   .navigate(R.id.action_bookListScreen_to_chapterScreen, args);
  }

  @NonNull
  @Override
  public String getContentString(@IntRange(from = 1) final int verses,
                                 @IntRange(from = 1) final int chapters) {
    final String template = getString(R.string.item_book_template_details);
    final String verseString = getResources().getQuantityString(
        R.plurals.item_book_template_verses, verses, verses);
    final String chapterString = getResources().getQuantityString(
        R.plurals.item_book_template_chapter, chapters, chapters);

    return String.format(template, verseString, chapterString);
  }

  @Override
  public void refreshCachedList(@NonNull final List<?> list) {
    model.refreshCachedList(list);
  }

  @NonNull
  @Override
  public List<?> getCachedList() {
    return model.getCachedList();
  }

  @NonNull
  @Override
  public Object getCachedItemAt(final int position) {
    return model.getCachedItemAt(position);
  }

  @Override
  public int getCachedListSize() {
    return model.getCachedListSize();
  }

}
