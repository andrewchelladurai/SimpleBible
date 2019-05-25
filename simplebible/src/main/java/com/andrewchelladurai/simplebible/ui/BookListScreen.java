package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.model.BookListScreenModel;
import com.andrewchelladurai.simplebible.ui.adapter.BookListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookListScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.ArrayList;
import java.util.List;

public class BookListScreen
    extends Fragment
    implements BookListScreenOps {

  private static final String TAG = "BookListScreen";
  private BookListAdapter listAdapter;
  private RecyclerView list;
  private AutoCompleteTextView input;
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
                           Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.booklist_screen, container, false);

    input = view.findViewById(R.id.blist_scr_input);
    list = view.findViewById(R.id.blist_scr_list);

    setupInputField();
    setupListView();

    return view;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps = null;
    listAdapter = null;
  }

  private void setupInputField() {
    final ArrayAdapter<String> nameAdapter = model.getBookNameAdapter();
    if (nameAdapter != null && nameAdapter.getCount() == BookUtils.EXPECTED_COUNT) {
      // setup listener to handle clicks on the auto-complete drop down items
      setupInputItemClickListener(nameAdapter);
      // setup listener to handle the search button click on the keyboard
      setupEditorActionListener();

      input.setAdapter(nameAdapter);
      Log.d(TAG, "setupInputField: using cached [" + nameAdapter.getCount() + "] records");
    } else {
      model.getAllBooks().observe(this, list -> {
        if (list == null || list.isEmpty() || list.size() != BookUtils.EXPECTED_COUNT) {
          final String message = getString(R.string.blist_scr_err_incorrect_book_count);
          Log.e(TAG, "setupInputField: " + message);
          activityOps.showErrorScreen(message, true);
          return;
        }
        final ArrayList<String> names = new ArrayList<>(list.size());
        for (final Book book : list) {
          names.add(book.getName());
        }
        model.setBookNameAdapter(new ArrayAdapter<>(
            requireContext(), android.R.layout.simple_dropdown_item_1line, names));

        input.setAdapter(model.getBookNameAdapter());
        setupInputItemClickListener(model.getBookNameAdapter());
        setupEditorActionListener();
      });
      Log.d(TAG, "setupInputField: finished");
    }
  }

  private void setupEditorActionListener() {
    input.setOnEditorActionListener((v, actionId, event) -> {
      if (event == null) {
        final String bookName = input.getText().toString().trim();
        if (!bookName.isEmpty()) {
          activityOps.hideKeyboard();
          handleClickBookSelection(bookName);
          return true;
        }
        return false;
      }
      return false;
    });
    Log.d(TAG, "setupEditorActionListener:");
  }

  private void setupInputItemClickListener(@NonNull final ArrayAdapter<String> nameAdapter) {
    input.setOnItemClickListener((parent, view, position, id) -> {
      final String bookName = nameAdapter.getItem(position);
      if (bookName != null && !bookName.isEmpty()) {
        activityOps.hideKeyboard();
        handleClickBookSelection(bookName);
      } else {
        Log.e(TAG, "setupInputItemClickListener: this is unexpected, how did I get here?");
      }
    });
    Log.d(TAG, "setupInputItemClickListener:");
  }

  private void setupListView() {
    list.setAdapter(listAdapter);

    if (listAdapter.getItemCount() == BookUtils.EXPECTED_COUNT) {
      listAdapter.notifyDataSetChanged();
      Log.d(TAG, "setupListView: using cached version");
      return;
    }

    model.getAllBooks().observe(this, list -> {
      if (list == null || list.isEmpty() || list.size() != BookUtils.EXPECTED_COUNT) {
        final String message = getString(R.string.blist_scr_err_incorrect_book_count);
        Log.e(TAG, "setupInputField: " + message);
        activityOps.showErrorScreen(message, true);
        return;
      }
      listAdapter.refreshList(list);
      listAdapter.notifyDataSetChanged();
    });
    Log.d(TAG, "setupListView() returned");
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
      input.requestFocus();
      String message = getString(R.string.blist_scr_err_invalid_book_name);
      activityOps.showErrorMessage(message);
      return;
    }

    input.setText(null);
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
    final String template = getString(R.string.item_book_details_template);
    final String verseString = getResources().getQuantityString(
        R.plurals.item_book_verses_template, verses, verses);
    final String chapterString = getResources().getQuantityString(
        R.plurals.item_book_chapter_template, chapters, chapters);

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
