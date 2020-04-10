package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.EntityBook;
import com.andrewchelladurai.simplebible.model.BookListViewModel;
import com.andrewchelladurai.simplebible.ui.adapter.BookListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookListScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.Set;

public class BookListScreen
    extends Fragment
    implements BookListScreenOps {

  private static final String TAG = "BookListScreen";

  private BookListViewModel model;

  private SimpleBibleOps ops;

  private View rootView;

  private BookListAdapter booksAdapter;

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
                .create(BookListViewModel.class);

    booksAdapter = new BookListAdapter(this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView:");
    rootView = inflater.inflate(R.layout.book_list_screen, container, false);

    final RecyclerView recyclerView = rootView.findViewById(R.id.scr_book_list_list);
    recyclerView.setAdapter(booksAdapter);

    final SearchView searchView = rootView.findViewById(R.id.scr_book_list_search);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

      @Override
      public boolean onQueryTextSubmit(final String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(final String newText) {
        booksAdapter.filterList((newText == null) ? "" : newText);
        booksAdapter.notifyDataSetChanged();
        return true;
      }
    });

    if (savedInstanceState == null) {
      updateContent();
    }

    return rootView;
  }

  private void updateContent() {
    Log.d(TAG, "updateContent:");

    final Utils utils = Utils.getInstance();
    final Set<Integer> books = utils.getCachedBookList();

    if (books.size() != Utils.MAX_BOOKS) {
      refreshContent();
      return;
    }

    model.getAllBooks().observe(getViewLifecycleOwner(), bookList -> {

      if (bookList == null || bookList.isEmpty() || bookList.size() != Utils.MAX_BOOKS) {
        final String msg = getString(R.string.scr_book_list_err_invalid_list,
                                     (bookList == null) ? 0 : bookList.size(),
                                     Utils.MAX_BOOKS);
        Log.e(TAG, "updateContent: " + msg);
        ops.showErrorScreen(msg, true, true);
        return;
      }

      utils.updateCacheBooks(bookList);
      refreshContent();

    });
  }

  private void refreshContent() {
    Log.d(TAG, "refreshContent:");
    booksAdapter.filterList("");
    booksAdapter.notifyDataSetChanged();
  }

  @Override
  public void handleBookSelection(@Nullable final EntityBook book) {
    Log.d(TAG, "handleBookSelection:");

    if (book == null) {
      Log.e(TAG, "handleBookSelection: cannot show a null book");
      return;
    }

    Log.d(TAG, "handleBookSelection: " + book);

  }

}
