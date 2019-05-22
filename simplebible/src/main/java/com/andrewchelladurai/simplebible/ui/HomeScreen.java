package com.andrewchelladurai.simplebible.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.DbSetupService;
import com.andrewchelladurai.simplebible.model.HomeScreenModel;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class HomeScreen
    extends Fragment {

  private static final String TAG = "HomeScreen";

  private static boolean flagSetupFinished = false;

  private SimpleBibleScreenOps actvityOps;

  private FragmentInteractionListener fragListener;

  private ProgressBar pBar;

  private TextView tvVerse;

  private HomeScreenModel model;

  private FloatingActionButton fabShare;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof FragmentInteractionListener)) {
      throw new RuntimeException(
          context.toString() + " must implement FragmentInteractionListener");
    }
    fragListener = (FragmentInteractionListener) context;
    actvityOps = (SimpleBibleScreenOps) context;
    model = ViewModelProviders.of(this).get(HomeScreenModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    final View view = inflater.inflate(R.layout.home_screen, container, false);

    pBar = view.findViewById(R.id.home_src_pbar);
    tvVerse = view.findViewById(R.id.home_src_txt_verse);
    fabShare = view.findViewById(R.id.home_src_fab_share);

    if (savedState == null && !flagSetupFinished) {
      actvityOps.hideNavigationComponent();
      showLoadingScreen();
      startDbSetupService();
    } else {
      showDailyVerse();
    }
    return view;
  }

  @Override
  public void onResume() {
    setupDbServiceListeners();
    super.onResume();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    fragListener = null;
    actvityOps = null;
  }

  private void showLoadingScreen() {
    pBar.setVisibility(VISIBLE);
    fabShare.setVisibility(GONE);
    tvVerse.setText(HtmlCompat.fromHtml(getString(R.string.home_src_txt_verse_loading),
                                        FROM_HTML_MODE_LEGACY));
  }

  private void setupDbServiceListeners() {
    Log.d(TAG, "setupDbServiceListeners() : flagSetupFinished [" + flagSetupFinished + "]");
    if (flagSetupFinished) {
      actvityOps.showNavigationComponent();
      return;
    }

    actvityOps.hideNavigationComponent();
    model.getBookCount().observe(this, bookCount -> {
      if (bookCount == BookUtils.EXPECTED_COUNT) {
        model.getVerseCount().observe(this, verseCount -> {
          if (verseCount == VerseUtils.EXPECTED_COUNT) {
            flagSetupFinished = true;
            actvityOps.showNavigationComponent();
            showDailyVerse();
          }
        });
      }
    });
  }

  private void showDailyVerse() {
    Log.d(TAG, "showDailyVerse:");
    pBar.setVisibility(GONE);
    fabShare.setVisibility(VISIBLE);
    tvVerse.setText(HtmlCompat.fromHtml(getString(R.string.home_src_txt_verse_default),
                                        FROM_HTML_MODE_LEGACY));
    // TODO: 22/5/19 Query the DB and show the Day's verse
  }

  private void startDbSetupService() {
    // create a listener to handle failed database setup
    LocalBroadcastManager.getInstance(requireContext()).registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(final Context context, final Intent intent) {
        String message = ": onReceive: DbSetupService setup failed";
        Log.d(TAG, message);
        actvityOps.showErrorScreen(TAG + message, true);
      }
    }, new IntentFilter(DbSetupService.ACTION_SETUP_FAILURE));

    // start the database setup service
    final Context context = requireContext();
    ContextCompat.startForegroundService(context, new Intent(context, DbSetupService.class));
  }

  interface FragmentInteractionListener {

  }

}
