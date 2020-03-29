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

    if (model.getWorkerUuid() == null) {
      validateDatabase();
    } else {
      monitorDatabaseSetup();
    }

    return view;
  }

  private void validateDatabase() {
    final LifecycleOwner lifeOwner = getViewLifecycleOwner();
    model.validateTableData().observe(lifeOwner, count -> {

      if (model.getWorkerUuid() != null) {
        return;
      }

      if (count != 4) {
        Log.e(TAG, "validateDatabase: no uuid set and count[" + count + "] != 4");
        final WorkManager wManager = WorkManager.getInstance(requireContext());
        @NonNull final UUID uuid = model.setupDatabase(wManager);
        model.setWorkerUuid(uuid);
        monitorDatabaseSetup();
      }
    });
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
                     NavHostFragment.findNavController(SetupScreen.this)
                                    .navigate(R.id.nav_from_scr_setup_to_scr_home);
                     break;
                   case CANCELLED:
                   case FAILED:
                     ops.showErrorScreen(getString(R.string.scr_setup_msg_err_creation), true,
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

}
