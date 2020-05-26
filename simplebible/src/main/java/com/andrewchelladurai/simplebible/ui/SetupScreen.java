package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.WorkManager;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.Book;
import com.andrewchelladurai.simplebible.data.entities.EntityBook;
import com.andrewchelladurai.simplebible.model.SetupViewModel;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;

import java.util.ArrayList;
import java.util.UUID;

public class SetupScreen
    extends Fragment {

  private static final String TAG = "SetupScreen";

  private SetupViewModel model;

  private SimpleBibleOps ops;

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
                .create(SetupViewModel.class);

  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedState) {
    final View view = inflater.inflate(R.layout.setup_screen, container, false);

    ops.hideNavigationView();

    final Spanned htmlText = HtmlCompat.fromHtml(getString(R.string.scr_setup_verse),
                                                 HtmlCompat.FROM_HTML_MODE_COMPACT);
    ((TextView) view.findViewById(R.id.scr_setup_text)).setText(htmlText);

    validateDatabase();

    return view;
  }

  private void validateDatabase() {
    Log.d(TAG, "validateDatabase:");
    final LifecycleOwner lifeOwner = getViewLifecycleOwner();

    model.validateTableData().observe(lifeOwner, value -> {

      if (value == 4) {
        Log.d(TAG, "validateDatabase: Expected value[" + value + "] received");
        updateCacheRepository();
        return;
      }

      if (model.getWorkerUuid() != null) {
        return;
      }

      Log.e(TAG, "validateDatabase: no uuid set and value[" + value + "] != 4");
      final WorkManager wManager = WorkManager.getInstance(requireContext());
      @NonNull final UUID uuid = model.setupDatabase(wManager);
      model.setWorkerUuid(uuid);
      monitorDatabaseSetup();

    });
  }

  private void updateCacheRepository() {
    Log.d(TAG, "updateCacheRepository:");
    if (!model.isCacheUpdated()) {
      model.getAllBooks().observe(getViewLifecycleOwner(), list -> {
        if (list == null || list.isEmpty() || list.size() != Book.MAX_BOOKS) {
          String msg = getString(R.string.scr_setup_err_cache_update_failure);
          Log.e(TAG, "updateCacheRepository: " + msg);
          ops.showErrorScreen(msg, true, true);
        } else {

          final ArrayList<Book> bookList = new ArrayList<>(list.size());
          for (final EntityBook book : list) {
            bookList.add(new Book(book));
          }

          model.updateCacheBooks(bookList);
          showHomeScreen();
        }
      });
    } else {
      showHomeScreen();
    }
  }

  private void monitorDatabaseSetup() {
    Log.d(TAG, "monitorDatabaseSetup:");
    final UUID uuid = model.getWorkerUuid();

    if (uuid == null) {
      Log.e(TAG, "monitorDatabaseSetup: null worker UUID");
      return;
    }

    WorkManager.getInstance(requireContext())
               .getWorkInfoByIdLiveData(uuid)
               .observe(getViewLifecycleOwner(), info -> {
                 switch (info.getState()) {
                   case SUCCEEDED:
                     Log.d(TAG, "validateDatabase: database successfully setup");
                     updateCacheRepository();
                     break;
                   case CANCELLED:
                   case FAILED:
                     ops.showErrorScreen(getString(R.string.scr_setup_err_creation), true,
                                         true);
                     break;
                   case BLOCKED:
                   case ENQUEUED:
                     Log.e(TAG, "validateDatabase: database setup is enqueued or blocked");
                     break;
                   case RUNNING:
                   default:
                 }
               });
  }

  private void showHomeScreen() {
    Log.d(TAG, "showHomeScreen:");
    NavHostFragment.findNavController(SetupScreen.this)
                   .navigate(R.id.nav_from_scr_setup_to_scr_home);
  }

}
