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
import androidx.lifecycle.ViewModelProvider;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.SetupViewModel;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.Utils;

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

    model.isSetup().observe(getViewLifecycleOwner(), isSetup -> {
      if (isSetup) {
        // model.validateBookTable();
        // model.validateVerseTable();
      } else {
        try {
          model.validateTableData().observe(getViewLifecycleOwner(), recordCount -> {
            if (recordCount != (Utils.MAX_BOOKS + Utils.MAX_VERSES)) {
              Log.e(TAG, "validateDatabase: [" + recordCount + "] != [(MAX_BOOKS + MAX_VERSES)]");
              model.setupDatabase();
            }
          });
        } catch (Exception e) {
          Log.e(TAG, "validateDatabase: Failure validating database", e);
          ops.showErrorScreen("Failure validating database", true, true);
        }
      }
    });

  }

}
