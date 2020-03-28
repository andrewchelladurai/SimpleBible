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
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.SetupViewModel;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;

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

    if (savedState == null) {
      validateDatabase();
    }

    return view;
  }

  private void validateDatabase() {
    Log.d(TAG, "validateDatabase:");
    model.validateTableData().observe(getViewLifecycleOwner(), count -> {
      if (count == -1) {

        final String msg = getString(R.string.scr_setup_msg_err_validation);
        Log.e(TAG, "validateDatabase: " + msg);
        ops.showErrorScreen(TAG + " " + msg, true, true);

      } else if (count >= 0 && count <= 3) {

        final WorkInfo.State cachedDbSetupWorkState = model.getDatabaseSetupWorkerState();
        if (cachedDbSetupWorkState == WorkInfo.State.RUNNING
            || cachedDbSetupWorkState == WorkInfo.State.CANCELLED
            || cachedDbSetupWorkState == WorkInfo.State.BLOCKED
            || cachedDbSetupWorkState == WorkInfo.State.FAILED
            || cachedDbSetupWorkState == WorkInfo.State.SUCCEEDED) {
          return;
        }

        Log.e(TAG, "validateDatabase: incorrect count[" + count + "], need to setup database");

        final WorkManager workManager = WorkManager.getInstance(requireContext());
        final UUID uuid = model.setupDatabase(workManager);

        workManager.getWorkInfoByIdLiveData(uuid).observe(getViewLifecycleOwner(), workInfo -> {

          if (workInfo == null) {
            ops.showErrorScreen(getString(R.string.scr_setup_msg_err_creation), true, true);
            return;
          }

          final WorkInfo.State currentDbSetupWorkState = workInfo.getState();
          model.setDatabaseSetupWorkerState(currentDbSetupWorkState);

          if (currentDbSetupWorkState == WorkInfo.State.CANCELLED
              || currentDbSetupWorkState == WorkInfo.State.FAILED) {
            ops.showErrorScreen(getString(R.string.scr_setup_msg_err_creation), true, true);
          } else if (currentDbSetupWorkState == WorkInfo.State.SUCCEEDED) {
            Log.d(TAG, "validateDatabase: Database setup work successfully completed");
            NavHostFragment.findNavController(SetupScreen.this)
                           .navigate(R.id.nav_from_scr_setup_to_scr_home);
          }

        });

      } else if (count == 4) {

        Log.d(TAG, "validateDatabase: expected count[" + count + "], records seem correct");
        NavHostFragment.findNavController(this)
                       .navigate(R.id.nav_from_scr_setup_to_scr_home);

      } else {
        Log.e(TAG, "validateDatabase: unexpected count[" + count + "]");
      }
    });
  }

}
