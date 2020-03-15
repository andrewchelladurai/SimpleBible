package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.ScreenBookListModel;
import com.andrewchelladurai.simplebible.objects.Book;
import com.andrewchelladurai.simplebible.ui.adapter.BookListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ScreenBookListOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.BookUtils;

public class ScreenBookList
    extends Fragment
    implements ScreenBookListOps {

  private static final String TAG = "ScreenBookList";

  private ScreenSimpleBibleOps mainOps;
  private ScreenBookListModel model;
  private BookListAdapter adapter;
  private View rootView;

  public ScreenBookList() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement InteractionListener");
    }
    mainOps = (ScreenSimpleBibleOps) context;
    model = new ScreenBookListModel(requireActivity().getApplication());
    adapter = new BookListAdapter(this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_books, container, false);

    final SearchView searchView = rootView.findViewById(R.id.screen_books_search);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(final String query) {
        if (query != null) {
          if (query.isEmpty()) {
            adapter.filterList("");
          } else {
            adapter.filterList(query);
          }
        }
        return false;
      }

      @Override
      public boolean onQueryTextChange(final String query) {
        if (query != null) {
          if (query.isEmpty()) {
            adapter.filterList("");
          } else {
            adapter.filterList(query);
          }
        }
        return false;
      }
    });

    final RecyclerView recyclerView = rootView.findViewById(R.id.screen_books_list);
    recyclerView.setAdapter(adapter);

    if (savedState == null) {
      model.getAllBooks().observe(getViewLifecycleOwner(), entityBookList -> {

        if (entityBookList == null) {
          final String message = getString(R.string.screen_bookmarks_msg_no_books);
          Log.e(TAG, "onCreateView: " + message);
          mainOps.showErrorScreen(message, true, true);
          return;
        }

        if (entityBookList.isEmpty() || entityBookList.size() != BookUtils.EXPECTED_COUNT) {
          final String message = getString(R.string.screen_bookmarks_msg_book_count_incorrect);
          Log.e(TAG, "onCreateView: " + message);
          mainOps.showErrorScreen(message, true, true);
          return;
        }

        adapter.updateList(entityBookList);
      });
    }

    mainOps.hideKeyboard();
    mainOps.showNavigationView();

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
    model = null;
    adapter = null;
  }

  @NonNull
  @Override
  public String getFormattedBookDetails(final int chapterCount) {
    return getResources().getQuantityString(
        R.plurals.screen_bookmarks_list_item_template_chapter_count, chapterCount, chapterCount);
  }

  @Override
  public void handleBookClick(@NonNull final Book book) {
    final Bundle bundle = new Bundle();
    bundle.putInt(ScreenChapter.ARG_BOOK, book.getNumber());
    bundle.putInt(ScreenChapter.ARG_CHAPTER, 1);
    bundle.putInt(ScreenChapter.ARG_VERSE, 1);

    NavHostFragment.findNavController(this)
                   .navigate(R.id.screen_books_to_screen_chapter, bundle);

    final SearchView searchView = rootView.findViewById(R.id.screen_books_search);
    if (searchView.getQuery()
                  .length() > 0) {
      searchView.setQuery("", true);
    }
  }

}
