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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.db.entities.EntityBook;
import com.andrewchelladurai.simplebible.model.Book;
import com.andrewchelladurai.simplebible.model.view.BooksViewModel;
import com.andrewchelladurai.simplebible.ui.adapter.BooksAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookListScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.ArrayList;
import java.util.Set;

public class BooksScreen
    extends Fragment
    implements BookListScreenOps {

  private static final String TAG = "BooksScreen";

  private SimpleBibleOps ops;

  private View rootView;

  private BooksViewModel model;

  private BooksAdapter booksAdapter;

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
                .create(BooksViewModel.class);

    booksAdapter = new BooksAdapter(this);
  }

  @Override
  public void onDestroyView() {
    final SearchView searchView = rootView.findViewById(R.id.scr_books_search);
    searchView.setQuery("", true);
    super.onDestroyView();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView:");

    ops.hideKeyboard();
    ops.showNavigationView();

    rootView = inflater.inflate(R.layout.books_screen, container, false);

    final RecyclerView recyclerView = rootView.findViewById(R.id.scr_books_list);
    recyclerView.setAdapter(booksAdapter);

    final SearchView searchView = rootView.findViewById(R.id.scr_books_search);
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
    final Set<Integer> books = Book.getCachedBookList();

    if (books.size() != Book.MAX_BOOKS) {
      refreshContent();
      return;
    }

    model.getAllBooks().observe(getViewLifecycleOwner(), list -> {

      if (list == null || list.isEmpty() || list.size() != Book.MAX_BOOKS) {
        final String msg = getString(R.string.scr_books_msg_invalid_list,
                                     (list == null) ? 0 : list.size(),
                                     Book.MAX_BOOKS);
        Log.e(TAG, "updateContent: " + msg);
        ops.showErrorScreen(msg, true, true);
        return;
      }

      final ArrayList<Book> bookList = new ArrayList<>(list.size());
      for (final EntityBook book : list) {
        bookList.add(new Book(book));
      }

      Book.updateCacheBooks(bookList);
      refreshContent();

    });
  }

  private void refreshContent() {
    Log.d(TAG, "refreshContent:");
    booksAdapter.filterList("");
    booksAdapter.notifyDataSetChanged();
  }

  @Override
  public void handleBookSelection(@Nullable final Book book) {
    Log.d(TAG, "handleBookSelection:");

    if (book == null) {
      Log.e(TAG, "handleBookSelection: cannot show a null book");
      return;
    }

    final Bundle bundle = new Bundle();
    bundle.putInt(ChapterScreen.ARG_INT_BOOK, book.getNumber());
    bundle.putInt(ChapterScreen.ARG_INT_CHAPTER, 1);

    NavHostFragment.findNavController(this)
                   .navigate(R.id.nav_from_scr_book_list_to_scr_chapter, bundle);
  }

}
