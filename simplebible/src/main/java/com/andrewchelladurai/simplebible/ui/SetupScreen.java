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
import androidx.navigation.fragment.NavHostFragment;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.SetupViewModel;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;

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
    model.validateTableData().observe(getViewLifecycleOwner(), count -> {
      Log.d(TAG, "validateDatabase: [" + count + "] rows found using validation query");
      switch (count) {
        case 4:
          Log.d(TAG, "validateDatabase: expected count - validated");
          NavHostFragment.findNavController(this)
                         .navigate(R.id.nav_from_scr_setup_to_scr_home);
          break;
        case -1:
          Log.e(TAG, "validateDatabase: negative count (failed to validate)");
          final String msg = getString(R.string.scr_setup_msg_err_validation);
          Log.e(TAG, "validateDatabase: " + msg);
          ops.showErrorScreen(TAG + " " + msg, true, true);
          break;
        default:
          Log.e(TAG, "validateDatabase: unexpected, perform setup");
          if (model.setupDatabase()) {
            Log.d(TAG, "validateDatabase: successfully setup database");
            NavHostFragment.findNavController(this)
                           .navigate(R.id.nav_from_scr_setup_to_scr_home);
          } else {
            ops.showErrorScreen(getString(R.string.scr_setup_msg_err_creation), true, true);
          }
      }
    });
  }

}
